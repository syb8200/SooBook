package com.choonoh.soobook;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class WishDetailActivity extends AppCompatActivity {


    View reading, del;
    TextView wish_title, wish_auth, wish_pub, wish_isbn, wish_cover;
    ImageView wish_iv;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = firebaseAuth.getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동

    final String user_UID = currentUser.getUid();
    final String user_email = currentUser.getEmail();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_detail);
        wish_auth = findViewById(R.id.wish_auth);
        wish_iv = findViewById(R.id.wish_iv);
        wish_pub = findViewById(R.id.wish_pub);
        wish_title = findViewById(R.id.wish_title);
        wish_isbn = findViewById(R.id.wish_isbn);
        wish_cover = findViewById(R.id.wish_cover);

        String title = getIntent().getStringExtra("title");
        String auth = getIntent().getStringExtra("auth");
        String pub = getIntent().getStringExtra("pub");
        String img = getIntent().getStringExtra("cover");
        String isbn = getIntent().getStringExtra("isbn");
        wish_title.setText(title);
        wish_pub.setText(pub);
        wish_auth.setText(auth);
        wish_isbn.setText(isbn);
        wish_cover.setText(img);
        Glide.with(this).load(img).into(wish_iv);


        reading = findViewById(R.id.wish_reading_book_btn);
        del = findViewById(R.id.wish_del_btn);

        //읽는 책 추가 버튼

        reading.setOnClickListener(v -> {
            if (!isbn.equals("")) {

                    postFirebaseDatabase(true);

                    FirebaseDatabase database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동
                    DatabaseReference data = database.getReference("Wishlist/" + user_UID + "/" + isbn);
                    data.removeValue();
                    Toast toast = Toast.makeText(WishDetailActivity.this, "읽는책에 추가되었습니다.", Toast.LENGTH_SHORT);
                    toast.show();
                    Handler handler = new Handler();
                    handler.postDelayed(toast::cancel, 1000);



                } else {
                    Toast toast = Toast.makeText(WishDetailActivity.this, "이미 등록한 책입니다.", Toast.LENGTH_SHORT);
                    toast.show();
                    Handler handler = new Handler();
                    handler.postDelayed(toast::cancel, 1000);
                }

        });

        del.setOnClickListener(v->{
            FirebaseDatabase database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동
            DatabaseReference data = database.getReference("Wishlist/" + user_UID + "/" + isbn);
            data.removeValue();
            Toast toast = Toast.makeText(WishDetailActivity.this, "위시리스트에서 삭제되었습니다.", Toast.LENGTH_SHORT);
            toast.show();
            Handler handler = new Handler();
            handler.postDelayed(toast::cancel, 1000);
        });
    }
    public void postFirebaseDatabase(boolean add) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy년 MM월dd일 HH시mm분", Locale.KOREA);
        //시간 좀 안맞음 수정해야함
        long now = System.currentTimeMillis();
        Date time = new Date(now);
        String time2 = format.format(time);
        String title_s = wish_title.getText().toString();
        String isbn_s = wish_isbn.getText().toString();
        String auth_s = wish_auth.getText().toString();
        String pub_s = wish_pub.getText().toString();
        String cover_txt = wish_cover.getText().toString();

        DatabaseReference mPostReference = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = null;
        if (add) {
            FirebaseMylibPost post = new FirebaseMylibPost(user_UID, user_email, isbn_s, title_s, cover_txt, time2, auth_s, pub_s);
            postValues = post.toMap();
        }
        String root = "/Mylib/" + user_UID + "/" + isbn_s;
        childUpdates.put(root, postValues);
        mPostReference.updateChildren(childUpdates);
    }



}