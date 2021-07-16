package com.choonoh.soobook;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

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
import java.util.HashMap;
import java.util.Map;

import static android.view.View.VISIBLE;


public class Add_frnd extends AppCompatActivity {

    private DatabaseReference mPostReference;

    Button make_frnd_btn, search_frnd_btn;
    EditText et_email_frnd;
    LinearLayout frnd_add_layout;
    String user_email, user_UID;
    ArrayList<User> arrayList;
//    static ArrayList<String> arrayIndex = new ArrayList<String>();
    RecyclerView.Adapter adapter;

    String frnd_email, frnd_uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_frnd);

        et_email_frnd = findViewById(R.id.email_frnd);
        make_frnd_btn = findViewById(R.id.make_frnd_btn);
        frnd_add_layout = findViewById(R.id.frnd_add_layout);
        search_frnd_btn = findViewById(R.id.seach_frnd);
        user_email = getIntent().getStringExtra("user_email");
        user_UID = getIntent().getStringExtra("user_UID");


        search_frnd_btn.setOnClickListener(v -> {
            if(!et_email_frnd.getText().toString().equals("")) {
                RecyclerView recyclerView = findViewById(R.id.recyclerview); // 아디 연결
                recyclerView.setHasFixedSize(true); // 리사이클러뷰 기존성능 강화
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Add_frnd.this);
                recyclerView.setLayoutManager(layoutManager);
                arrayList = new ArrayList<>(); // User 객체를 담을 어레이 리스트 (어댑터쪽으로)

                FirebaseDatabase database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동
                DatabaseReference databaseReference = database.getReference("User/"); // DB 테이블 연결
                databaseReference.orderByChild("email").equalTo(et_email_frnd.getText().toString()).addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        arrayList.clear(); // 기존 배열리스트가 존재하지않게 초기화
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) { // 반복문으로 데이터 List를 추출해냄
                            User user = snapshot.getValue(User.class); // 만들어뒀던 User 객체에 데이터를 담는다.
                            frnd_email = user.getEmail();
                            frnd_uid = user.getUid();
                            arrayList.add(user); // 담은 데이터들을 배열리스트에 넣고 리사이클러뷰로 보낼 준비
                            frnd_add_layout.setVisibility(VISIBLE);
                            make_frnd_btn.setOnClickListener(v -> {
                                    mPostReference = FirebaseDatabase.getInstance().getReference();
                                    Map<String, Object> childUpdates = new HashMap<>();
                                    Map<String, Object> postValues;

                                    FirebasefriendPost post = new FirebasefriendPost(user_UID, frnd_email, frnd_uid);
                                    postValues = post.toMap();

                                    String root ="/Friend/"+user_UID+"/"+frnd_uid+"/";
                                    childUpdates.put(root, postValues);
                                    mPostReference.updateChildren(childUpdates);

                                    Intent intent = new Intent(Add_frnd.this, AdminFrnd.class);
                                    intent.putExtra("user_email", user_email);
                                    intent.putExtra("user_UID", user_UID);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();

                            });
                        }
                        adapter.notifyDataSetChanged(); // 리스트 저장 및 새로고침
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // 디비를 가져오던중 에러 발생 시
                        Log.e("MainActivity", String.valueOf(databaseError.toException())); // 에러문 출력
                    }
                });
                adapter = new CustomAddfrndAdapter(arrayList, Add_frnd.this);
                recyclerView.setAdapter(adapter); // 리사이클러뷰에 어댑터 연결
            } else if(et_email_frnd.getText().toString().equals("")) {
                Toast toast = Toast.makeText(Add_frnd.this, "친구 이메일을 입력해주세요.", Toast.LENGTH_SHORT); toast.show();
                Handler handler = new Handler();
                handler.postDelayed(toast::cancel, 1000);
            }
        });
    }
}
