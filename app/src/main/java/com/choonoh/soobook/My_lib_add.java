package com.choonoh.soobook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.os.StrictMode;
import android.widget.ImageButton;
import android.widget.Toast;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import java.net.URL;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static android.view.View.VISIBLE;

public class My_lib_add  extends AppCompatActivity implements View.OnClickListener {
    TextView title_view, auth_view, pub_view, star_view;
    ImageButton btn_Insert;
    EditText edit_isbn, edit_star;
    TextView title, author, pub ;
    CheckBox check_good;
    CheckBox check_bad;
    String recImage="";
    String star;
    String rec = "";
    String isbn, TITLE, AUTH, PUB, user_email, user_UID;
    String Title = null, Author = null, Pub = null;
    boolean inTitle = false, inAuthor = false, inPub = false;
    int is_result_exist = 0;

    static ArrayList<String> arrayIndex =  new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_lib_add);

        StrictMode.enableDefaults();

        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setPrompt("   ");
        intentIntegrator.setBeepEnabled(true);
        intentIntegrator.setCaptureActivity(QrReaderActivity.class);
        intentIntegrator.initiateScan();


        ImageButton sc = findViewById(R.id.isbn_sc);
        title = findViewById(R.id.book_title_add);
        author = findViewById(R.id.book_author_add);
        pub = findViewById(R.id.book_pub_add);
        edit_star = findViewById(R.id.edit_age);
        edit_isbn = findViewById(R.id.isbn_txt);
        title_view = findViewById(R.id.book_title_view);
        auth_view = findViewById(R.id.book_auth_view);
        pub_view = findViewById(R.id.book_pub_view);
        star_view = findViewById(R.id.text_age);
        user_email = getIntent().getStringExtra("user_email");
        user_UID =getIntent().getStringExtra("user_UID");
        Log.e(this.getClass().getName(), user_UID+"&"+user_email);

        btn_Insert = findViewById(R.id.btn_insert);
        btn_Insert.setOnClickListener(this);
        check_bad = findViewById(R.id.check_bad);
        check_bad.setOnClickListener(this);
        check_good = findViewById(R.id.check_good);
        check_good.setOnClickListener(this);



        sc.setOnClickListener(v -> {
            title_view.setVisibility(VISIBLE);
            auth_view.setVisibility(VISIBLE);
            pub_view.setVisibility(VISIBLE);
            star_view.setVisibility(VISIBLE);
            edit_star.setVisibility(VISIBLE);
            check_good.setVisibility(VISIBLE);
            check_bad.setVisibility(VISIBLE);
            btn_Insert.setVisibility(VISIBLE);
            try{
                URL url = new URL("http://seoji.nl.go.kr/landingPage/SearchApi.do?cert_key=1af3f780faeed316e48de8f0e2541d43eecf78d212859aed298460eddff2bd16" +
                        "&result_style=xml&page_no=1&page_size=10&isbn="+edit_isbn.getText().toString()); //검색 URL부분

                XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
                XmlPullParser parser = parserCreator.newPullParser();

                parser.setInput(url.openStream(), null);

                int parserEvent = parser.getEventType();
                Log.e(this.getClass().getName(), "start parsing");

                while (parserEvent != XmlPullParser.END_DOCUMENT) {
                    switch (parserEvent) {
                        case XmlPullParser.START_TAG://parser가 시작 태그를 만나면 실행
                            if (parser.getName().equals("TITLE")) { //title 만나면 내용을 받을수 있게 하자
                                inTitle = true;
                            }
                            if (parser.getName().equals("AUTHOR")) { //address 만나면 내용을 받을수 있게 하자
                                inAuthor = true;
                            }
                            if (parser.getName().equals("PUBLISHER")) { //mapx 만나면 내용을 받을수 있게 하자
                                inPub = true;
                            }
                            break;
                        case XmlPullParser.TEXT://parser가 내용에 접근했을때
                            if (inTitle) { //isTitle이 true일 때 태그의 내용을 저장.
                                is_result_exist++;
                                Title = parser.getText();
                                inTitle = false;
                            }
                            if (inAuthor) { //isAddress이 true일 때 태그의 내용을 저장.
                                Author = parser.getText();
                                inAuthor = false;
                            }
                            if (inPub) { //isMapx이 true일 때 태그의 내용을 저장.
                                Pub = parser.getText();
                                inPub = false;
                            }
                            break;
                        case XmlPullParser.END_TAG:
                            if (parser.getName().equals("docs")) {
                                Log.e(this.getClass().getName(), String.valueOf(is_result_exist));
                                if(is_result_exist == 1) {
                                    title.setText(Title);
                                    author.setText(Author);
                                    pub.setText(Pub);
                                } else {
                                    Toast toast = Toast.makeText(My_lib_add.this, "책이 검색되지 않습니다.", Toast.LENGTH_SHORT); toast.show();
                                    Handler handler = new Handler();
                                    handler.postDelayed(toast::cancel, 1000);
                                }
                            }
                            break;
                    }
                    parserEvent = parser.next();
                }
            } catch(Exception e){
                Log.e(this.getClass().getName(), "error", e);
            }

        });
    }

    public void postFirebaseDatabase(boolean add){
        SimpleDateFormat format = new SimpleDateFormat ( "yyyy년 MM월dd일 HH시mm분", Locale.KOREA);

        long now = System.currentTimeMillis();
        Date time = new Date(now);
        String time2 = format.format(time);


        DatabaseReference mPostReference = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = null;
        if(add){
            FirebasePost post = new FirebasePost(user_UID, user_email ,isbn, TITLE, PUB, AUTH, star, rec, recImage, time2);
            postValues = post.toMap();
        }
        String root ="/Book/"+user_UID+"/"+isbn;
        childUpdates.put(root, postValues);
        mPostReference.updateChildren(childUpdates);
    }
    public boolean IsExistID(){
        boolean IsExist = arrayIndex.contains(isbn);
        return IsExist;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_insert:
                if(!edit_isbn.getText().toString().equals("")) {
                    isbn = edit_isbn.getText().toString();
                    TITLE = title.getText().toString();
                    AUTH = author.getText().toString();
                    star = edit_star.getText().toString();
                    PUB = pub.getText().toString();

                    title.requestFocus();
                    title.setCursorVisible(true);

                    if(!IsExistID()){
                        postFirebaseDatabase(true);
                        Intent intent = new Intent(this, Home.class);
                        intent.putExtra("user_email", user_email);
                        intent.putExtra("user_UID", user_UID);
                        intent.putExtra("fragment", "my_lib");
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }else{
                        Toast toast = Toast.makeText(My_lib_add.this, "이미 등록한 책입니다.", Toast.LENGTH_SHORT); toast.show();
                        Handler handler = new Handler();
                        handler.postDelayed(toast::cancel, 1000);
                    }
                    edit_isbn.requestFocus();
                    edit_isbn.setCursorVisible(true);
                    break;
                } else {
                    Toast toast = Toast.makeText(My_lib_add.this, "ISBN을 올바르게 입력해주세요.", Toast.LENGTH_SHORT); toast.show();
                    Handler handler = new Handler();
                    handler.postDelayed(toast::cancel, 1000);
                }
            case R.id.check_good:
                check_bad.setChecked(false);
                rec = "추천";
                recImage = "https://firebasestorage.googleapis.com/v0/b/soobook-971fa.appspot.com/o/like.png?alt=media&token=6b12539a-b30f-4254-a3fa-f06dd15a9f73";
                break;
            case R.id.check_bad:
                check_good.setChecked(false);
                rec = "비추천";
                recImage = "https://firebasestorage.googleapis.com/v0/b/soobook-971fa.appspot.com/o/unlike.png?alt=media&token=cdb9925c-0b07-4b72-9fbe-2ad3bda4ade9";
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        TextView isbn = findViewById(R.id.isbn_txt);
        if (resultCode == Activity.RESULT_OK) {
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            isbn.setText( result.getContents() );
            String re = result.getContents();
            Log.e("onActivityResult", "onActivityResult: ." + re);
            try {
                if(re.equals("null")) {
                    Toast toast = Toast.makeText(My_lib_add.this, "ISBN 검색을 다시 시도해주세요.", Toast.LENGTH_SHORT); toast.show();
                    Handler handler = new Handler();
                    handler.postDelayed(toast::cancel, 1000);
                } else {
                    Toast toast = Toast.makeText(My_lib_add.this, "isbn : " + re, Toast.LENGTH_SHORT); toast.show();
                    Handler handler = new Handler();
                    handler.postDelayed(toast::cancel, 1000);
                }
            } catch (Exception e) {
                Toast toast = Toast.makeText(My_lib_add.this, "ISBN 검색을 다시 시도해주세요.", Toast.LENGTH_SHORT); toast.show();
                Handler handler = new Handler();
                handler.postDelayed(toast::cancel, 1000);
            }

        }

        // QR코드/ 바코드를 스캔한 결과

        // result.getFormatName() : 바코드 종류
        // result.getContents() : 바코드 값

    }
}
