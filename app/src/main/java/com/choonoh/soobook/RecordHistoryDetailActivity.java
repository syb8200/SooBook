package com.choonoh.soobook;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

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

import java.util.ArrayList;
import java.util.List;


public class RecordHistoryDetailActivity extends AppCompatActivity {


    String content, last, now, start, end, title;

    TextView his_content, his_date, his_last, his_time, his_title;
    ImageButton back_btn;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = firebaseAuth.getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동

    final String user_UID = currentUser.getUid();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_history_detail);

        his_content = findViewById(R.id.his_content);
        his_date = findViewById(R.id.his_date);
        his_last = findViewById(R.id.his_last);
        his_title = findViewById(R.id.his_title);
  //      back_btn = findViewById(R.id.his_back_btn);
        //his_time = findViewById(R.id.his_time);
        content = getIntent().getStringExtra("content");
        last = getIntent().getStringExtra("last");
        now = getIntent().getStringExtra("now");
        title = getIntent().getStringExtra("title");
        his_content.setText(content);
        his_last.setText("~"+last+"페이지");
        his_date.setText(now);
        his_title.setText(title);

/* 읽은시간 받아오기 근데 날짜 포맷이 달라서.. 안되네
        DatabaseReference databaseReference = database.getReference("ReadTime/"+user_UID+"/"+now); // DB 테이블 연결

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                                             @Override
                                                             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                 for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                                     ReadTimeList readTimeList = snapshot.getValue(ReadTimeList.class);
                                                                     start = readTimeList.getStartTime();
                                                                     end = readTimeList.getEndTime();
                                                                 }
                                                                 his_time.setText(start + "~" + end);
                                                             }

                                                             @Override
                                                             public void onCancelled(@NonNull DatabaseError error) {

                                                             }
                                                         });

                                                         */

        //뒤로가기 버튼 하지말자..
/*
        back_btn.setOnClickListener(v -> {
            Intent intent=new Intent(RecordHistoryDetailActivity.this, RecordHistoryActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
*/

}
}