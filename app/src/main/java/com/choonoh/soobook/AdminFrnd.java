package com.choonoh.soobook;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class AdminFrnd extends AppCompatActivity {

    private DatabaseReference mPostReference;
    private ImageButton add_frnd;
    private Button del_frnd;
    private ArrayList<User> arrayList;
    static ArrayList<String> arrayIndex = new ArrayList<String>();
    private RecyclerView.Adapter adapter;
    String frnd_email, frnd_uid;
    private String user_email, user_UID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_frnd);

        user_email = getIntent().getStringExtra("user_email");
        user_UID = getIntent().getStringExtra("user_UID");
        add_frnd = findViewById(R.id.add_frnd);
        add_frnd.setOnClickListener(v -> {
            user_UID = getIntent().getStringExtra("user_UID");
            Intent intent = new Intent(AdminFrnd.this, Add_frnd.class);
            intent.putExtra("user_email", user_email);
            intent.putExtra("user_UID", user_UID);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);


        });


        Log.e(this.getClass().getName(), user_email + ", " + user_UID);


                    RecyclerView recyclerView = findViewById(R.id.recyclerview); // 아디 연결
                    recyclerView.setHasFixedSize(true); // 리사이클러뷰 기존성능 강화
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(AdminFrnd.this);
                    recyclerView.setLayoutManager(layoutManager);
                    arrayList = new ArrayList<>(); // User 객체를 담을 어레이 리스트 (어댑터쪽으로)
                    FirebaseDatabase database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동

                    DatabaseReference databaseReference = database.getReference("Friend/"+user_UID+"/"); // DB 테이블 연결
                  databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        arrayList.clear(); // 기존 배열리스트가 존재하지않게 초기화
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) { // 반복문으로 데이터 List를 추출해냄
                            User user = snapshot.getValue(User.class); // 만들어뒀던 User 객체에 데이터를 담는다.
                            frnd_email = user.getEmail();
                            frnd_uid = user.getUid();

                            arrayList.add(user); // 담은 데이터들을 배열리스트에 넣고 리사이클러뷰로 보낼 준비
                        }
                        adapter.notifyDataSetChanged(); // 리스트 저장 및 새로고침

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // 디비를 가져오던중 에러 발생 시
                        Log.e("MainActivity", String.valueOf(databaseError.toException())); // 에러문 출력
                    }
                      });
                adapter = new CustomUserAdapter(arrayList, AdminFrnd.this);

                recyclerView.setAdapter(adapter); // 리사이클러뷰에 어댑터 연결

       }

       private AdapterView.OnItemLongClickListener onClickListener = (parent, view, position, id) -> {
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

}
