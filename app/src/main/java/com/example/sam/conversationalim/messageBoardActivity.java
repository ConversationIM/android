package com.example.sam.conversationalim;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextWatcher;

import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;

import android.text.Editable;
import android.widget.Toast;


import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class messageBoardActivity extends Activity {

    ListView lv;
    private MessageArrayAdapter messageAdapter;
    private Socket mSocket;
    private String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOjEsImV4cCI6MTQ0NjkyNzM2MiwiZW1haWwiOiJhZG1pbkBleGFtcGxlLmNvbSJ9.A2gcDVmoO95fm5-PL89fFakBfHgNTky1OJ_qLOdS6Cg";
    JSONObject creds;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            IO.Options opts = new IO.Options();
            opts.port = 80;
            mSocket = IO.socket("http://staging-magerko2.rhcloud.com", opts);
            mSocket.connect();
            Log.i("Set Socket IO", "Socket IO Setting");
        }   catch (URISyntaxException e) {Log.e("Socket Problem", "Socket Setting", e);}

        try {
             creds = new JSONObject("{ \n\"token\": "+ token +",\n\"room\": \"default\" \n}");
        }
        catch (JSONException e){e.printStackTrace();}



        mSocket.on("connect", new Emitter.Listener() {

            @Override
            public void call(Object... args) {
                mSocket.emit("join", creds);
                Log.w("CONVIM", "connect has executed");
            }
        }).on("joined", new Emitter.Listener() {

            @Override
            public void call(Object... args) {
                Log.w("CONVIM", "joined successfully: " + args[0]);
            }

        }).on("error", new Emitter.Listener() {

            @Override
            public void call(Object... args) {
                Log.w("CONVIM", "error has executed: Error - " + args[0]);
            }

        }).on("updated", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                //if (lv == messageAdapter.getView(messageAdapter.size() - 1, lv, lv)) ;
                JSONObject obj = (JSONObject) args[0];
                refreshListView(decodeMessage(obj));
                Log.w("CONVIM", "updated has executed");
            }

        });



        setContentView(R.layout.messageboard);
        final EditText mEditText = (EditText) findViewById(R.id.inputMsg);
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                sendMessage(encodeMessage(new Message(mEditText.getText().toString(), MainActivity.getUserName())));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        messageAdapter = new MessageArrayAdapter(getApplicationContext(), R.layout.activity_chat_singlemessage);
        lv = (ListView) findViewById(R.id.list_view_messages);
        lv.setAdapter(messageAdapter);
        appendListView(new Message());
    }

    public void onNewMessage(Message m){
        appendListView(m);
    }

    public JSONObject encodeMessage(Message m){
        //refreshListView(m);
        String JSON = "{\n" +
                "    \"sender\": \"" + m.getSender() + "\",\n" +
                "    \"room\": \"default\",\n" +
                "    \"message\": \"" + m.getMessage() + "\"\n" +
                "}";
        try {
            return new JSONObject(JSON);
        }
        catch(JSONException e){
            return null;
        }
    }

    public void sendMessage(JSONObject JSON) {
        //SOCKETS!
        mSocket.emit("updated", JSON);
    }

    public Message decodeMessage(JSONObject JSON){
        refreshListView(new Message(JSON));
        return new Message(JSON);
    }


    public void appendListView(Message m){
        messageAdapter.add(m);
    }

    public void refreshListView(Message m){
        messageAdapter.refreshListView(m);
        messageAdapter.notifyDataSetChanged();
    }

}
