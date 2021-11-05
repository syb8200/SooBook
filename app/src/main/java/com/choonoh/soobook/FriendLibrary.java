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
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;
import static com.google.zxing.integration.android.IntentIntegrator.REQUEST_CODE;

public class FriendLibrary extends AppCompatActivity {

    TextView nickname_tv, state_tv;
    ImageView profile_img;
    //ImageButton back_btn;
    Button follow_btn;

    private Uri imageUri;
    private String pathUri;

    private Intent intent;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    String currentUserId, user_email, user_UID, f_nickname, f_myuid, f_state;

    Boolean LikeChecker = false;
    int countLikes;

    private List<ReviewList> mData;
    FirebaseDatabase database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동
    String book_img, book_isbn, book_title;

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
        f_myuid = intent.getStringExtra("uid");
        f_nickname = intent.getStringExtra("nick");
        Log.e("친구 uid",f_myuid);

        nickname_tv.setText(f_nickname);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("User").child(f_myuid).child("state").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    f_state = String.valueOf(task.getResult().getValue());
                    state_tv.setText(f_state);

                }
            }
        } );


        profile_img = findViewById(R.id.frprofile_img);


        FirebaseStorage picstorage = FirebaseStorage.getInstance("gs://soobook-donghwa.appspot.com");
        StorageReference storageRef = picstorage.getReference();

        storageRef.child("Profile Images/"+f_myuid+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                //이미지 로드 성공시
                Glide.with(FriendLibrary.this)
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




/*
        //뒤로가기 버튼
        back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(v -> {
            Intent intent=new Intent(FriendLibrary.this, BookDetailActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
*/

        //팔로우 버튼
        follow_btn = findViewById(R.id.follow_btn);
        follow_btn.setOnClickListener(v->{


                                DatabaseReference frPostReference = FirebaseDatabase.getInstance().getReference();
                                Map<String, Object> childUpdates = new HashMap<>();
                                Map<String, Object> postValues = null;

                                FirebaseFrPost post1 = new FirebaseFrPost(f_myuid,f_nickname, f_state);
                                postValues = post1.toMap();

                                String root1 ="Friend/"+currentUserId+"/"+f_myuid;

                                childUpdates.put(root1, postValues);
                                frPostReference.updateChildren(childUpdates);

            Toast.makeText(this, "친구가 되었습니다.", Toast.LENGTH_SHORT).show();


    });

        GridView gridView = findViewById(R.id.frlib_gridview);
        GridListAdapter adapter = new GridListAdapter();


        ///그리드뷰 스크롤 없애기기
        gridView.setVerticalScrollBarEnabled(false);



        FirebaseDatabase database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동
        DatabaseReference databaseReference = database.getReference("Mylib/"+f_myuid+"/"); // DB 테이블 연결

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    MylibList mylibList = snapshot.getValue(MylibList.class);
                    book_img = mylibList.getImg();
                    book_isbn = mylibList.getisbn();
                    book_title = mylibList.getTitle();

                    adapter.addItem(mylibList);
                }
                gridView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Mylib", String.valueOf(databaseError.toException())); // 에러문 출력
            }
        });
        GridView gridView1 = findViewById(R.id.frold_gridview);
        GridListAdapter adapter1 = new GridListAdapter();



        DatabaseReference databaseReference1 = database.getReference("Oldlib/"+f_myuid+"/"); // DB 테이블 연결

        databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    MylibList mylibList = snapshot.getValue(MylibList.class);
                    book_img = mylibList.getImg();
                    book_isbn = mylibList.getisbn();
                    book_title = mylibList.getTitle();

                    adapter1.addItem(mylibList);
                }
                gridView1.setAdapter(adapter1);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Oldlib", String.valueOf(databaseError.toException())); // 에러문 출력
            }
        });
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


}}
