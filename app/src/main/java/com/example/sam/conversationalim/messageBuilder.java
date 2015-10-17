package com.example.sam.conversationalim;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class messageBuilder {
    ArrayList message = new ArrayList();
    Context c;
    EditText e = new EditText(c);
    boolean readyToSend = false;

    Timer timer = new Timer();
    final long DELAY = 2000; // milliseconds

    private TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            timer.cancel();
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public boolean stillTyping(final Editable s) {
        readyToSend = false;

        timer = new Timer();
        timer.schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                       readyToSend = true;
                    }
                },
                DELAY
        );
        return readyToSend;
    }


    public messageBuilder(Context context) {
        c = context;
    }

    public void waitOnMessage() {
        //while (messageAdapter.isTyping()) {
        //  message.add(messageAdapter.nextChar());
        //}

    }

    public boolean isTyping() {
        return true;
    }

    public Message saveMessage() {
        Message m = new Message();
        m.setMessage(message.toString());
        return m;
    }
}

