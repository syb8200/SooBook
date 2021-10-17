package com.choonoh.soobook;

public class TestFindFriends {

    public String profileimage, nickname, status;

    public TestFindFriends(){

    }

    public TestFindFriends(String profileimage, String nickname, String status) {
        this.profileimage = profileimage;
        this.nickname = nickname;
        this.status = status;
    }

    public String getProfileimage() {
        return profileimage;
    }

    public void setProfileimage(String profileimage) {
        this.profileimage = profileimage;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
