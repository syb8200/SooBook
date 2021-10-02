package com.choonoh.soobook;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class Mylib extends AppCompatActivity {

    private DatabaseReference mPostReference;
    private TextView add_mylib;
    private Button del_frnd;
    private ArrayList<MylibList> arrayList;
    static ArrayList<String> arrayIndex = new ArrayList<String>();
    private RecyclerView.Adapter adapter;
    String book_img, book_isbn;
    private String user_email, user_UID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mylib);

        user_email = getIntent().getStringExtra("user_email");
        user_UID = getIntent().getStringExtra("user_UID");
        add_mylib = findViewById(R.id.btn_add);
        add_mylib.setOnClickListener(v -> {
            user_UID = getIntent().getStringExtra("user_UID");
            Intent intent = new Intent(Mylib.this, SearchBook.class);
            intent.putExtra("user_email", user_email);
            intent.putExtra("user_UID", user_UID);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
        Log.e(this.getClass().getName(), user_email + ", " + user_UID);

     /*   RecyclerView recyclerView = findViewById(R.id.mylib_recycler_view); // 아디 연결
        recyclerView.setHasFixedSize(true); // 리사이클러뷰 기존성능 강화
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Mylib.this);
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>(); // User 객체를 담을 어레이 리스트 (어댑터쪽으로)
        FirebaseDatabase database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동

        DatabaseReference databaseReference = database.getReference("Mylib/"+user_UID+"/"); // DB 테이블 연결
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear(); // 기존 배열리스트가 존재하지않게 초기화
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) { // 반복문으로 데이터 List를 추출해냄
                    MylibList mylibList = snapshot.getValue(MylibList.class); // 만들어뒀던 User 객체에 데이터를 담는다.
                    book_img = mylibList.getImg();
                    book_isbn = mylibList.getisbn();
                    arrayList.add(mylibList); // 담은 데이터들을 배열리스트에 넣고 리사이클러뷰로 보낼 준비
                }
                adapter.notifyDataSetChanged(); // 리스트 저장 및 새로고침

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 디비를 가져오던중 에러 발생 시
                Log.e("Mylib", String.valueOf(databaseError.toException())); // 에러문 출력
            }
        });
        //adapter = new MylibAdapter(arrayList, Mylib.this);

        recyclerView.setAdapter(adapter); // 리사이클러뷰에 어댑터 연결

    }*/

   /* private AdapterView.OnItemLongClickListener onClickListener = (parent, view, position, id) -> {
        Log.d("Long Click", "position = " + position);
        //     final String[] nowData = arrayData.get(position).split("\\s+");

        AlertDialog.Builder dialog = new AlertDialog.Builder(AdminFrnd.this);
        dialog.setTitle("데이터 삭제")
                .setMessage("해당 데이터를 삭제 하시겠습니까?" + "\n" )
                .setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Toast.makeText(AdminFrnd.this, "데이터를 삭제했습니다.", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(AdminFrnd.this, "삭제를 취소했습니다.", Toast.LENGTH_SHORT).show();
                    }
                })
                .create()
                .show();
        return false;
    };
*/
    }
}