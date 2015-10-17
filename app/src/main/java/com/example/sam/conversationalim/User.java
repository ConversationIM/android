package com.example.sam.conversationalim;

public class User {
    private String userName;
    private String pw;

    public User(String userName, String pw){
        this.userName = userName;
        this.pw = pw;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
