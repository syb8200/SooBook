package com.choonoh.soobook;

import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class BookDetailActivity extends AppCompatActivity {
    LinearLayout reading_book_btn, wishlist_btn;
    TextView total_reviews, isbn, title, auth, pub, star, date, disc, wishlist_txt;
    ImageView cover;
    ImageButton back_btn;
    String isbn_txt, title_txt, auth_txt, pub_txt, star_txt, date_txt, disc_txt, cover_txt;
    String isbn_txt_s, title_txt_s, auth_txt_s, pub_txt_s, star_txt_s, date_txt_s, disc_txt_s, cover_txt_s;
    String review_title, review_content;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = firebaseAuth.getCurrentUser();
    String user_UID = currentUser.getUid();
    String user_email = currentUser.getEmail();
    String user_state;
    static ArrayList<String> arrayIndex = new ArrayList<String>();
    List<ReviewList> reviewList;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        setDetailView();

        //책 검색에서 넘어옴
        getIntentString_search();
        setTextViews_search();

        //기존
        getIntentString();
        setTextViews();

        reviewList = new ArrayList<>();
        recyclerView = findViewById(R.id.review_recycler_view);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        String user_UID = currentUser.getUid();
        String user_email = currentUser.getEmail();


        //리뷰 리스트
        RecyclerView recyclerView =findViewById(R.id.review_recycler_view);
        ReviewAdapter adapter = new ReviewAdapter(BookDetailActivity.this, reviewList);
        recyclerView.setLayoutManager(new LinearLayoutManager(BookDetailActivity.this));
        recyclerView.setAdapter(adapter);



        FirebaseDatabase database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동
        DatabaseReference databaseReference = database.getReference("Review/"+isbn_txt_s+"/"); // DB 테이블 연결


        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ReviewList reviewList = snapshot.getValue(ReviewList.class);

                    review_title = reviewList.getReview();
                    Log.e("review_title", review_title); // 에러문 출력

                 //   String myuid = reviewList.setMyd(user_UID);
                //    myuid = reviewList.getMyuid();


                   // review_content = reviewList.getContent();

                    adapter.addItem(reviewList);
                }
                recyclerView.setAdapter(adapter);
            }


            private void PutDataIntoRecyclerView(List<ReviewList> reviewList){

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("BookDetailActivity", String.valueOf(databaseError.toException())); // 에러문 출력
            }
        });

        //뒤로가기 버튼
        back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(v -> {
            Intent intent=new Intent(BookDetailActivity.this, Home.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        //읽는 책 추가 버튼
        reading_book_btn = findViewById(R.id.reading_book_btn);
        reading_book_btn.setOnClickListener(v -> {
            if (!isbn_txt.equals("")) {
                if (!IsExistID()) {
                    postFirebaseDatabase(true);
                    Toast toast = Toast.makeText(BookDetailActivity.this, "읽는책에 추가되었습니다.", Toast.LENGTH_SHORT);
                    toast.show();
                    Handler handler = new Handler();
                    handler.postDelayed(toast::cancel, 1000);
                    //finish();
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

        //위시리스트 버튼
        wishlist_btn = findViewById(R.id.wishlist_btn);
        wishlist_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                if (!isbn_txt.equals("")) {
                    if (!IsExistID()) {
                        postWishFirebaseDatabase(true);
                        Toast toast = Toast.makeText(BookDetailActivity.this, "위시리스트에 추가되었습니다.", Toast.LENGTH_SHORT);
                        toast.show();
                        Handler handler = new Handler();
                        handler.postDelayed(toast::cancel, 1000);

                        //finish();
                    } else {//이거 동작 안함
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
            }
        });

        //리뷰 전체보기 버튼
        total_reviews = findViewById(R.id.total_reviews);
        total_reviews.setOnClickListener(v -> {
            Intent intent=new Intent(BookDetailActivity.this, TotalReviews.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        String isbn_s = isbn.getText().toString();
        Log.e(this.getClass().getName(), isbn_s+"클릭");
    }

    public void postFirebaseDatabase(boolean add){
        SimpleDateFormat format = new SimpleDateFormat ( "yyyy년 MM월dd일 HH시mm분", Locale.KOREA);
        //시간 좀 안맞음 수정해야함
        long now = System.currentTimeMillis();
        Date time = new Date(now);
        String time2 = format.format(time);
        String title_s = title.getText().toString();
        String isbn_s = isbn.getText().toString();
        String auth_s = auth.getText().toString();
        String pub_s = pub.getText().toString();
        DatabaseReference mPostReference = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = null;
        if(add){
            FirebaseMylibPost post = new FirebaseMylibPost(user_UID, user_email, isbn_s, title_s,cover_txt, time2, auth_s, pub_s);
            postValues = post.toMap();
        }
        String root ="/Mylib/"+user_UID+"/"+isbn_txt;
        childUpdates.put(root, postValues);
        mPostReference.updateChildren(childUpdates);
    }

    public void postWishFirebaseDatabase(boolean add){
        SimpleDateFormat format = new SimpleDateFormat ( "yyyy년 MM월dd일 HH시mm분", Locale.KOREA);
        long now = System.currentTimeMillis();
        Date time = new Date(now);
        String time2 = format.format(time);
        String title_s = title.getText().toString();
        String isbn_s = isbn.getText().toString();
        String auth_s = auth.getText().toString();
        String pub_s = pub.getText().toString();
        DatabaseReference mPostReference = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = null;
        if(add){
            FirebaseMylibPost post = new FirebaseMylibPost(user_UID, user_email, isbn_s, title_s,cover_txt, time2, auth_s, pub_s);
            postValues = post.toMap();
        }
        String root ="/Wishlist/"+user_UID+"/"+isbn_txt;
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

    //책 검색에서 연결 (get)
    public void getIntentString_search(){
        isbn_txt_s= getIntent().getStringExtra("isbn");
        title_txt_s= getIntent().getStringExtra("title");
        auth_txt_s= getIntent().getStringExtra("author");
        star_txt_s= getIntent().getStringExtra("customerReviewRank");
        pub_txt_s = getIntent().getStringExtra("publisher");
        date_txt_s = getIntent().getStringExtra("pubDate");
        disc_txt_s = getIntent().getStringExtra("description");
        cover_txt_s = getIntent().getStringExtra("coverSmallUrl");
    }

    //책 검색에서 연결 (set)
    public void setTextViews_search(){
        isbn.setText(isbn_txt_s);
        title.setText(title_txt_s);
        auth.setText(auth_txt_s);
        star.setText(star_txt_s);
        pub.setText(pub_txt_s);
        date.setText(date_txt_s);
        disc.setText(disc_txt_s);

        Glide.with(this) .load(cover_txt_s) .override(100, 100) .into(cover);
    }
}

