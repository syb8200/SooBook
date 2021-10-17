package com.choonoh.soobook;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;
import static com.google.zxing.integration.android.IntentIntegrator.REQUEST_CODE;

public class FriendLibrary extends AppCompatActivity {

    TextView nickname_tv, state_tv;
    ImageView profile_img;
    ImageButton back_btn;
    Button follow_btn;

    private Uri imageUri;
    private String pathUri;

    private Intent intent;
    private FirebaseAuth mAuth;
    private DatabaseReference FriendRef;

    String currentUserId, user_email, user_UID, f_nickname, f_myuid;

    Boolean LikeChecker = false;
    int countLikes;

    private List<ReviewList> mData;

    FirebaseDatabase database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_library);

        nickname_tv = findViewById(R.id.nickname_tv);
        state_tv = findViewById(R.id.state_tv);


        user_email = getIntent().getStringExtra("user_email");
        user_UID = getIntent().getStringExtra("user_UID");
        Log.e(this.getClass().getName(), user_UID + "&" + user_email);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();


        //친구 nickname 받기
        intent = getIntent();
        f_nickname = intent.getStringExtra("nick");
        f_myuid = intent.getStringExtra("uid");


        //친구서재로 띄우기
        DatabaseReference databaseReference = database.getReference().child("User").child(f_myuid).child("nick"); // DB 테이블 연결FirebaseDatabase.getInstance().getReference().addListenerForSingleValueEvent(new ValueEventListener() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                nickname_tv.setText(f_nickname);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //친구 상태메시지 친구서재로 넘김
        DatabaseReference databaseReference2 = database.getReference().child("User").child(f_myuid).child("state"); // DB 테이블 연결FirebaseDatabase.getInstance().getReference().addListenerForSingleValueEvent(new ValueEventListener() {
        databaseReference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Object value = snapshot.getValue(Object.class);
                state_tv.setText(value.toString());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //친구 프로필 이미지 친구서재로 띄우기 (도움!)
        profile_img = findViewById(R.id.profile_img);

        /*
        FirebaseStorage picstorage = FirebaseStorage.getInstance("gs://soobook-donghwa.appspot.com");
        StorageReference storageRef = picstorage.getReference();

        storageRef.child("Profile Images/"+user_UID+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                //이미지 로드 성공시
                Glide.with(getApplicationContext())
                        .load(uri)
                        .into(profile_img);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                //이미지 로드 실패시
                Toast.makeText(getApplicationContext(), "실패", Toast.LENGTH_SHORT).show();
            }
        });
         */




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

    /*
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // Uri
        imageUri = data.getData();
        pathUri = getPath(data.getData());
        Log.e(TAG, "PICK_FROM_ALBUM photoUri : " + imageUri);

        profile_img.setImageURI(imageUri); // 이미지 띄움
    }

    // uri 절대경로 가져오기
    public String getPath(Uri uri) {

        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(getApplicationContext(), uri, proj, null, null, null);

        Cursor cursor = cursorLoader.loadInBackground();
        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();
        return cursor.getString(index);
    }
    */


}
