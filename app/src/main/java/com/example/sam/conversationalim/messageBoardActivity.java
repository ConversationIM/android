package com.example.sam.conversationalim;


import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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

        adapter=new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                listItems);
        messageAdapter=new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                messageList);
        lv = (ListView) findViewById(R.id.list_view_messages);
        lv.setAdapter(messageAdapter);
    }


}
