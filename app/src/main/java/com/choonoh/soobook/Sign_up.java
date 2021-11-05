package com.choonoh.soobook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Sign_up extends AppCompatActivity {

    private EditText et_email, et_pwd, et_pwd_check, et_nick;
    private Button only_one_btn, sign_up_btn;
    private ImageButton back_btn;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    private String state="상태메시지를 입력해주세요";
    private String pic="none";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setID();
        setEvents();

        back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(v -> {
            Intent intent=new Intent(Sign_up.this, Before_Signup.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

    }
    public void setID() {
        et_email = findViewById(R.id.et_email);
        et_pwd = findViewById(R.id.et_pwd);
        et_pwd_check = findViewById(R.id.et_pwd_check);
        et_nick = findViewById(R.id.et_nickname);
        sign_up_btn = findViewById(R.id.sign_up_btn);
        firebaseAuth = FirebaseAuth.getInstance();
    }
    public void setEvents() {
        sign_up_btn.setOnClickListener(v -> {
            Toast toast;
            Handler handler = new Handler();
            if (et_email.getText().toString().length() <= 0) {
                toast = Toast.makeText(Sign_up.this, "이메일을 입력하세요.", Toast.LENGTH_SHORT); toast.show();
                handler.postDelayed(toast::cancel, 1000);
            } else if (et_pwd.getText().toString().length() <= 0 || et_pwd_check.getText().toString().length() <= 0) {
                toast = Toast.makeText(Sign_up.this, "비밀번호를 입력하세요.", Toast.LENGTH_SHORT); toast.show();
                handler.postDelayed(toast::cancel, 1000);
            } else if(!et_pwd.getText().toString().equals(et_pwd_check.getText().toString())) {
                toast = Toast.makeText(Sign_up.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT); toast.show();
                handler.postDelayed(toast::cancel, 1000);
            }else {
                startSignUp();
            }
        });
    }

    public void postFirebaseDatabase(boolean add){
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        String user_UID = currentUser.getUid();
        String user_email= currentUser.getEmail();
        String user_nick = et_nick.getText().toString();


    }


    public void startSignUp() {
        firebaseAuth.createUserWithEmailAndPassword(et_email.getText().toString(), et_pwd.getText().toString()).addOnCompleteListener(this, task -> {
            postFirebaseDatabase(true);
            Toast toast;
            Handler handler = new Handler();
            if (task.isSuccessful()) {
                FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                String user_UID = currentUser.getUid();
                String user_email= currentUser.getEmail();
                String user_nick = et_nick.getText().toString();
                //db에 user 추가
                HashMap<Object,String> hashMap = new HashMap<>();

                hashMap.put("uid",user_UID);
                hashMap.put("email",user_email);
                hashMap.put("nick",user_nick);
                hashMap.put("state", "상태메세지를 입력해주세요");

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference reference = database.getReference("User");
                reference.child(user_UID).setValue(hashMap);

                HashMap<Object,String> hashMap2 = new HashMap<>();
                hashMap2.put("totalBookNum", Integer.toString(0));
                hashMap2.put("totalReadBookNum", Integer.toString(0));

                hashMap2.put("monday", Integer.toString(0));
                hashMap2.put("tuesday", Integer.toString(0));
                hashMap2.put("wednesday", Integer.toString(0));
                hashMap2.put("thursday", Integer.toString(0));
                hashMap2.put("friday", Integer.toString(0));
                hashMap2.put("saturday", Integer.toString(0));
                hashMap2.put("sunday", Integer.toString(0));

                reference = database.getReference("ReadTime/info");
                reference.child(user_UID).setValue(hashMap2);
/*
            DatabaseReference hopperRef = database.getReference("ReadTime/0ABGKRMonqbw6Pbx3aASBJvxyEa2/").child("info");
            Map<String, Object> hopperUpdates = new HashMap<>();
            hopperUpdates.put("readBookNum", plusOne);

            hopperRef.updateChildren(hopperUpdates);
            */


/*
            Map<String, Object> childUpdates = new HashMap<>();
            Map<String, Object> postValues = null;
            if(true){
                FirebaseuserPost post = new FirebaseuserPost(user_email, user_UID, user_nick, state, pic);
                postValues = post.toMap();
            }
            String root ="/User/"+user_UID;
            childUpdates.put(root, postValues);
            mPostReference.updateChildren(childUpdates);
*/
               // firebaseAuth.signOut();
                Intent intent = new Intent(Sign_up.this, Login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                toast = Toast.makeText(Sign_up.this, "수북의 회원이 되신 것을 환영합니다!", Toast.LENGTH_SHORT); toast.show();
                handler.postDelayed(toast::cancel, 1000);
                finish();
            } else {
                toast = Toast.makeText(Sign_up.this, "이메일,비밀번호 형식(영문, 숫자, 특수문자)을 확인하고 다시 시도해주세요.", Toast.LENGTH_SHORT); toast.show();
                handler.postDelayed(toast::cancel, 1000);
            }
        });
    }
}