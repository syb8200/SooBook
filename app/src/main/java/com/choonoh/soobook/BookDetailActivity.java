package com.choonoh.soobook;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;

import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class BookDetailActivity extends AppCompatActivity {
    TextView reading_txt,isbn, title, auth, pub, star, date, disc;
    ImageView cover;
    String isbn_txt, title_txt, auth_txt, pub_txt, star_txt, date_txt, disc_txt, cover_txt;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = firebaseAuth.getCurrentUser();
    String user_UID = currentUser.getUid();
    String user_email = currentUser.getEmail();
    static ArrayList<String> arrayIndex = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        setDetailView();
        getIntentString();
        setTextViews();
        reading_txt = findViewById(R.id.reading_txt);
        String isbn_s = isbn.getText().toString();
        Log.e(this.getClass().getName(), isbn_s+"클릭");

        reading_txt.setOnClickListener(v -> {

            if (!isbn_txt.equals("")) {
                if (!IsExistID()) {
                    postFirebaseDatabase(true);
                    Toast toast = Toast.makeText(BookDetailActivity.this, "읽는책에 추가되었습니다.", Toast.LENGTH_SHORT);
                    toast.show();
                    Handler handler = new Handler();
                    handler.postDelayed(toast::cancel, 1000);
                    
                    finish();
                } else {
                    Toast toast = Toast.makeText(BookDetailActivity.this, "이미 등록한 책입니다.", Toast.LENGTH_SHORT);
                    toast.show();
                    Handler handler = new Handler();
                    handler.postDelayed(toast::cancel, 1000);
                }

            } else {
                Toast toast = Toast.makeText(BookDetailActivity.this, "error", Toast.LENGTH_SHORT);
                toast.show();
                Handler handler = new Handler();
                handler.postDelayed(toast::cancel, 1000);
            }



        });
    }

    public void postFirebaseDatabase(boolean add){
        SimpleDateFormat format = new SimpleDateFormat ( "yyyy년 MM월dd일 HH시mm분", Locale.KOREA);
        //시간 좀 안맞음 수정해야함
        long now = System.currentTimeMillis();
        Date time = new Date(now);
        String time2 = format.format(time);

        String isbn_s = isbn.getText().toString();
        DatabaseReference mPostReference = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = null;
        if(add){
            FirebaseMylibPost post = new FirebaseMylibPost(user_UID, user_email ,isbn_s, cover_txt, time2);
            postValues = post.toMap();
        }
        String root ="/Mylib/"+user_UID+"/"+isbn_txt;
        childUpdates.put(root, postValues);
        mPostReference.updateChildren(childUpdates);


    }




    public boolean IsExistID() {
        boolean IsExist = arrayIndex.contains(isbn_txt);
        return IsExist;
    }


        public void setDetailView() {
        isbn = findViewById(R.id.tv_isbn);
        title = findViewById(R.id.tv_title);
        auth = findViewById(R.id.tv_auth);
        star = findViewById(R.id.tv_star);
        pub = findViewById(R.id.tv_pub);
        date = findViewById(R.id.tv_date);
        disc = findViewById(R.id.tv_disc);
        cover = findViewById(R.id.iv_cover);
    }
    public void getIntentString() {
        isbn_txt= getIntent().getStringExtra("isbn");
        title_txt= getIntent().getStringExtra("title");
        auth_txt= getIntent().getStringExtra("author");
        star_txt= getIntent().getStringExtra("customerReviewRank");
        pub_txt = getIntent().getStringExtra("publisher");
        date_txt = getIntent().getStringExtra("pubDate");
        disc_txt = getIntent().getStringExtra("description");
        cover_txt = getIntent().getStringExtra("coverSmallUrl");
    }
    public void setTextViews() {
        isbn.setText(isbn_txt);
        title.setText(title_txt);
        auth.setText(auth_txt);
        star.setText(star_txt);
        pub.setText(pub_txt);
        date.setText(date_txt);
        disc.setText(disc_txt);

        Glide.with(this) .load(cover_txt) .override(100, 100) .into(cover);



    }
}

