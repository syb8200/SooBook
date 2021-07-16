package com.choonoh.soobook;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class FirebasefriendPost {
    public String user_uid, email, uid;

    public FirebasefriendPost(){
        // Default constructor required for calls to DataSnapshot.getValue(FirebasePost.class)
    }

    public FirebasefriendPost(String user_uid, String email, String uid) {
        this.user_uid = user_uid;
        this.email = email;
        this.uid = uid;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("user_uid", user_uid);
        result.put("email", email);
        result.put("uid", uid);

        return result;
    }
}