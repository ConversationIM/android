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

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URISyntaxException;

public class messageBoardActivity extends Activity {

    ListView lv;
    private MessageArrayAdapter messageAdapter;
    private Socket mSocket;
    private String token;

    {
        try {
            //mSocket = IO.socket("http://staging-magerko2.rhcloud.com/mvp");
            mSocket = IO.socket("http://nma55251.pagekite.me/mvp");
            Log.i("ConversationIM", "Initializing socket connection");
        } catch (URISyntaxException e) {
            Log.e("ConversationIM", "Problem while initializing", e);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        token = extras.getString("token");

        mSocket.on("error", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                Log.d("ConversationIM", "args length: " + args.length);
                Error error = new Error(args[0].toString());
                Throwable t = (Throwable) args[0];
                StringWriter sw = new StringWriter();
                t.printStackTrace(new PrintWriter(sw));
                Log.d("ConversationIM", String.format("An error ocurred: %s", sw.toString()));
                //Toast.makeText(getActivity().getApplicationContext(), "error: " + error, Toast.LENGTH_LONG).show();
            }
        })/*.on("updated", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                final JSONObject obj = (JSONObject) args[0];
                refreshListView(decodeMessage(obj));
                Log.w("CONVIM", "updated has executed");
                //Toast.makeText(getApplicationContext(), "updated", Toast.LENGTH_SHORT).show();
            }

        })*/.on("joined", new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        String joinReceipt = args[0].toString();
                        Log.w("ConversationIM", "joined successfully: " + joinReceipt);
                    }
                }
        ).on("connect", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                JSONObject joinRequest = makeJoinRequest();
                Log.i("ConversationIM", "Connected: now attempting to join room...");
                Log.d("ConversationIM", joinRequest.toString());
                mSocket.emit("join", joinRequest);
                //Log.w("CONVIM", "connect has executed");
                //Toast.makeText(getApplicationContext(), "connect", Toast.LENGTH_SHORT).show();
                //mSocket.disconnect();
            }
        });
        mSocket.connect();


        setContentView(R.layout.messageboard);
        final EditText mEditText = (EditText) findViewById(R.id.inputMsg);
        mEditText.addTextChangedListener(
                new TextWatcher() {
                   @Override
                   public void beforeTextChanged(CharSequence charSequence, int i, int i1,
                                                 int i2) {

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


    public JSONObject makeJoinRequest() {
        JSONObject joinRequest = null;
        try {
            joinRequest = new JSONObject("{ \n\"token\": " + token + ",\n\"room\": \"default\" \n}");
        } catch (JSONException e) {
            Log.w("JSON error : ", e);
        }

        return joinRequest;
    }

    public JSONObject encodeMessage(Message m) {
        //refreshListView(m);
        String JSON = "{\n\"sender\": \"" + m.getSender() + "\",\n\"room\": \"default\",\n\"message\": \"" + m.getMessage() + "\"\n}";
        try {
            return new JSONObject(JSON);
        } catch (JSONException e) {
            return null;
        }
    }

    public void sendMessage(JSONObject JSON) {
        //SOCKETS!
        mSocket.emit("updated", JSON);
    }

    public Message decodeMessage(JSONObject JSON) {
        refreshListView(new Message(JSON));
        return new Message(JSON);
    }


    public void appendListView(Message m) {
        messageAdapter.add(m);
    }

    public void refreshListView(Message m) {
        messageAdapter.refreshListView(m);
        messageAdapter.notifyDataSetChanged();
    }

    private Activity getActivity() {
        return this;
    }


}
