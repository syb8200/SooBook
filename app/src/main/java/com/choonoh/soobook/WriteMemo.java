package com.choonoh.soobook;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRadioButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class WriteMemo extends AppCompatActivity {
    AppCompatRadioButton radio_left, radio_right;
    String img, auth, pub, title, isbn, readBookNum, plusOne;
    EditText one_line_review;
    ImageButton back_btn;
    ImageView write_book_img;
    TextView write_book_pub, write_book_title, write_book_auth;
    String s_title, s_content, s_last;
    EditText memo_title, memo_content, memo_last;
    Button save_btn;
    Bitmap bitmap;
    String nick;
    int i;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = firebaseAuth.getCurrentUser();

    private String user_uid = currentUser.getUid();
    private ArrayList<SpinnerItem> mSpinnerList;
    private SpinnerAdapter mAdapter;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_memo);

        Intent intent2 = getIntent();
        img = intent2.getStringExtra("img");
        auth = intent2.getStringExtra("auth");
        pub = intent2.getStringExtra("pub");
        title = intent2.getStringExtra("title");
        isbn = intent2.getStringExtra("isbn");
        Log.e("Mylib", img + ", " + auth + ", " + pub + ", " + title);

        write_book_img = findViewById(R.id.write_book_img);
        write_book_auth = findViewById(R.id.write_book_auth);
        write_book_title = findViewById(R.id.write_book_title);
        write_book_pub = findViewById(R.id.write_book_pub);
        save_btn = findViewById(R.id.button);

        write_book_auth.setText(auth);
        write_book_title.setText(title);
        write_book_pub.setText(pub);

        memo_title = findViewById(R.id.memo_title);
        memo_content = findViewById(R.id.memo_content);
        memo_last = findViewById(R.id.memo_last);


        DatabaseReference databaseReference = database.getReference("User/" + user_uid + "/nick"); // DB 테이블 연결FirebaseDatabase.getInstance().getReference().addListenerForSingleValueEvent(new ValueEventListener() {

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Object value = snapshot.getValue(Object.class);
                nick = value.toString();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });









        Thread mThread = new Thread() {
            @Override
            public void run() {
                try {
                    URL url = new URL(img);

                    // Web에서 이미지를 가져온 뒤
                    // ImageView에 지정할 Bitmap을 만든다
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true); // 서버로 부터 응답 수신
                    conn.connect();

                    InputStream is = conn.getInputStream(); // InputStream 값 가져오기
                    bitmap = BitmapFactory.decodeStream(is); // Bitmap으로 변환

                } catch (MalformedURLException e) {
                    e.printStackTrace();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        mThread.start(); // Thread 실행

        try {
            // 메인 Thread는 별도의 작업 Thread가 작업을 완료할 때까지 대기해야한다
            // join()를 호출하여 별도의 작업 Thread가 종료될 때까지 메인 Thread가 기다리게 한다
            mThread.join();

            // 작업 Thread에서 이미지를 불러오는 작업을 완료한 뒤
            // UI 작업을 할 수 있는 메인 Thread에서 ImageView에 이미지를 지정한다
            write_book_img.setImageBitmap(bitmap);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        //다 읽음, 읽는 중 라디오 버튼, 리뷰 한줄평 란
        radio_left = findViewById(R.id.radio_left);
        radio_right = findViewById(R.id.radio_right);
        one_line_review = (EditText)findViewById(R.id.one_line_review);

        //뒤로가기
        back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(v -> {
            Intent intent=new Intent(WriteMemo.this, SelectReadBook.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        initList();

       i = 0;
        save_btn.setOnClickListener(v -> {

            SimpleDateFormat format = new SimpleDateFormat ( "yyyy년 MM월dd일 HH시mm분", Locale.KOREA);
            long now = System.currentTimeMillis();
            Date time = new Date(now);
            String time2 = format.format(time);
            DatabaseReference memoPostReference = FirebaseDatabase.getInstance().getReference();

            DatabaseReference mPostReference = database.getReference("ReadTime/"+user_uid+"/info/");
            s_title = memo_title.getText().toString();
            s_content = memo_content.getText().toString();
            s_last = memo_last.getText().toString();

            Map<String, Object> childUpdates = new HashMap<>();
            Map<String, Object> postValues = null;
            if(true){
                FirebaseMemoPost post = new FirebaseMemoPost(s_title, s_content, s_last, nick);
                postValues = post.toMap();
            }

            String root1 ="Memo/"+user_uid+"/"+isbn+"/"+time2;

            childUpdates.put(root1, postValues);
            memoPostReference.updateChildren(childUpdates);

            if(!one_line_review.getText().toString().equals("")){
                ValueEventListener postListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            if(i == 1){
                                readBookNum = snapshot.getValue().toString();
                                plusOne = String.valueOf(Integer.parseInt(readBookNum)+1);
                                Log.e("readBookNum", readBookNum);

                                DatabaseReference hopperRef = database.getReference("ReadTime/"+user_uid+"/").child("info");
                                Map<String, Object> hopperUpdates = new HashMap<>();
                                hopperUpdates.put("readBookNum", plusOne);

                                hopperRef.updateChildren(hopperUpdates);
                            }
                            i++;
                        }

                        Intent intent = new Intent(WriteMemo.this, Home.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Getting Post failed, log a message
                        Log.e("StatisticsFragment", "loadPost:onCancelled", databaseError.toException());
                    }
                };
                mPostReference.addValueEventListener(postListener);

                if(true){
                    FirebaseReviewPost post = new FirebaseReviewPost(time2, one_line_review.getText().toString(), "5");
                    postValues = post.toMap();
                }
                String root2 = "Review/"+user_uid+"/"+isbn;
                childUpdates.put(root2, postValues);
                memoPostReference.updateChildren(childUpdates);
            }

            Intent intent = new Intent(WriteMemo.this, Home.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();


        });

        Spinner stars_spinner = findViewById(R.id.stars_spinner);
        mAdapter = new SpinnerAdapter(this, mSpinnerList);
        stars_spinner.setAdapter(mAdapter);


        //스피너 리스트 아이템 클릭했을 때
        stars_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SpinnerItem clickedItem = (SpinnerItem) parent.getItemAtPosition(position);
                Toast.makeText(WriteMemo.this, "selected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //다 읽음, 읽는 중 라디오 버튼 클릭 시
    public void onRadioButtonClicked(View view){
        boolean isSelected = ((AppCompatRadioButton)view).isChecked();
        switch(view.getId()){
            case R.id.radio_left:
                if(isSelected){
                    radio_left.setTextColor(Color.parseColor("#FFFFFF"));
                    radio_right.setTextColor(Color.parseColor("#FF5F68"));
                    one_line_review.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.radio_right:
                if(isSelected){
                    radio_right.setTextColor(Color.parseColor("#FFFFFF"));
                    radio_left.setTextColor(Color.parseColor("#FF5F68"));
                    one_line_review.setVisibility(View.GONE);
                }
                break;
        }
    }

    //별점1~5 이미지 리스트에 추가
    private void initList(){
        mSpinnerList = new ArrayList<>();
        mSpinnerList.add(new SpinnerItem(R.drawable.stars_5));
        mSpinnerList.add(new SpinnerItem(R.drawable.stars_4));
        mSpinnerList.add(new SpinnerItem(R.drawable.stars_3));
        mSpinnerList.add(new SpinnerItem(R.drawable.stars_2));
        mSpinnerList.add(new SpinnerItem(R.drawable.stars_1));
    }
}
