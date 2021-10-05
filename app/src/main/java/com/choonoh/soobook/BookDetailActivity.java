package com.choonoh.soobook;

import android.media.Image;
import android.os.Bundle;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;


public class BookDetailActivity extends AppCompatActivity {
    TextView isbn, title, auth, pub, star, date, disc;
    ImageView cover;
    String isbn_txt, title_txt, auth_txt, pub_txt, star_txt, date_txt, disc_txt, cover_txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);


        setDetailView();
        getIntentString();
        setTextViews();
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

