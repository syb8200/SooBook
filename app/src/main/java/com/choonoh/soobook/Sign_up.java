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

import java.util.HashMap;
import java.util.Map;

public class Sign_up extends AppCompatActivity {

    private EditText et_email, et_pwd, et_pwd_check, et_nickname;
    private Button only_one_btn, sign_up_btn;
    private ImageButton back_btn;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    private DatabaseReference mPostReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setID();
        setEvents();
    }
    public void setID() {
        back_btn = findViewById(R.id.back_btn);
        et_email = findViewById(R.id.et_email);
        et_pwd = findViewById(R.id.et_pwd);
        et_pwd_check = findViewById(R.id.et_pwd_check);
        et_nickname = findViewById(R.id.et_nickname);
        sign_up_btn = findViewById(R.id.sign_up_btn);
        firebaseAuth = FirebaseAuth.getInstance();
    }
    public void setEvents() {

        back_btn.setOnClickListener(v -> {
            Intent intent=new Intent(Sign_up.this, Before_Signup.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

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
            } //else if(!et_nickname.getText().toString().length() <= 0) {
                //toast = Toast.makeText(Sign_up.this, "별명을 입력하세요.", Toast.LENGTH_SHORT); toast.show();
                //handler.postDelayed(toast::cancel, 1000);
           // }
            else {
                startSignUp();
            }
        });
    }




    public void startSignUp() {
        firebaseAuth.createUserWithEmailAndPassword(et_email.getText().toString(), et_pwd.getText().toString()).addOnCompleteListener(this, task -> {
            Toast toast;
            Handler handler = new Handler();
            if (task.isSuccessful()) {
                currentUser = firebaseAuth.getCurrentUser();
                String user_UID = currentUser.getUid();
                String user_email =currentUser.getEmail();
                Log.e(this.getClass().getName(), currentUser.getUid());

               //db에 user 추가
                mPostReference = FirebaseDatabase.getInstance().getReference();
                Map<String, Object> childUpdates = new HashMap<>();
                Map<String, Object> postValues = null;
                if(true){
                    FirebaseuserPost post = new FirebaseuserPost(user_email, user_UID);
                    postValues = post.toMap();}
                String root ="/User/"+user_UID;
                childUpdates.put(root, postValues);
                mPostReference.updateChildren(childUpdates);



                    firebaseAuth.signOut();
                Intent intent = new Intent(Sign_up.this, Login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                toast = Toast.makeText(Sign_up.this, "수북의 회원이 되신 것을 환영합니다!", Toast.LENGTH_SHORT); toast.show();
                handler.postDelayed(toast::cancel, 1000);
                finish();
            } else {
                toast = Toast.makeText(Sign_up.this, "중복 이메일이 존재합니다. 다른 이메일을 사용해주세요.", Toast.LENGTH_SHORT); toast.show();
                handler.postDelayed(toast::cancel, 1000);
            }
        });
    }
}