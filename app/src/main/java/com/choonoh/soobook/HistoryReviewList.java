package com.choonoh.soobook;

public class HistoryReviewList {
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    String title;
    String content;
    String last;

    public String getNow() {
        return now;
    }

    public void setNow(String now) {
        this.now = now;
    }

    String now;
}