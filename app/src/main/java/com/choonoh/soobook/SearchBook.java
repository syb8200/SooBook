package com.choonoh.soobook;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SearchBook  extends AppCompatActivity {
    Button book_add;
    ImageButton btn_search, back_btn;
    TextView title_view, pub_view;
    ImageView book_img_view;
    EditText et_search;
    String user_email, user_UID, isbn;
    String Title = null, Pub = null, IMG = null, Author = null, Pdate = null, Custrank = null, Desc = null, IMG_s;
    boolean inTitle = false, inAuthor=false, inPub = false, inPdate=false, inImg = false, inIsbn=false, inCustrank=false, inDesc=false, inImg_s=false;
    static ArrayList<String> arrayIndex = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_book);

        //뒤로가기 버튼
        back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(v -> {
            Intent intent = new Intent(SearchBook.this, Home.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

            //키보드 내리기
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

        });

        StrictMode.enableDefaults();

        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setPrompt("   ");
        intentIntegrator.setBeepEnabled(true);
        intentIntegrator.setCaptureActivity(QrReaderActivity.class);
        intentIntegrator.initiateScan();

        btn_search = findViewById(R.id.search_book);
        title_view = findViewById(R.id.book_title_view);
        pub_view = findViewById(R.id.book_pub_view);
        et_search = findViewById(R.id.et_search);
        book_img_view = findViewById(R.id.book_img_view);
        book_add = findViewById(R.id.book_add);

        //처음에는 읽는 책 추가 버튼 보이지 않음
        book_add.setVisibility(View.INVISIBLE);

        user_email = getIntent().getStringExtra("user_email");
        user_UID = getIntent().getStringExtra("user_UID");
        Log.e(this.getClass().getName(), user_UID + "&" + user_email);


        //키보드에 검색 버튼 생성
        et_search.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {

                    //엔터키 눌릴 때 하고 싶은 일
                    Log.e(this.getClass().getName(), "클릭");

                    URL url = null;
                    try {
                        url = new URL("http://book.interpark.com/api/search.api?key=B91AE6F8D1E9702FB8D9CD1FC356A6E0F422AA40510994A9DC06E2196E716175&query=" + et_search.getText().toString() + "&queryType=isbn");
                        //"" + et_search.getText().toString());

                        XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
                        XmlPullParser parser = parserCreator.newPullParser();

                        parser.setInput(url.openStream(), null);

                        int parserEvent = parser.getEventType();
                        while (parserEvent != XmlPullParser.END_DOCUMENT) {
                            switch (parserEvent) {
                                case XmlPullParser.START_TAG://parser가 시작 태그를 만나면 실행

                                    Log.e(this.getClass().getName(), "start parsing");

                                    if (parser.getName().equals("isbn")) {
                                        inIsbn = true;
                                    }
                                    if (parser.getName().equals("title")) {
                                        inTitle = true;
                                    }

                                    if (parser.getName().equals("publisher")) {
                                        inPub = true;
                                    }
                                    if (parser.getName().equals("coverLargeUrl")) {
                                        inImg = true;
                                    }
                                    if(parser.getName().equals("author")){
                                        inAuthor = true;
                                    }
                                    if(parser.getName().equals("pubDate")){
                                        inPdate = true;
                                    }
                                    if(parser.getName().equals("customerReviewRank")){
                                        inCustrank = true;
                                    }
                                    if(parser.getName().equals("description")){
                                        inDesc = true;
                                    }

                                    break;

                                case XmlPullParser.TEXT://parser가 내용에 접근했을때

                                    if (inIsbn) {
                                        isbn = parser.getText();
                                        //클릭했을 때 읽는 책 추가버튼 보임
                                        book_add.setVisibility(View.VISIBLE);
                                        inIsbn = false;
                                    }

                                    if (inTitle) {
                                        Title = parser.getText();
                                        title_view.setText(Title);
                                        inTitle = false;
                                    }

                                    if (inPub) { //isMapx이 true일 때 태그의 내용을 저장.
                                        Pub = parser.getText();
                                        pub_view.setText(Pub);
                                        inPub = false;
                                    }

                                    if (inImg) {
                                        IMG = parser.getText();
                                        Glide.with(SearchBook.this).load(IMG).into(book_img_view);
                                        inImg = false;
                                    }

                                    //책 검색 이후 이미지 클릭하면 책 정보로 넘어감
                                    book_img_view.setOnClickListener(new View.OnClickListener(){
                                        @Override
                                        public void onClick(View v){
                                            Intent intent = new Intent(getApplicationContext(),BookDetailActivity.class);

                                            if (inTitle) {
                                                Title = parser.getText();
                                                inTitle = false;
                                            }

                                            if (inImg_s){
                                                IMG_s = parser.getText();
                                                inImg_s = false;
                                            }

                                            if (inAuthor) {
                                                Author = parser.getText();
                                                inAuthor = false;
                                            }
                                            if (inPub) {
                                                Pub = parser.getText();
                                                inPub = false;
                                            }
                                            if (inPdate) {
                                                Pdate = parser.getText();
                                                inPdate = false;
                                            }

                                            if (inIsbn) {
                                                isbn = parser.getText();
                                                inIsbn = false;
                                            }

                                            if (inCustrank) {
                                                Custrank = parser.getText();
                                                inCustrank = false;
                                            }
                                            if (inDesc) {
                                                Desc = parser.getText();
                                                inDesc = false;
                                            }


                                            Intent intent1 = new Intent(getApplicationContext(), BookDetailActivity.class);
                                            intent1.putExtra("title", Title);
                                            intent1.putExtra("coverSmallUrl", IMG_s);
                                            intent1.putExtra("author", Author);
                                            intent1.putExtra("publisher", Pub);
                                            intent1.putExtra("pubDate", Pdate);
                                            intent1.putExtra("isbn", isbn);
                                            intent1.putExtra("customerReviewRank", Custrank);
                                            intent1.putExtra("description", Desc);

                                            startActivity(intent1);

                                        }
                                    });

                                    if (title_view.getText().toString().equals("인터파크도서검색결과")) {
                                        title_view.setText("검색 결과 없음");
                                        pub_view.setText("정확한 ISBN을 입력해주세요.");
                                        book_img_view.setImageResource(0);
                                        book_add.setVisibility(View.INVISIBLE);
                                    }
                                    break;

                                case XmlPullParser.END_TAG:
                                    if (parser.getName().equals("mobileLink")) {
                                        Log.e(this.getClass().getName(), "끝");
                                        Log.e(this.getClass().getName(), "end parsing");
                                    }
                                    break;
                            }
                            parserEvent = parser.next();
                        }

                    } catch (IOException | XmlPullParserException e) {
                        Toast toast = Toast.makeText(SearchBook.this, "책이 검색되지 않습니다.", Toast.LENGTH_SHORT);
                        toast.show();
                        Handler handler = new Handler();
                        handler.postDelayed(toast::cancel, 1000);
                        e.printStackTrace();
                    }

                    return true;
                }

                return false;
            }

        });


        //검색 버튼
        btn_search.setOnClickListener(v -> {
            Log.e(this.getClass().getName(), "클릭");

            URL url = null;
            try {
                url = new URL("http://book.interpark.com/api/search.api?key=B91AE6F8D1E9702FB8D9CD1FC356A6E0F422AA40510994A9DC06E2196E716175&query=" + et_search.getText().toString() + "&queryType=isbn");
                //"" + et_search.getText().toString());

                XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
                XmlPullParser parser = parserCreator.newPullParser();

                parser.setInput(url.openStream(), null);

                int parserEvent = parser.getEventType();
                while (parserEvent != XmlPullParser.END_DOCUMENT) {
                    switch (parserEvent) {
                        case XmlPullParser.START_TAG://parser가 시작 태그를 만나면 실행

                            Log.e(this.getClass().getName(), "start parsing");

                            if (parser.getName().equals("isbn")) {
                                inIsbn = true;
                            }
                            if (parser.getName().equals("title")) {
                                inTitle = true;
                            }
                            if (parser.getName().equals("publisher")) {
                                inPub = true;
                            }
                            if (parser.getName().equals("coverLargeUrl")) {
                                inImg = true;
                            }
                            if (parser.getName().equals("coverSmallUrl")){
                                inImg_s = true;
                            }
                            if(parser.getName().equals("author")){
                                inAuthor = true;
                            }
                            if(parser.getName().equals("pubDate")){
                                inPdate = true;
                            }
                            if(parser.getName().equals("customerReviewRank")){
                                inCustrank = true;
                            }
                            if(parser.getName().equals("description")){
                                inDesc = true;
                            }

                            break;

                        case XmlPullParser.TEXT://parser가 내용에 접근했을때

                            if (inIsbn) {
                                isbn = parser.getText();
                                //클릭했을 때 읽는 책 추가버튼 보임
                                book_add.setVisibility(View.VISIBLE);
                                inIsbn = false;
                            }

                            if (inTitle) {
                                Title = parser.getText();
                                title_view.setText(Title);
                                inTitle = false;
                            }

                            if (inPub) { //isMapx이 true일 때 태그의 내용을 저장.
                                Pub = parser.getText();
                                pub_view.setText(Pub);
                                inPub = false;
                            }

                            if (inImg) {
                                IMG = parser.getText();
                                Glide.with(this).load(IMG).into(book_img_view);
                                inImg = false;
                            }

                            if (inImg_s) {
                                IMG_s = parser.getText();
                                inImg_s = false;
                            }

                            if(inAuthor){
                                Author = parser.getText();
                                inAuthor = false;
                            }

                            if(inPdate){
                                Pdate = parser.getText();
                                inPdate = false;
                            }

                            if(inCustrank){
                                Custrank = parser.getText();
                                inCustrank = false;
                            }

                            if(inDesc){
                                Desc = parser.getText();
                                inDesc = false;
                            }

                            //책 검색 이후 이미지 클릭하면 책 정보로 넘어감
                            book_img_view.setOnClickListener(new View.OnClickListener(){
                                @Override
                                public void onClick(View v){
                                    Intent intent = new Intent(getApplicationContext(),BookDetailActivity.class);

                                    if (inTitle) {
                                        Title = parser.getText();
                                        inTitle = false;
                                    }

                                    if (inImg_s){
                                        IMG_s = parser.getText();
                                        inImg_s = false;
                                    }

                                    if (inAuthor) {
                                        Author = parser.getText();
                                        inAuthor = false;
                                    }
                                    if (inPub) {
                                        Pub = parser.getText();
                                        inPub = false;
                                    }
                                    if (inPdate) {
                                        Pdate = parser.getText();
                                        inPdate = false;
                                    }

                                    if (inIsbn) {
                                        isbn = parser.getText();
                                        inIsbn = false;
                                    }

                                    if (inCustrank) {
                                        Custrank = parser.getText();
                                        inCustrank = false;
                                    }
                                    if (inDesc) {
                                        Desc = parser.getText();
                                        inDesc = false;
                                    }


                                    Intent intent1 = new Intent(getApplicationContext(), BookDetailActivity.class);
                                    intent1.putExtra("title", Title);
                                    intent1.putExtra("coverSmallUrl", IMG_s);
                                    intent1.putExtra("author", Author);
                                    intent1.putExtra("publisher", Pub);
                                    intent1.putExtra("pubDate", Pdate);
                                    intent1.putExtra("isbn", isbn);
                                    intent1.putExtra("customerReviewRank", Custrank);
                                    intent1.putExtra("description", Desc);

                                    startActivity(intent1);

                                }
                            });

                            if (title_view.getText().toString().equals("인터파크도서검색결과")) {
                                title_view.setText("검색 결과 없음");
                                pub_view.setText("정확한 ISBN을 입력해주세요.");
                                book_img_view.setImageResource(0);
                                book_add.setVisibility(View.INVISIBLE);
                            }

                            break;

                        case XmlPullParser.END_TAG:
                            if (parser.getName().equals("mobileLink")) {
                                Log.e(this.getClass().getName(), "끝");
                                Log.e(this.getClass().getName(), "end parsing");
                            }
                            break;
                    }
                    parserEvent = parser.next();

                }

            } catch (IOException | XmlPullParserException e) {
                Toast toast = Toast.makeText(SearchBook.this, "책이 검색되지 않습니다.", Toast.LENGTH_SHORT);
                toast.show();
                Handler handler = new Handler();
                handler.postDelayed(toast::cancel, 1000);
                e.printStackTrace();
            }
        });

        //읽는 책 추가
        book_add.setOnClickListener(v -> {

            Log.e(this.getClass().getName(), isbn + "클릭");

            if (!isbn.equals("")) {
                if (!IsExistID()) {
                    postFirebaseDatabase(true);
                    Toast toast = Toast.makeText(SearchBook.this, "읽는책에 추가되었습니다.", Toast.LENGTH_SHORT);
                    toast.show();
                    Handler handler = new Handler();
                    handler.postDelayed(toast::cancel, 1000);
                 /*   Intent intent = new Intent(this, Mylib.class);
                    intent.putExtra("user_email", user_email);
                    intent.putExtra("user_UID", user_UID);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();*/
                } else {
                    Toast toast = Toast.makeText(SearchBook.this, "이미 등록한 책입니다.", Toast.LENGTH_SHORT);
                    toast.show();
                    Handler handler = new Handler();
                    handler.postDelayed(toast::cancel, 1000);
                }

            } else {
                Toast toast = Toast.makeText(SearchBook.this, "ISBN을 올바르게 입력해주세요.", Toast.LENGTH_SHORT);
                toast.show();
                Handler handler = new Handler();
                handler.postDelayed(toast::cancel, 1000);
            }

        });
    }


    public void postFirebaseDatabase(boolean add){
        SimpleDateFormat format = new SimpleDateFormat ( "yyyy년 MM월dd일 HH시mm분", Locale.KOREA);
        //시간 좀 안맞음 수정해야함
        long now = System.currentTimeMillis();
        Date time = new Date(now);
        String time2 = format.format(time);

        DatabaseReference mPostReference = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = null;
        if(add){
            FirebaseMylibPost post = new FirebaseMylibPost(user_UID, user_email ,isbn, Title,IMG, time2);
            postValues = post.toMap();
        }
        String root ="/Mylib/"+user_UID+"/"+isbn;
        childUpdates.put(root, postValues);
        mPostReference.updateChildren(childUpdates);
    }

    public boolean IsExistID(){
        boolean IsExist = arrayIndex.contains(isbn);
        return IsExist;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                //Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                //result.getContents 를 이용 데이터 재가공
                //Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();

                et_search.setText(result.getContents());
                Toast.makeText(this, "검색 버튼을 눌러주세요.", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    //다른 곳 터치했을 때 키보드 내리기
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View focusView = getCurrentFocus();
        if(focusView != null){
            Rect rect = new Rect();
            focusView.getGlobalVisibleRect(rect);
            int x = (int) ev.getX(), y = (int) ev.getY();
            if(!rect.contains(x,y)){
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if(imm != null)
                    imm.hideSoftInputFromWindow(focusView.getWindowToken(),0);
                focusView.clearFocus();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

}



