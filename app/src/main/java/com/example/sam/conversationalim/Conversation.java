package com.example.sam.conversationalim;


import java.util.ArrayList;

public class Conversation {
    private ArrayList<Message> myMessages;
    private ArrayList<Message> theirMessages;
    private User otherPerson;
    private String name;
    private String sessionId;

    public Conversation(){ otherPerson = null; }
    public Conversation(User otherPerson){
        this.otherPerson = otherPerson;
    }

    public void addMessage(Message m){

    }

    public User getOtherPerson() {
        return otherPerson;
    }
}
