package com.choonoh.soobook;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class NewFragment extends Fragment {

    private static String JSON_URL = "https://book.interpark.com/api/newBook.api?key=B91AE6F8D1E9702FB8D9CD1FC356A6E0F422AA40510994A9DC06E2196E716175&categoryId=100&output=json";

    List<NewList> newList;
    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup child_child_container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_category_new, child_child_container, false);

        newList = new ArrayList<>();
        recyclerView = rootView.findViewById(R.id.recycler_view);

        GetData getData = new GetData();
        getData.execute();

        return rootView;
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

                    NewList newb = new NewList();
                    newb.setTitle(jsonObject1.getString("title"));
                    newb.setAuthor(jsonObject1.getString("author"));
                    newb.setPublisher(jsonObject1.getString("publisher"));
                    newb.setCustomerReviewRank(jsonObject1.getString("customerReviewRank"));
                    newb.setCoverSmallUrl(jsonObject1.getString("coverSmallUrl"));

                    newList.add(newb);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            PutDataIntoRecyclerView(newList);


        }




    }


    private void PutDataIntoRecyclerView(List<NewList> newList){
        NewAdapter adapter = new NewAdapter(getContext(), newList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }





}
