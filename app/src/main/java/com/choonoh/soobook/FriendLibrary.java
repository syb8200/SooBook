package com.choonoh.soobook;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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

    ImageButton back_btn;
    Button follow_btn;

    private FirebaseAuth mAuth;
    private DatabaseReference UsersRef, PostsRef, LikesRef;

    String currentUserId;
    Boolean LikeChecker = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_library);

        int countLikes;
        String currentUserId;
        DatabaseReference LikesRef;

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("User").child(currentUserId);;
        LikesRef = FirebaseDatabase.getInstance().getReference().child("Likes");



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
                LikeChecker = true;

                LikesRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {

                        if(dataSnapshot.hasChild(currentUserId)){
                            LikesRef.child(currentUserId).removeValue();
                            LikeChecker = false;

                        } else{
                            LikesRef.child(currentUserId).setValue(true);

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {

                    }
                });
            }




            //Toast toast = Toast.makeText(FriendLibrary.this, "팔로잉을 시작합니다.", Toast.LENGTH_SHORT);
            //toast.show();
            //Handler handler = new Handler();
            //handler.postDelayed(toast::cancel, 1000);
        });



    }
}
