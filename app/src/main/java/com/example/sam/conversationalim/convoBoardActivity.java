package com.example.sam.conversationalim;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
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
    ArrayAdapter<Conversation> adapter;
    SocketsMain socketService;

    String conversationName;
    String conversationId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter=new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                listItems);
        try {
            listItems = socketService.getListConversations();
            for (int i = 0; i < listItems.size(); i++) {
                adapter.add(listItems.get(i));
            }
            adapter.notifyDataSetChanged();
            Log.d("LIST CONTENTS", adapter.toString());
        }catch (NullPointerException e){
            e.printStackTrace();
        }

        ServiceConnection mServerConn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.d("connect CONVOBOARD", "onServiceConnected");
                SocketsMain.MyLocalBinder binder = (SocketsMain.MyLocalBinder) service;
                socketService = binder.getService();
                try {
                    for (int i = 0; i < listItems.size(); i++) {
                        adapter.add(listItems.get(i));
                    }
                }catch (NullPointerException e){
                    Log.w("empyt list", "fill it");
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.d("disconnected service", "onServiceDisconnected");
            }
        };

        Intent serviceIntent = new Intent();
        serviceIntent.setClass(getApplicationContext(), SocketsMain.class);
        getApplicationContext().bindService(serviceIntent, mServerConn, getApplicationContext().BIND_AUTO_CREATE);



        setContentView(R.layout.convoboard);
        lv = (ListView) findViewById(R.id.list);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent();
                intent.setClass(view.getContext(), messageBoardActivity.class);
                Conversation c = (Conversation) adapterView.getItemAtPosition(i);
                socketService.joinSpecifiedConversation(c);
                startActivity(intent);
            }
        });

        View v = this.findViewById(android.R.id.content);

    }

    public void refreshListView(View v){
        try {
            listItems = socketService.getListConversations();
            Log.d("LIST CONTENTS", listItems.toString());
            adapter.clear();
            for (int i = 0; i < listItems.size(); i++) {
                adapter.add(listItems.get(i));
            }
            adapter.notifyDataSetChanged();
            Log.d("ADAPTER CONTENTS", adapter.toString());
        }catch (NullPointerException e){
            e.printStackTrace();
        }
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

    public void createNewConversation(View view){ //new conversation with a user

        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), CreateConversationActivity.class);
        startActivity(intent);


    }

    private void newConversation(User user){ //new conversation with a user
        adapter.add(new Conversation(user));
    }


    public void addConvoToList(String id, String name){
        adapter.add(new Conversation(id, name));
    }



}
