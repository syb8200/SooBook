package com.choonoh.soobook;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SelectReadBook extends AppCompatActivity {
    ImageButton back_btn;
    String book_img, book_isbn, book_title, book_auth, book_pub;
    MylibList[] myLibLists = new MylibList[50];
    String nick ="";
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_read_book);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        String user_UID = currentUser.getUid();
        String user_email = currentUser.getEmail();

        //뒤로가기 버튼
        back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(v -> {
            Intent intent=new Intent(SelectReadBook.this, RecordFragment.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });


        GridView gridView =findViewById(R.id.list_gridview);
        RecordGridListAdapter adapter = new RecordGridListAdapter();


        ///그리드뷰 스크롤 없애기기
        gridView.setVerticalScrollBarEnabled(false);

        FirebaseDatabase database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동
        DatabaseReference databaseReference = database.getReference("Mylib/"+user_UID+"/"); // DB 테이블 연결


        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            int i = 0;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    MylibList mylibList = snapshot.getValue(MylibList.class);
                    book_img = mylibList.getImg();
                    book_isbn = mylibList.getisbn();
                    book_title = mylibList.getTitle();
                    book_auth = mylibList.getauth();
                    book_pub = mylibList.getPub();
                    book_isbn = mylibList.getisbn();

                    myLibLists[i++] = mylibList;
                    Log.e("myLibLists", "" + (i-1) + "th title: " + myLibLists[(i-1)].img);

                    adapter.addItem(mylibList);
                }
                gridView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Mylib", String.valueOf(databaseError.toException())); // 에러문 출력
            }
        });
        gridView.setOnItemClickListener((parent, view, position, id) -> {


       /*     DatabaseReference databaseReference1 = database.getReference("User/"+user_UID+"/nick");
            databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Object value = snapshot.getValue(Object.class);
                    nick = value.toString();

                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
*/

            Log.e("nick", nick);
            Intent intent=new Intent(SelectReadBook.this, WriteMemo.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("img", myLibLists[(position)].img);
            intent.putExtra("title", myLibLists[(position)].title);
            intent.putExtra("auth", myLibLists[(position)].auth);
            intent.putExtra("pub", myLibLists[(position)].pub);
            intent.putExtra("isbn",myLibLists[(position)].isbn);
            intent.putExtra("nick",nick);

            startActivity(intent);
        });
    }
}
