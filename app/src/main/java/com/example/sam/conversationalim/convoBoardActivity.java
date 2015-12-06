package com.example.sam.conversationalim;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;


public class convoBoardActivity extends Activity{

    ListView lv;
    ArrayList<Conversation> listItems = new ArrayList<>();
    ArrayList<String> namesList = new ArrayList<>();
    ArrayAdapter<Conversation> adapter;
    ArrayAdapter<String> nameAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.convoboard);

        adapter=new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                listItems);
        nameAdapter=new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                namesList);
        lv = (ListView) findViewById(R.id.list);
        lv.setAdapter(nameAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent();
                intent.setClass(view.getContext(), messageBoardActivity.class);

                String conversationTitle = getIntent().getExtras().getString("newRoom");
                String conversationId = getIntent().getExtras().getString("conversationID");


                if(conversationTitle != null) addConvoToList(conversationId, conversationTitle);

                String newString;
                Bundle extras = getIntent().getExtras();
                newString = extras.getString("token");
                String token = null;
                intent.putExtra("token", newString);
                startActivity(intent);
            }
        });

        View v = this.findViewById(android.R.id.content);

        addDemoUser(v);
    }

    public void addExampleUser(View view){
            User dummy = new User("email", "pw", "pw", "default", "name");
            newConversation(dummy);
    }

    public void addDemoUser(View view)
    {
        User demo = new User("email", "pw", "default", "default", "pw");
        newConversation(demo);
    }

    private void newConversation(User user){ //new conversation with a user

        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), CreateConversationActivity.class);
        String newString;
        Bundle extras = getIntent().getExtras();
        newString = extras.getString("token");
        String token = null;
        intent.putExtra("token", newString);
        startActivity(intent);

        adapter.add(new Conversation(user));
        nameAdapter.add(new Conversation(user).getOtherPerson().getFirst());

    }

    public void addConvoToList(String id, String name){
        adapter.add(new Conversation(id, name));
        nameAdapter.add(name);
    }



}
