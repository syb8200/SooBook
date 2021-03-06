package com.choonoh.soobook;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class FirebaseFrPost {
    public String uid;
    public String nick;
    public String state;

    public FirebaseFrPost(){
        // Default constructor required for calls to DataSnapshot.getValue(FirebasePost.class)
    }

    public FirebaseFrPost(String uid, String nick, String state) {
        this.uid= uid;
        this.nick=nick;
        this.state = state;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid",uid);
        result.put("nick", nick);
        result.put("state", state);
        return result;
    }
}