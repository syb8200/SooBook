package com.choonoh.soobook;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Mylib extends AppCompatActivity {

    private static String JSON_URL = "http://book.interpark.com/api/search.api?key=D10E38E11FF9AF9A94BBFCEA6E7C69EB862A51DD8A9A6F6F0141AA42540FEF41&query=%EB%A8%B8%EC%8B%A0%EB%9F%AC%EB%8B%9D&output=json";

    List<MylibList> mylibList;
    RecyclerView recyclerView;


    String user_email, user_UID;
    TextView btn_add;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mylib);
        mylibList = new ArrayList<>();
        recyclerView = findViewById(R.id.mylib_recycler_view);
        btn_add = findViewById(R.id.btn_add);
        user_email = getIntent().getStringExtra("user_email");
        user_UID = getIntent().getStringExtra("user_UID");

        btn_add.setOnClickListener(v -> {
            Intent intent = new Intent(this, QrReaderActivity.class);
            intent.putExtra("user_email", user_email);
            intent.putExtra("user_UID", user_UID);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });


        GetData getData = new GetData();
        getData.execute();

    }

    public class GetData extends AsyncTask<String, String, String>{

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

            try{
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("item");

                for(int i = 0; i<jsonArray.length(); i++){
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                    MylibList mylib = new MylibList();
                    mylib.setCoverSmallUrl(jsonObject1.getString("coverSmallUrl"));

                    mylibList.add(mylib);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            PutDataIntoRecyclerView(mylibList);


        }




    }


    private void PutDataIntoRecyclerView(List<MylibList> mylibList){
        MylibAdapter adapter = new MylibAdapter(Mylib.this, mylibList);
        recyclerView.setLayoutManager(new LinearLayoutManager(Mylib.this));
        recyclerView.setAdapter(adapter);
    }





}
