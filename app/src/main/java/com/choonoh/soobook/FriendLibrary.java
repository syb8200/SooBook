package com.choonoh.soobook;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class FriendLibrary extends AppCompatActivity {

    TextView nickname_tv, state_tv;
    ImageView profile_img;
    ImageButton back_btn;
    Button follow_btn;

    private FirebaseAuth mAuth;
    private DatabaseReference UsersRef, LikesRef;

    String currentUserId, user_email, user_UID;
    Boolean LikeChecker = false;
    int countLikes;

    FirebaseDatabase database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_library);

        user_email = getIntent().getStringExtra("user_email");
        user_UID = getIntent().getStringExtra("user_UID");
        Log.e(this.getClass().getName(), user_UID + "&" + user_email);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("User").child(currentUserId);
        LikesRef = FirebaseDatabase.getInstance().getReference().child("Likes").child(currentUserId);


        //뒤로가기 버튼
        back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(v -> {
            Intent intent=new Intent(FriendLibrary.this, BookDetailActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });


        //팔로우 버튼
        follow_btn = findViewById(R.id.follow_btn);
        follow_btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){

                Bundle bundle = new Bundle();
                bundle.putString("user_email", user_email);
                bundle.putString("user_UID", user_UID);




            }




            //Toast toast = Toast.makeText(FriendLibrary.this, "팔로잉을 시작합니다.", Toast.LENGTH_SHORT);
            //toast.show();
            //Handler handler = new Handler();
            //handler.postDelayed(toast::cancel, 1000);
        });



    }
}
