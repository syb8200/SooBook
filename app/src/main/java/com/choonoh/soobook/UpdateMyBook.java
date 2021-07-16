package com.choonoh.soobook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class UpdateMyBook extends AppCompatActivity {

    String changedRec, changedRecImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_my_book);

        String title, auth, pub, star, rec, uid, isbn, user_email;
        TextView tv_title, tv_auth, tv_pub;
        EditText et_star;
        CheckBox check_good, check_bad;
        ImageButton save_btn;

        title = getIntent().getStringExtra("title");
        auth = getIntent().getStringExtra("auth");
        pub = getIntent().getStringExtra("pub");
        star = getIntent().getStringExtra("star");
        rec = getIntent().getStringExtra("rec");
        uid = getIntent().getStringExtra("uid");
        isbn = getIntent().getStringExtra("isbn");
        user_email = getIntent().getStringExtra("user_email");

        tv_title = findViewById(R.id.book_title_add);
        tv_auth = findViewById(R.id.book_author_add);
        tv_pub = findViewById(R.id.book_pub_add);
        et_star = findViewById(R.id.edit_age);
        check_good = findViewById(R.id.check_good);
        check_bad = findViewById(R.id.check_bad);
        save_btn = findViewById(R.id.save_btn);

        tv_title.setText(title);
        tv_auth.setText(auth);
        tv_pub.setText(pub);
        et_star.setText(star);

        if(rec.equals("추천")) {
            check_good.setChecked(true);
            changedRec = "추천";
            changedRecImage ="https://firebasestorage.googleapis.com/v0/b/soobook-971fa.appspot.com/o/like.png?alt=media&token=6b12539a-b30f-4254-a3fa-f06dd15a9f73";
        } else if(rec.equals("비추천")) {
            check_bad.setChecked(true);
            changedRec = "비추천";
            changedRecImage = "https://firebasestorage.googleapis.com/v0/b/soobook-971fa.appspot.com/o/unlike.png?alt=media&token=cdb9925c-0b07-4b72-9fbe-2ad3bda4ade9";

        }

        check_good.setOnClickListener(v -> {
            changedRec = "추천";
            changedRecImage = "https://firebasestorage.googleapis.com/v0/b/soobook-971fa.appspot.com/o/like.png?alt=media&token=6b12539a-b30f-4254-a3fa-f06dd15a9f73";
            check_bad.setChecked(false);
        });

        check_bad.setOnClickListener(v -> {
            changedRec = "비추천";
            changedRecImage = "https://firebasestorage.googleapis.com/v0/b/soobook-971fa.appspot.com/o/unlike.png?alt=media&token=cdb9925c-0b07-4b72-9fbe-2ad3bda4ade9";
            check_good.setChecked(false);
        });

        save_btn.setOnClickListener(v -> {
            String finalStar = et_star.getText().toString();
            SimpleDateFormat format = new SimpleDateFormat ( "yyyy년 MM월dd일 HH시mm분", Locale.KOREA);

            Date time = new Date();
            String finalTime = format.format(time);

            DatabaseReference mPostReference = FirebaseDatabase.getInstance().getReference();
            Map<String, Object> childUpdates = new HashMap<>();

            childUpdates.put("/Book/" + uid + "/" + isbn + "/rec", changedRec);
            childUpdates.put("/Book/" + uid + "/" + isbn + "/recImage", changedRecImage);
            childUpdates.put("/Book/" + uid + "/" + isbn + "/star", finalStar);
            childUpdates.put("/Book/" + uid + "/" + isbn + "/time", finalTime);
            mPostReference.updateChildren(childUpdates);

            Toast toast = Toast.makeText(UpdateMyBook.this, "수정되었습니다.", Toast.LENGTH_SHORT); toast.show();
            Handler handler = new Handler();
            handler.postDelayed(toast::cancel, 1000);

            Intent intent = new Intent(this, Home.class);
            intent.putExtra("user_email", user_email);
            intent.putExtra("user_UID", uid);
            intent.putExtra("fragment", "my_lib");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });
    }
}