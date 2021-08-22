package com.choonoh.soobook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
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
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SearchBook  extends AppCompatActivity {
    Button search_isbn;
    ImageButton btn_search;
    TextView title_view, pub_view;
    ImageView book_img_view;
    EditText et_search;
    String user_email, user_UID;
    String Title = null, Pub = null, IMG = null;
    boolean inTitle = false, inPub = false, inImg = false;
    static ArrayList<String> arrayIndex = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_book);

        search_isbn = findViewById(R.id.search_isbn);
        search_isbn.setOnClickListener(v -> {
            Intent intent = new Intent(SearchBook.this, QrReaderActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
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

        user_email = getIntent().getStringExtra("user_email");
        user_UID = getIntent().getStringExtra("user_UID");
        Log.e(this.getClass().getName(), user_UID + "&" + user_email);




        btn_search.setOnClickListener(v -> {
            Log.e(this.getClass().getName(), "클릭");


            URL url = null; 
            try {
                url = new URL("http://book.interpark.com/api/search.api?key=D10E38E11FF9AF9A94BBFCEA6E7C69EB862A51DD8A9A6F6F0141AA42540FEF41&query="+et_search.getText().toString()+"&queryType=isbn");
                        //"" + et_search.getText().toString());


            XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserCreator.newPullParser();

            parser.setInput(url.openStream(), null);

            int parserEvent = parser.getEventType();
            while (parserEvent != XmlPullParser.END_DOCUMENT) {
                switch (parserEvent) {
                    case XmlPullParser.START_TAG://parser가 시작 태그를 만나면 실행

                        Log.e(this.getClass().getName(), "start parsing");

                        if (parser.getName().equals("title")) {
                            inTitle = true;
                        }

                        if (parser.getName().equals("publisher")) {
                            inPub = true;
                        }
                        if (parser.getName().equals("coverLargeUrl")) {
                            inImg = true;
                        }


                        break;
                    case XmlPullParser.TEXT://parser가 내용에 접근했을때
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
                        if (inImg){
                            IMG = parser.getText();
                            Glide.with(this).load(IMG).into(book_img_view);

                            inImg = false;
                        }


                        if(title_view.getText().toString().equals("인터파크도서검색결과"))
                        {
                            title_view.setText("검색결과없음");
                            pub_view.setText("정확한 isbn입력해라");
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
                Toast toast = Toast.makeText(SearchBook.this, "책이 검색되지 않습니다.", Toast.LENGTH_SHORT); toast.show();
                Handler handler = new Handler();
                handler.postDelayed(toast::cancel, 1000);
                e.printStackTrace();
            }
        });

    }
}


