package com.example.sam.conversationalim;

import android.content.Context;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Timer;


public class messageBuilder {
    ArrayList message = new ArrayList();
    Context c;
    EditText e = new EditText(c);

    private Timer time = new Timer();
    private long delay = 2000;


    public messageBuilder(Context context){
        c = context;
    }

    public void waitOnMessage() {
        //while (messageAdapter.isTyping()) {
          //  message.add(messageAdapter.nextChar());
        //}

    }

    public boolean isTyping(){
        return true;
    }

    public Message saveMessage(){
        Message m = new Message();
        m.setMessage(message.toString());
        return m;
    }



}
