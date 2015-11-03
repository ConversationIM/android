package com.example.sam.conversationalim;

import io.socket.client.Ack;
import io.socket.emitter.Emitter;
import io.socket.client.IO;
import io.socket.client.Socket;

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
    private String token = "";
    JSONObject creds;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        token = extras.getString("token");

        try {
            IO.Options opts = new IO.Options();
            opts.port = 80;
            opts.forceNew = true;
            opts.reconnection = false;
            mSocket = IO.socket("http://staging-magerko2.rhcloud.com", opts);
            Log.i("Set Socket IO", "Socket IO Setting");
        }   catch (URISyntaxException e) {Log.e("Socket Problem", "Socket Setting", e);}

        mSocket.on("error", onError);
        mSocket.on("updated", onUpdated);
        mSocket.on("joined", onJoined);
        mSocket.on("connect", onConnect);
        mSocket.open();
        mSocket.connect();

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


    public void initializeCredentials(){
        try {
            creds = new JSONObject("{ \n\"token\": "+ token +",\n\"room\": \"default\" \n}");
        }
        catch (JSONException e){Log.w("JSON error : ", e);}
    }

    public JSONObject encodeMessage(Message m){
        //refreshListView(m);
        String JSON = "{\n\"sender\": \"" + m.getSender() + "\",\n\"room\": \"default\",\n\"message\": \"" + m.getMessage() + "\"\n}";
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

    private Activity getActivity(){
        return this;
    }

    private Emitter.Listener onError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            final String error = args[0].toString();
            getActivity().runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    Log.w("CONVIM", "error" + error);
                    Toast.makeText(getActivity().getApplicationContext(),
                            "error: " + error, Toast.LENGTH_LONG).show();
                }
            });
        }
    };

    private Emitter.Listener onJoined = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            final String joinReceipt = args[0].toString();
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.w("CONVIM", "joined successfully: " + joinReceipt);
                }
            });
        }
    };

    private Emitter.Listener onUpdated = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            final JSONObject obj = (JSONObject) args[0];
            getActivity().runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    refreshListView(decodeMessage(obj));
                    Log.w("CONVIM", "updated has executed");
                    Toast.makeText(getApplicationContext(), "updated", Toast.LENGTH_SHORT).show();
                }
            });
        }
    };

    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    initializeCredentials();
                    Log.w("connection status ", mSocket.connected()?"true":"false");
                    mSocket.emit("join", new Ack() {
                        public void call(Object...args) {
                            Log.w("Acknowledge: ", "join");
                        }
                    } ,creds);
                    Log.w("CONVIM", "connect has executed");
                    Toast.makeText(getApplicationContext(), "connect", Toast.LENGTH_SHORT).show();
                    mSocket.disconnect();
                }
            });
        }
    };


}
