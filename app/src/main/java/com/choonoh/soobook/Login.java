package com.choonoh.soobook;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;

import android.os.Bundle;
import android.os.Handler;

import android.widget.Button;

import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Login extends AppCompatActivity{

    private EditText et_email, et_pwd;
    private TextView find_email_pwd, sign_up;
    private Button login_btn;
    private FirebaseAuth firebaseAuth;
    private ImageButton back_btn;

    private String user_email, user_pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        back_btn = findViewById(R.id.back_btn);
        find_email_pwd = findViewById(R.id.find_email_pwd);
        sign_up = findViewById(R.id.sign_up_tv);
        login_btn = findViewById(R.id.login_btn);
        et_email = findViewById(R.id.et_email);
        et_pwd = findViewById(R.id.et_pwd);


        back_btn.setOnClickListener(v -> {
            Intent intent=new Intent(Login.this, Before_Signup.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        find_email_pwd.setOnClickListener(v -> {
            Intent intent=new Intent(Login.this, FindPw.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
        sign_up.setOnClickListener(v -> {
            Intent intent = new Intent(Login.this, Sign_up.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
        login_btn.setOnClickListener(v -> {
            Intent intent = new Intent(Login.this, BottomNavActivity.class);
            user_email = et_email.getText().toString();
            user_pwd = et_pwd.getText().toString();
            final Toast toast;
            Handler handler = new Handler();
            if (user_email.length() <= 0) {
                toast = Toast.makeText(Login.this, "이메일을 입력해주세요.", Toast.LENGTH_SHORT); toast.show();
                handler.postDelayed(toast::cancel, 1000);
            } else if (user_pwd.length() <= 0) {
                toast = Toast.makeText(Login.this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT); toast.show();
                handler.postDelayed(toast::cancel, 1000);
            } else {

            }

            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);

        });
    }




}