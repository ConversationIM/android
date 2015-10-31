package com.example.sam.conversationalim;

import org.json.JSONException;
import org.json.JSONObject;

public class User {
    private String id;
    private String username;
    private String first;
    private String last;
    private String pw;
    private String pwc;

    public User(String id, String username, String first, String last, String pw) {
        this.id = id;
        this.username = username;
        this.first = first;
        this.last = last;
        this.pw = pw;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public String getPwc() {
        return pwc;
    }

    public void setPwc(String pwc) {
        this.pwc = pwc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return username;
    }

    public void setEmail(String username) {
        this.username = username;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }


    public JSONObject post(){
        try {
            return new JSONObject("{\n" +
                    "  \"email\": " + getEmail()+ ",\n" +
                    "  \"password\": "+ getPw() + ",\n" +
                    "  \"confirmedPassword\":" + getPwc() + ",\n" +
                    "  \"firstName\":"+ getFirst() +",\n" +
                    "  \"lastName\": "+ getLast() + "\n" +
                    "}"
                            );
        }
        catch (JSONException e){return null;}

        }
}
