package com.choonoh.soobook;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SearchBookTitle extends AppCompatActivity {

    EditText ed_title;
    String title;
    List<BookList> bestsellerList;
    RecyclerView recyclerView;
    ImageButton btn_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_title);
        ed_title = findViewById(R.id.ed_title_search);
        title = ed_title.getText().toString();
        btn_search = findViewById(R.id.search_book);

        btn_search.setOnClickListener(v -> {
            bestsellerList = new ArrayList<>();
            recyclerView = findViewById(R.id.search_recycler_view);
            GetData getData = new GetData();
            getData.execute();

        });
    }
        public class GetData extends AsyncTask<String, String, String>{
            String JSON_URL = "http://book.interpark.com/api/search.api?key=D10E38E11FF9AF9A94BBFCEA6E7C69EB862A51DD8A9A6F6F0141AA42540FEF41&query=" + title+"&output=json";

            @Override
            protected String doInBackground(String... strings){

                String current = "";

                try{
                    URL url;
                    HttpURLConnection urlConnection = null;

                    try{
                        url = new URL(JSON_URL);
                        urlConnection = (HttpURLConnection) url.openConnection();

                        InputStream is = urlConnection.getInputStream();
                        InputStreamReader isr = new InputStreamReader(is);

                        int data = isr.read();
                        while(data != -1){

                            current += (char) data;
                            data = isr.read();

                        }
                        return current;

                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }finally {
                        if(urlConnection != null){
                            urlConnection.disconnect();
                        }
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }


                return current;
            }

            @Override
            protected void onPostExecute(String s){

                Log.e("onPostExecute", "잘됨");
                try{
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONArray("item");

                    for(int i = 0; i<jsonArray.length(); i++){
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        Log.e("json", "잘됨");
                        BookList bestseller = new BookList();
                        bestseller.setTitle(jsonObject1.getString("title"));
                        bestseller.setAuthor(jsonObject1.getString("author"));
                        bestseller.setPublisher(jsonObject1.getString("publisher"));
                        bestseller.setCoverSmallUrl(jsonObject1.getString("coverSmallUrl"));

                        bestsellerList.add(bestseller);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                PutDataIntoRecyclerView(bestsellerList);


            }




        }


        private void PutDataIntoRecyclerView(List<BookList> bestsellerList){
            BookAdapter adapter = new BookAdapter(SearchBookTitle.this, bestsellerList);
            recyclerView.setLayoutManager(new LinearLayoutManager(SearchBookTitle.this));
            recyclerView.setAdapter(adapter);
        }



    }