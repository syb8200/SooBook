package com.choonoh.soobook;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class MyBookDetailView extends AppCompatActivity {

    TextView isbn, title, auth, pub, star, rec, owner, time;
    String isbn_txt, title_txt, auth_txt, pub_txt, star_txt, rec_txt, owner_txt, uid, time_txt;
    private FirebaseAuth firebaseAuth;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mybook_detail_view);


   setDetailView();
   getIntentString();
   setTextViews();
    }
    public void setDetailView() {
       isbn = findViewById(R.id.book_isbn);
       title = findViewById(R.id.book_title);
       auth = findViewById(R.id.book_auth);
       pub = findViewById(R.id.book_pub);
       star = findViewById(R.id.book_star);
       rec = findViewById(R.id.book_rec);
       time = findViewById(R.id.book_time);

    }
    public void getIntentString() {
        uid = getIntent().getStringExtra("uid");
        owner_txt = getIntent().getStringExtra("owner");
        isbn_txt= getIntent().getStringExtra("isbn");
        title_txt= getIntent().getStringExtra("title");
        auth_txt= getIntent().getStringExtra("auth");
        pub_txt= getIntent().getStringExtra("pub");
        star_txt= getIntent().getStringExtra("star");
        rec_txt= getIntent().getStringExtra("rec");
        time_txt= getIntent().getStringExtra("time");

    }
    public void setTextViews() {
        isbn.setText(isbn_txt);
        title.setText(title_txt);
        auth.setText(auth_txt);
        pub.setText(pub_txt);
        star.setText(star_txt);
        rec.setText(rec_txt);
        time.setText(time_txt);
    }
}