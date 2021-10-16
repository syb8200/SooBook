package com.choonoh.soobook;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class FirebaseMylibPost {
    public String uid;
    public String owner;
    public String isbn;
    public String title;
    public String img;
    public String time;
    public String auth;
    public String pub;

    public FirebaseMylibPost(){
        // Default constructor required for calls to DataSnapshot.getValue(FirebasePost.class)
    }

    public FirebaseMylibPost(String uid, String owner , String isbn, String title, String img, String time, String auth, String pub) {
        this.uid = uid;
        this.owner = owner;
        this.isbn = isbn;
        this.title = title;
        this.img = img;
        this.time = time;
        this.auth = auth;
        this.pub = pub;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid",uid);
        result.put("owner", owner);
        result.put("isbn",isbn);
        result.put("title",title);
        result.put("img", img);
        result.put("time",time);
        result.put("auth", auth);
        result.put("pub",pub);
        return result;
    }
}