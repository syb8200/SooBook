package com.choonoh.soobook;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class FirebaseTaregetBookNumPost {
    public String targetBookNum;
    public String readBookNum;
    public String month;

    public FirebaseTaregetBookNumPost(){
        // Default constructor required for calls to DataSnapshot.getValue(FirebasePost.class)
    }

    public FirebaseTaregetBookNumPost(String targetBookNum, String readBookNum, String month) {
        this.targetBookNum = targetBookNum;
        this.readBookNum = readBookNum;
        this.month = month;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("targetBookNum", targetBookNum);
        result.put("readBookNum", readBookNum);
        result.put("month", month);
        return result;
    }
}
