package com.example.sam.conversationalim;

import io.socket.client.Ack;
import io.socket.emitter.Emitter;
import io.socket.client.IO;
import io.socket.client.Socket;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextWatcher;

import android.util.Log;
import android.view.View;
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

    EditText textBox;
    ListView lv;
    private MessageArrayAdapter messageAdapter;
    private SOCKETZANDSHIT socketService;
    private String newline = System.getProperty("line.separator");
    MyReceiver myReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ServiceConnection mServerConn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.d("connected to service", "onServiceConnected");
                SOCKETZANDSHIT.MyLocalBinder binder = (SOCKETZANDSHIT.MyLocalBinder) service;
                socketService = binder.getService();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.d("disconnected service", "onServiceDisconnected");
            }
        };

        myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SOCKETZANDSHIT.MY_ACTION);
        registerReceiver(myReceiver, intentFilter);

        Intent serviceIntent = new Intent();
        serviceIntent.setClass(getApplicationContext(), SOCKETZANDSHIT.class);
        getApplicationContext().bindService(serviceIntent, mServerConn, getApplicationContext().BIND_AUTO_CREATE);



        setContentView(R.layout.messageboard);
        final EditText mEditText = (EditText) findViewById(R.id.inputMsg);
        textBox = mEditText;
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

    public messageBoardActivity getThis(){
        Log.w("getThis", "called Successfully");
        return this;
    }


    public void onUpdated(JSONObject obj){
        try {
            if (obj.getString("message").contains(newline)) {
                messageAdapter.add(new Message());
                textBox.setText("");
            } else refreshListView(decodeMessage(obj));
        } catch (JSONException e) {

        }
        Log.w("CONVIM", decodeMessage(obj).getMessage());
    }


    public JSONObject encodeMessage(Message m) {
        //refreshListView(m);
        String JSON = null;
        JSONObject encodedMessage = new JSONObject();

        try {
            encodedMessage.put("sender", m.getSender());
            encodedMessage.put("conversationId", socketService.getConversationId());
            encodedMessage.put("message", m.getMessage());
        } catch (JSONException e) {
            Log.w("JSON error : ", e);
        }
        return encodedMessage;
    }

    public void sendMessage(JSONObject obj) {
        /*try {
            if (obj.getString("message").contains(newline)) {
                messageAdapter.add(new Message());
                textBox.setText("");
            } else refreshListView(decodeMessage(obj));
        } catch (JSONException e) {

        }*/
        //SOCKETS!
        try {
            Log.d("MESSAGE being SENT", obj.toString());
            socketService.emit("updated", obj.toString());
        } catch (NullPointerException e) {
            Log.e("null pointer!!!", "error", e);
        }
    }

    public Message decodeMessage(JSONObject JSON) {
        //refreshListView(new Message(JSON));
        return new Message(JSON);
    }


    public void appendListView(Message m) {
        messageAdapter.add(m);
        messageAdapter.notifyDataSetChanged();
    }

    public void refreshListView(Message m) {
        messageAdapter.refreshListView(m);
        messageAdapter.notifyDataSetChanged();
    }

    private Activity getActivity() {
        return this;
    }

    private class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            // TODO Auto-generated method stub

            String datapassed = arg1.getExtras().getString("updateEvent");
            Log.w("Triggered by Service!" , String.valueOf(datapassed));
            try{
                JSONObject obj = new JSONObject(datapassed);
                onUpdated(obj);
            }catch(JSONException e){

            }

        }

    }


}
