package com.choonoh.soobook;

import android.os.Bundle;

public class MylibList {
    String isbn;
    String img;
    String title;
    String auth;
    String pub;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    String content;

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg() {
        return img;
    }
    public void setImg(String img) {
        this.img = img;
    }


    public String getisbn() {
        return isbn;
    }
    public void setisbn(String isbn) {
        this.isbn = isbn;
    }

    public String getauth() {
        return auth;
    }
    public void setauth(String auth) {
        this.auth = auth;
    }

    public String getPub() {
        return pub;
    }
    public void setPub(String pub) {
        this.pub = pub;
    }
}
