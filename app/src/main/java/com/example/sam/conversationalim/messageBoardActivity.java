package com.example.sam.conversationalim;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;


public class messageBoardActivity extends Activity {

    ListView lv;
    ArrayList<String> listItems = new ArrayList<>();
    ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.convoboard);

        adapter=new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                listItems);
        lv = (ListView) findViewById(R.id.list);
        lv.setAdapter(adapter);
    }

    public void addExampleUser(View view){
            User dummy = new User("Victor", "pass");
            newConversation(dummy);
    }

    private void newConversation(User user){ //new conversation with a user
        adapter.add(user.getUserName());

    }



}
