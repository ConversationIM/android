package com.example.sam.conversationalim;

import org.json.JSONException;
import org.json.JSONObject;

public class Message {

    private String sender;
    private String message;
    private String room;
    private boolean left;

    public Message(){


        JSONObject clientEvent;
        try {
            clientEvent = new JSONObject("{\n" +
                    "    \"sender\": \"testPerson\",\n" +
                    "    \"room\": \"default\",\n" +
                    "    \"message\": \" \"\n" +
                    "}");
            message = clientEvent.getString("message");
            room = clientEvent.getString("room");
            sender = clientEvent.getString("sender");
            setLeft((sender.equals(MainActivity.getUserName()))? false:true);
        }
        catch (JSONException e){
            message = e.toString();
            room = e.toString();
            sender = e.toString();
        }
    }

    public Message(JSONObject message){

        try {
            this.message = message.getString("message");
            room = "default";
            sender = message.getString("sender");
        }
        catch (JSONException e){
            this.message = e.toString();
            room = e.toString();
            sender = e.toString();
        }
        setLeft((sender.equals(MainActivity.getUserName()))? false:true);
    }

    public Message(String message, String username){
        this.message = message;
        room = "default";
        sender = username;
        setLeft((sender.equals(MainActivity.getUserName()))? false:true);
    }


    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean left() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }
}