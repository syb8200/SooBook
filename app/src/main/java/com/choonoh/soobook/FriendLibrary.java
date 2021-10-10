package com.choonoh.soobook;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class FriendLibrary extends AppCompatActivity {

    ImageButton back_btn;
    Button follow_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_library);

        back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(v -> {
            Intent intent=new Intent(FriendLibrary.this, BookDetailActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        follow_btn = findViewById(R.id.follow_btn);
        follow_btn.setOnClickListener(v->{
            Toast toast = Toast.makeText(FriendLibrary.this, "팔로잉을 시작합니다.", Toast.LENGTH_SHORT);
            toast.show();
            Handler handler = new Handler();
            handler.postDelayed(toast::cancel, 1000);
        });



    }
}
