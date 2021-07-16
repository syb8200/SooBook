package com.choonoh.soobook;

public class Book {
    private String uid;
    private String isbn;
    private String owner;
    private String auth;
    private String title;
    private String pub;
    private String rec;
    private String star;
    private String recImage;
    private String time;
    private  String nickname;

    public Book(){}

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public String gettitle() {
        return title;
    }

    public void settitle(String title) {
        this.title = title;
    }

    public String getPub() {
        return pub;
    }

    public void setPub(String pub) {
        this.pub = pub;
    }


    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getRec() {
      return rec;
    }

    public void setRec(String rec) {
        this.rec = rec;
    }

    public String getRecImage() {
        return recImage;
    }

    public void setRecImage(String recImage) {
        this.recImage = recImage;
    }


    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
