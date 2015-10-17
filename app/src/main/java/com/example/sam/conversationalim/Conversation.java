package com.example.sam.conversationalim;


public class Conversation {
    private Message[] conversation;
    private User otherPerson;
    private String name;
    private String sessionId;

    public Conversation(User otherPerson){
        this.otherPerson = otherPerson;
    }

    public void addMessage(Message m){

    }
}
