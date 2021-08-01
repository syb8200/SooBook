package com.choonoh.soobook;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class FindPw extends AppCompatActivity{
    private FirebaseAuth firebaseAuth; //계정인증에 필요한 변수
    private EditText targetEmail; //이메일 입력 EditText
    private Button sendButton; //클릭시, 해당 이메일로 패스워드 재설정 페이지링크를 보냄
    private ImageButton back_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findpw);

        back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(v -> {
            Intent intent=new Intent(FindPw.this, Login.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        sendButton = (Button) findViewById(R.id.send_btn);
        sendButton.setOnClickListener(v -> {

        });

    }



}