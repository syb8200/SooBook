package com.choonoh.soobook;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by DowonYoon on 2017-07-11.
 */

@IgnoreExtraProperties
public class FirebasePost {
    public String uid;
    public String owner;
    public String isbn;
    public String title;
    public String pub;
    public String auth;
    public String star;
    public String rec;
    public String recImage;
    public String time;

    public FirebasePost(){
        // Default constructor required for calls to DataSnapshot.getValue(FirebasePost.class)
    }

    public FirebasePost(String uid, String owner ,String isbn, String title,String pub, String auth, String star, String rec, String recImage, String time) {
        this.uid = uid;
        this.owner = owner;
        this.isbn = isbn;
        this.title = title;
        this.pub = pub;
        this.auth = auth;
        this.star = star;
        this.rec = rec;
        this.recImage = recImage;
        this.time = time;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid",uid);
        result.put("owner", owner);
        result.put("isbn",isbn);
        result.put("title", title);
        result.put("pub", pub);
        result.put("auth", auth);
        result.put("star", star);
        result.put("rec", rec);
        result.put("recImage",recImage);
        result.put("time",time);
        return result;
    }
}