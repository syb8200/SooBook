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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRadioButton;

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
import java.util.Map;

public class WriteMemo extends AppCompatActivity {
    AppCompatRadioButton radio_left, radio_right;
    String img, auth, pub, title, readBookNum, plusOne;
    EditText one_line_review;
    ImageButton back_btn;
    ImageView write_book_img;
    TextView write_book_pub, write_book_title, write_book_auth;
    Button save_btn;
    Bitmap bitmap;
    int i;

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
        Log.e("Mylib", img + ", " + auth + ", " + pub + ", " + title);

        write_book_img = findViewById(R.id.write_book_img);
        write_book_auth = findViewById(R.id.write_book_auth);
        write_book_title = findViewById(R.id.write_book_title);
        write_book_pub = findViewById(R.id.write_book_pub);
        save_btn = findViewById(R.id.button);

        write_book_auth.setText(auth);
        write_book_title.setText(title);
        write_book_pub.setText(pub);

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
            DatabaseReference mPostReference = database.getReference("ReadTime/0ABGKRMonqbw6Pbx3aASBJvxyEa2/info/");

            ValueEventListener postListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if(i == 1){
                            readBookNum = snapshot.getValue().toString();
                            plusOne = String.valueOf(Integer.parseInt(readBookNum)+1);
                            Log.e("readBookNum", readBookNum);

                            DatabaseReference hopperRef = database.getReference("ReadTime/0ABGKRMonqbw6Pbx3aASBJvxyEa2/").child("info");
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
