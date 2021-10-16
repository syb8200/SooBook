package com.choonoh.soobook;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class FirebaseMemoPost {
    public String title;
    public String content;
    public String last;
    public String star;


    public FirebaseMemoPost(){
        // Default constructor required for calls to DataSnapshot.getValue(FirebasePost.class)
    }

    public FirebaseMemoPost(String title, String content, String last, String star) {
        this.title = title;
        this.content = content;
        this.last = last;
        this.star = star;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("title",title);
        result.put("content", content);
        result.put("last",last);
        result.put("star", star);
        return result;
    }
}