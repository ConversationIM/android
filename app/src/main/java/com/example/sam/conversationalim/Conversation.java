package com.example.sam.conversationalim;


import java.util.ArrayList;

public class Conversation {
    private ArrayList<Message> myMessages;
    private ArrayList<Message> theirMessages;
    private User otherPerson;
    private String name;
    private String sessionId;

    private String conversationID;
    private String title;


    public Conversation(){ otherPerson = null; }

    public Conversation(String conversationID, String title){
        this.conversationID = conversationID;
        this.title = title;
    }

    public Conversation(User otherPerson){
        this.otherPerson = otherPerson;
    }


    public User getOtherPerson() {
        return otherPerson;
    }

    @Override
    public String toString(){
        return title + "\n" + conversationID;
    }

    public String getConversationID(){
        return conversationID;
    }
}
