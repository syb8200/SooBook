package com.choonoh.soobook;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Before_Signup extends AppCompatActivity {

    private Button sign_up_btn;
    private TextView login_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_before_sign_up);

        sign_up_btn = findViewById(R.id.sign_up_btn);
        login_tv = findViewById(R.id.login_tv);

        sign_up_btn.setOnClickListener(v -> {
            Intent intent=new Intent(Before_Signup.this, Sign_up.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        login_tv.setOnClickListener(v -> {
            Intent intent=new Intent(Before_Signup.this, Login.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

    }

}
