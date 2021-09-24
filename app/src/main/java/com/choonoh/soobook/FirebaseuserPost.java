package com.choonoh.soobook;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class FirebaseuserPost {
    public String email;
    public String uid;
    public String nick;
    public String state;
    public String pic;

    public FirebaseuserPost(){
        // Default constructor required for calls to DataSnapshot.getValue(FirebasePost.class)
    }

    public FirebaseuserPost(String email, String uid, String nick, String state, String pic) {
        this.email=email;
        this.uid=uid;
        this.nick=nick;
        this.state=state;
        this.pic=pic;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("email",email);
        result.put("uid",uid);
        result.put("nick",nick);
        result.put("state",state);
        result.put("pic",pic);

        return result;
    }
}