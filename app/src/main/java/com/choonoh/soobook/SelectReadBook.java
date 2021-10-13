package com.choonoh.soobook;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SelectReadBook extends AppCompatActivity {

    String book_img, book_isbn, book_title;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_read_book);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        String user_UID = currentUser.getUid();
        String user_email = currentUser.getEmail();


        GridView gridView =findViewById(R.id.list_gridview);
        GridListAdapter adapter = new GridListAdapter();


        ///그리드뷰 스크롤 없애기기
        gridView.setVerticalScrollBarEnabled(false);



        FirebaseDatabase database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동
        DatabaseReference databaseReference = database.getReference("Mylib/"+user_UID+"/"); // DB 테이블 연결

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

    }
}
