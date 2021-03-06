package com.choonoh.soobook;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
    String img, auth, pub, title, isbn, readBookNum, plusOne, plusOne2;
    ImageButton back_btn;
    ImageView write_book_img;
    TextView write_book_pub, write_book_title, write_book_auth;
    String s_title, s_content, s_last;
    EditText memo_title, memo_content, memo_last, one_line_review;
    Button save_btn;
    Bitmap bitmap;
    String nick,  time2, totalReadBookNum;
    int i, is_read = 0;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = firebaseAuth.getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance(); // ?????????????????? ?????????????????? ??????

    private String user_uid = currentUser.getUid();
    private String user_email = currentUser.getEmail();
    private ArrayList<SpinnerItem> mSpinnerList;
    private SpinnerAdapter mAdapter;
    private DatabaseReference mDatabase;

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
        Log.e("WriteMemo???????????? ??????", img + ", " + auth + ", " + pub + ", " + title + ", " + isbn);

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

        SimpleDateFormat format = new SimpleDateFormat ( "yyyy??? MM???dd??? HH???mm???", Locale.KOREA);
        long now = System.currentTimeMillis();
        Date time = new Date(now);
        time2 = format.format(time);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        Thread mThread = new Thread() {
            @Override
            public void run() {
                try {
                    URL url = new URL(img);
                    // Web?????? ???????????? ????????? ???
                    // ImageView??? ????????? Bitmap??? ?????????
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true); // ????????? ?????? ?????? ??????
                    conn.connect();

                    InputStream is = conn.getInputStream(); // InputStream ??? ????????????
                    bitmap = BitmapFactory.decodeStream(is); // Bitmap?????? ??????

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        mThread.start(); // Thread ??????

        try {
            // ?????? Thread??? ????????? ?????? Thread??? ????????? ????????? ????????? ??????????????????
            // join()??? ???????????? ????????? ?????? Thread??? ????????? ????????? ?????? Thread??? ???????????? ??????
            mThread.join();

            // ?????? Thread?????? ???????????? ???????????? ????????? ????????? ???
            // UI ????????? ??? ??? ?????? ?????? Thread?????? ImageView??? ???????????? ????????????
            write_book_img.setImageBitmap(bitmap);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //??? ??????, ?????? ??? ????????? ??????, ?????? ????????? ???
        radio_left = findViewById(R.id.radio_left);
        radio_right = findViewById(R.id.radio_right);
        one_line_review = findViewById(R.id.one_line_review);

        //???????????? ??????
        back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(v -> {
            Intent intent=new Intent(WriteMemo.this, RecordFragment.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
        initList();

        i = 0;
        save_btn.setOnClickListener(v -> {
            mDatabase.child("User").child(user_uid).child("nick").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (!task.isSuccessful()) {
                        Log.e("firebase", "Error getting data", task.getException());
                    }
                    else {
                        if(memo_title.getText().toString().equals("") || memo_content.getText().toString().equals("") ||
                                memo_last.getText().toString().equals("")){
                            Toast.makeText(getApplicationContext(), "?????? ?????? ????????? ?????????", Toast.LENGTH_SHORT).show();
                        } else{
                            nick = String.valueOf(task.getResult().getValue());
                            s_title = memo_title.getText().toString();
                            s_content = memo_content.getText().toString();
                            s_last = memo_last.getText().toString();

                            DatabaseReference memoPostReference = FirebaseDatabase.getInstance().getReference();
                            Map<String, Object> childUpdates = new HashMap<>();
                            Map<String, Object> postValues = null;

                            FirebaseMemoPost post1 = new FirebaseMemoPost(s_title, s_content, s_last, nick, time2);
                            postValues = post1.toMap();

                            String root1 ="Memo/"+user_uid+"/"+isbn+"/"+time2;

                            childUpdates.put(root1, postValues);
                            memoPostReference.updateChildren(childUpdates);

                            FirebaseReviewPost post2 = new FirebaseReviewPost(time2, one_line_review.getText().toString(), "5", nick, user_uid);
                            postValues = post2.toMap();

                            String root2 = "Review/"+isbn+"/"+user_uid;
                            childUpdates.put(root2, postValues);
                            memoPostReference.updateChildren(childUpdates);

                            DatabaseReference mPostReference = database.getReference("ReadTime/"+user_uid+"/info/");
                            DatabaseReference mPostReference2 = database.getReference("ReadTime/info/"+user_uid);


                            Log.e("is_read", Integer.toString(is_read));
                            if(is_read == 0){
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
                                            postFirebaseDatabase(true);
                                            postFirebaseDatabase1(false);
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
                                    ValueEventListener postListener2 = new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                if(i == 8){
                                                    totalReadBookNum = snapshot.getValue().toString();
                                                    plusOne2 = String.valueOf(Integer.parseInt(totalReadBookNum)+1);
                                                    Log.e("totalReadBookNum", totalReadBookNum);

                                                    DatabaseReference hopperRef = database.getReference("ReadTime/info/").child(user_uid);
                                                    Map<String, Object> hopperUpdates = new HashMap<>();
                                                    hopperUpdates.put("totalReadBookNum", plusOne2);

                                                    hopperRef.updateChildren(hopperUpdates);
                                                }
                                                i++;
                                            }
                                        }
                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                            // Getting Post failed, log a message
                                            Log.e("StatisticsFragment", "loadPost:onCancelled", databaseError.toException());
                                        }
                                    };
                                    mPostReference2.addValueEventListener(postListener2);
                                }
                            }

                            Intent intent = new Intent(WriteMemo.this, Home.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }

                    }
                }
            });


        });

        Spinner stars_spinner = findViewById(R.id.stars_spinner);
        mAdapter = new SpinnerAdapter(this, mSpinnerList);
        stars_spinner.setAdapter(mAdapter);

        //????????? ????????? ????????? ???????????? ???
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

    //??? ??????, ?????? ??? ????????? ?????? ?????? ???
    public void onRadioButtonClicked(View view){
        boolean isSelected = ((AppCompatRadioButton)view).isChecked();
        switch(view.getId()){
            case R.id.radio_left:
                if(isSelected){
                    radio_left.setTextColor(Color.parseColor("#FFFFFF"));
                    radio_right.setTextColor(Color.parseColor("#FF5F68"));
                    one_line_review.setVisibility(View.VISIBLE);
                    is_read = 0;
                }
                break;
            case R.id.radio_right:
                if(isSelected){
                    radio_right.setTextColor(Color.parseColor("#FFFFFF"));
                    radio_left.setTextColor(Color.parseColor("#FF5F68"));
                    one_line_review.setVisibility(View.GONE);
                    is_read = 1;
                }
                break;
        }
    }

    //??????1~5 ????????? ???????????? ??????
    private void initList(){
        mSpinnerList = new ArrayList<>();
        mSpinnerList.add(new SpinnerItem(R.drawable.stars_5));
        mSpinnerList.add(new SpinnerItem(R.drawable.stars_4));
        mSpinnerList.add(new SpinnerItem(R.drawable.stars_3));
        mSpinnerList.add(new SpinnerItem(R.drawable.stars_2));
        mSpinnerList.add(new SpinnerItem(R.drawable.stars_1));
    }

    public void postFirebaseDatabase(boolean add){
        SimpleDateFormat format = new SimpleDateFormat ( "yyyy??? MM???dd??? HH???mm???", Locale.KOREA);
        //?????? ??? ????????? ???????????????
        long now = System.currentTimeMillis();
        Date time = new Date(now);
        String time2 = format.format(time);
        Intent intent2 = getIntent();
        img = intent2.getStringExtra("img");
        auth = intent2.getStringExtra("auth");
        pub = intent2.getStringExtra("pub");
        title = intent2.getStringExtra("title");
        isbn = intent2.getStringExtra("isbn");
        DatabaseReference mPostReference = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = null;
        if(add){
            FirebaseMylibPost post = new FirebaseMylibPost(user_uid, user_email, isbn, title, img, time2, auth, pub);
            postValues = post.toMap();
        }
        String root ="/Oldlib/"+user_uid+"/"+isbn;
        childUpdates.put(root, postValues);
        mPostReference.updateChildren(childUpdates);
    }
    public void postFirebaseDatabase1(boolean add){

        DatabaseReference mPostReference = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = null;
        if(add){
            FirebaseMylibPost post = new FirebaseMylibPost(null,null,null,null,null,null,null,null);
            postValues = post.toMap();
        }
        String root ="/Mylib/"+user_uid+"/"+isbn;
        childUpdates.put(root, postValues);
        mPostReference.updateChildren(childUpdates);
    }

}
