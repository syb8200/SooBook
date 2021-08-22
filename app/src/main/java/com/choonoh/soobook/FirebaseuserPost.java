package com.choonoh.soobook;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by DowonYoon on 2017-07-11.
 */

@IgnoreExtraProperties
public class FirebaseuserPost {
    public String email;
    public String uid;
    public String nick;

    public FirebaseuserPost(){
        // Default constructor required for calls to DataSnapshot.getValue(FirebasePost.class)
    }

    public FirebaseuserPost(String email, String uid, String nick) {
        this.email=email;
        this.uid=uid;
        this.nick=nick;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("email",email);
        result.put("uid",uid);
        result.put("nick",nick);

        return result;
    }
}