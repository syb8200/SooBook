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
    public String img;
    public String time;

    public FirebaseMylibPost(){
        // Default constructor required for calls to DataSnapshot.getValue(FirebasePost.class)
    }

    public FirebaseMylibPost(String uid, String owner , String isbn, String img, String time) {
        this.uid = uid;
        this.owner = owner;
        this.isbn = isbn;
        this.img = img;
        this.time = time;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid",uid);
        result.put("owner", owner);
        result.put("isbn",isbn);
        result.put("img", img);
        result.put("time",time);
        return result;
    }
}