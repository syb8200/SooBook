package com.choonoh.soobook;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class FirebaseReviewPost {
    public String now;
    public String review;
    public String star;



    public FirebaseReviewPost(){
        // Default constructor required for calls to DataSnapshot.getValue(FirebasePost.class)
    }

    public FirebaseReviewPost(String now, String review, String star) {
        this.now = now;
        this.review = review;
        this.star = star;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("now",now);
        result.put("review", review);
        result.put("star", star);
        return result;
    }
}