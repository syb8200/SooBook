package com.choonoh.soobook;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Document;
import org.w3c.dom.Text;

public class test extends AppCompatActivity {

    private AsyncTask<String, Void, Document> getXMLTask;

    TextView textview;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        textview = findViewById(R.id.textView1);

        getXMLTask = new GetXMLTask(test.this,textview).execute();
    }

}