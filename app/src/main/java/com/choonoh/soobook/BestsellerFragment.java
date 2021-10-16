package com.choonoh.soobook;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

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

public class BestsellerFragment extends Fragment {

    private static String JSON_URL = "https://book.interpark.com/api/bestSeller.api?key=B91AE6F8D1E9702FB8D9CD1FC356A6E0F422AA40510994A9DC06E2196E716175&categoryId=100&output=json";

    List<BestsellerList> bestsellerList;
    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup child_child_container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_category_bestseller, child_child_container, false);

        bestsellerList = new ArrayList<>();
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

                    BestsellerList bestseller = new BestsellerList();
                    bestseller.setRank(jsonObject1.getString("rank"));
                    bestseller.setTitle(jsonObject1.getString("title"));
                    bestseller.setAuthor(jsonObject1.getString("author"));
                    bestseller.setPublisher(jsonObject1.getString("publisher"));
                    bestseller.setCustomerReviewRank(jsonObject1.getString("customerReviewRank"));
                    bestseller.setCoverSmallUrl(jsonObject1.getString("coverSmallUrl"));
                    bestseller.setIsbn(jsonObject1.getString("isbn"));
                    bestseller.setPubDate(jsonObject1.getString("pubDate"));
                    bestseller.setDescription(jsonObject1.getString("description"));

                    bestsellerList.add(bestseller);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            PutDataIntoRecyclerView(bestsellerList);
        }
    }

    private void PutDataIntoRecyclerView(List<BestsellerList> bestsellerList){
        BestsellerAdapter adapter = new BestsellerAdapter(getContext(), bestsellerList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }
}
