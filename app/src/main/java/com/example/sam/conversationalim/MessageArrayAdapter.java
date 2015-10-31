package com.example.sam.conversationalim;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class MessageArrayAdapter extends ArrayAdapter {
    private TextView lv;
    private List messageList = new ArrayList();
    private LinearLayout singleMessageContainer;


    public void add(Message m){
        messageList.add(m);
        super.add(m);
    }

    public MessageArrayAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public int size() {
        return this.messageList.size();
    }

    public Message get(int index) {
        return (Message) this.messageList.get(index);
    }

    public void refreshListView(Message m){
        messageList.set(messageList.size() - 1, m);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.activity_chat_singlemessage, parent, false);
        }
        singleMessageContainer = (LinearLayout) row.findViewById(R.id.singleMessageContainer);
        Message chatMessageObj = get(position);
        lv = (TextView) row.findViewById(R.id.singleMessage);
        lv.setText(chatMessageObj.getMessage());
//        lv.setBackgroundResource(chatMessageObj.left() ? R.drawable.a : R.drawable.b);
        singleMessageContainer.setGravity(chatMessageObj.left() ? Gravity.LEFT : Gravity.RIGHT);
        return row;
    }
}
