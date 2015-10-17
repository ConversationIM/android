package com.example.sam.conversationalim;

public class Message {

    private String sender;
    private String recipient;
    private String timeStamp;
    private String message;
    private boolean flag;

    public Message(){
    }


    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean flag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}