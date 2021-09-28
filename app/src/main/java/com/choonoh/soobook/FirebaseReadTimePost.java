package com.choonoh.soobook;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class FirebaseReadTimePost {
    //public String uid;
    public String readTime;
    public String startTime;
    public String endTime;
    public String date;

    public FirebaseReadTimePost(){
        // Default constructor required for calls to DataSnapshot.getValue(FirebasePost.class)
    }

    public FirebaseReadTimePost(String readTime , String startTime, String endTime, String date) {
        //this.uid = uid;
        this.readTime = readTime;
        this.startTime = startTime;
        this.endTime = endTime;
        this.date = date;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
       // result.put("uid",uid);
        result.put("readTime", readTime);
        result.put("startTime",startTime);
        result.put("endTime", endTime);
        result.put("date", date);
        return result;
    }
}
