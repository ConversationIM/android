package com.example.sam.conversationalim;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.text.Editable;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class messageBoardActivity extends Activity {

    ListView lv;
    ArrayList<Message> listItems = new ArrayList<>();
    ArrayList<String> messageList = new ArrayList<>();
    ArrayAdapter<Message> adapter;
    ArrayAdapter<String> messageAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messageboard);
        final EditText mEditText = (EditText) findViewById(R.id.inputMsg);
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                sendMessage(encodeMessage(new Message(mEditText.getText().toString())));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                listItems);
        messageAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                messageList);
        lv = (ListView) findViewById(R.id.list_view_messages);
        lv.setAdapter(messageAdapter);
        appendListView(new Message());
    }

    public void onNewMessage(Message m){
        appendListView(m);
    }

    public JSONObject encodeMessage(Message m){
        refreshListView(m);
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

    public void sendMessage(JSONObject JSON){

    }

    public void appendListView(Message m){
        adapter.add(m);
        messageAdapter.add(m.getMessage());
    }

    public void refreshListView(Message m){
        listItems.set(listItems.size() - 1, m);
        messageList.set(messageList.size() - 1, m.getMessage());
        adapter.notifyDataSetChanged();
        messageAdapter.notifyDataSetChanged();
    }


}
