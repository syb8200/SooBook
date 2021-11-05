package com.choonoh.soobook;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class RecordHistoryActivity extends AppCompatActivity {


    String title, cover, auth, pub, isbn_txt_s;
    ImageView history_iv;
    TextView history_title, history_auth, history_pub;
    List<HistoryReviewList> reviewList;
    RecyclerView recyclerView;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = firebaseAuth.getCurrentUser();
    final String user_UID = currentUser.getUid();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_history);
        title = getIntent().getStringExtra("title");
        cover = getIntent().getStringExtra("cover");
        auth = getIntent().getStringExtra("auth");
        pub = getIntent().getStringExtra("pub");
        isbn_txt_s= getIntent().getStringExtra("isbn");
        history_iv = findViewById(R.id.history_iv);
        history_title = findViewById(R.id.history_title);
        history_pub = findViewById(R.id.history_pub);
        history_auth = findViewById(R.id.history_auth);

        Glide.with(RecordHistoryActivity.this).load(cover).into(history_iv);
        history_title.setText(title);
        history_pub.setText(pub);
        history_auth.setText(auth);


        reviewList = new ArrayList<>();
        recyclerView = findViewById(R.id.review_recycler_view);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        String user_UID = currentUser.getUid();
        String user_email = currentUser.getEmail();


        //리뷰 리스트
        RecyclerView recyclerView =findViewById(R.id.history_view);
        HistoryAdapter adapter = new HistoryAdapter(RecordHistoryActivity.this, reviewList);
        recyclerView.setLayoutManager(new LinearLayoutManager(RecordHistoryActivity.this));
        recyclerView.setAdapter(adapter);



        FirebaseDatabase database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동
        DatabaseReference databaseReference = database.getReference("Memo/"+user_UID+"/"+isbn_txt_s+"/"); // DB 테이블 연결

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    HistoryReviewList historyReviewList = snapshot.getValue(HistoryReviewList.class);

                    title= historyReviewList.getTitle();

                    adapter.addItem(historyReviewList);
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

     /*   //뒤로가기 버튼
        back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(v -> {
            Intent intent=new Intent(BookDetailActivity.this, Home.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });


*/
    }
}