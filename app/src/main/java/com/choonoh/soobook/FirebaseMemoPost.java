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
    public String nick;
    public String now;

    public FirebaseMemoPost(){
        // Default constructor required for calls to DataSnapshot.getValue(FirebasePost.class)
    }

    public FirebaseMemoPost(String title, String content, String last, String nick, String now) {
        this.title = title;
        this.content = content;
        this.last = last;
        this.nick = nick;
        this.now=now;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("title",title);
        result.put("content", content);
        result.put("last",last);
        result.put("nick", nick);
        result.put("now", now);

        return result;
    }
}