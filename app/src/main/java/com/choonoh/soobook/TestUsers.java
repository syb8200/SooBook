package com.choonoh.soobook;

public class TestUsers {
    private String email, nick, state, uid;

    public TestUsers(){

    }

    public TestUsers(String email, String nick, String state, String uid) {
        this.email = email;
        this.nick = nick;
        this.state = state;
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
