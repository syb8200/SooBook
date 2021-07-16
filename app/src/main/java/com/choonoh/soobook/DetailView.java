package com.choonoh.soobook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import com.choonoh.soobook.R;

public class DetailView extends AppCompatActivity {

    TextView tv_name, tv_type, tv_close_day, tv_weekOpenTime, tv_weekCloseTime, tv_satOpenTime, tv_satCloseTime, tv_holidayOpenTime, tv_holidayCloseTime;
    TextView tv_bookNum, tv_borPosNum, tv_borPosDay, tv_address, tv_number, tv_homePageUrl, tv_dataTime;

    String name, type, close_day, weekOpenTime, weekCloseTime, satOpenTime, satCloseTime, holidayOpenTime, holidayCloseTime;
    String bookNum, bookPosNum, borPosDay, address, number, homePageUrl, dataTime;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view);

        setDetailView();
        getIntentString();
        setTextViews();
    }
    public void setDetailView() {
        tv_name = findViewById(R.id.name);
        tv_type = findViewById(R.id.type);
        tv_close_day = findViewById(R.id.closeDay);
        tv_weekOpenTime = findViewById(R.id.weekOpenTime);
        tv_weekCloseTime = findViewById(R.id.weekCloseTime);

        tv_satOpenTime = findViewById(R.id.satOpenTime);
        tv_satCloseTime = findViewById(R.id.satCloseTime);
        tv_holidayOpenTime = findViewById(R.id.holidayOpenTime);
        tv_holidayCloseTime = findViewById(R.id.holidayCloseTime);

        tv_bookNum = findViewById(R.id.bookNum);
        tv_borPosNum = findViewById(R.id.borPosNum);
        tv_borPosDay = findViewById(R.id.borPosDay);
        tv_address = findViewById(R.id.address);

        tv_number = findViewById(R.id.number);
        tv_homePageUrl = findViewById(R.id.homePageUrl);
        tv_dataTime = findViewById(R.id.dataTime);


        tv_number.setOnClickListener(v -> {

            String tel = tv_number.getText().toString();

            Uri dialing = Uri.parse("tel:" + tel);
            Intent dialingIntent = new Intent(Intent.ACTION_DIAL, dialing);
         startActivity(dialingIntent);


        });
        tv_homePageUrl.setOnClickListener(v -> {
                String url = tv_homePageUrl.getText().toString();
                Uri uri = Uri.parse(url);   // "http:"을 알아서 분석해서 웹으로 인식.
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);

            });

    }
    public void getIntentString() {
        name = getIntent().getStringExtra("name");
        type = getIntent().getStringExtra("type");
        close_day = getIntent().getStringExtra("close_day");
        weekOpenTime = getIntent().getStringExtra("weekOpenTime");
        weekCloseTime = getIntent().getStringExtra("weekCloseTime");

        satOpenTime = getIntent().getStringExtra("satOpenTime");
        satCloseTime = getIntent().getStringExtra("satCloseTime");
        holidayOpenTime = getIntent().getStringExtra("holidayOpenTime");
        holidayCloseTime = getIntent().getStringExtra("holidayCloseTime");

        bookNum = getIntent().getStringExtra("bookNum");
        bookPosNum = getIntent().getStringExtra("bookPosNum");
        borPosDay = getIntent().getStringExtra("borPosDay");
        address = getIntent().getStringExtra("address");

        number = getIntent().getStringExtra("number");
        homePageUrl = getIntent().getStringExtra("homePageUrl");
        dataTime = getIntent().getStringExtra("dataTime");
    }
    public void setTextViews() {
        tv_name.setText(name);
        tv_type.setText(type);
        tv_close_day.setText(close_day);
        tv_weekOpenTime.setText(weekOpenTime);
        tv_weekCloseTime.setText(weekCloseTime);

        tv_satOpenTime.setText(satOpenTime);
        tv_satCloseTime.setText(satCloseTime);
        tv_holidayOpenTime.setText(holidayOpenTime);
        tv_holidayCloseTime.setText(holidayCloseTime);

        tv_bookNum.setText(bookNum);
        tv_borPosNum.setText(bookPosNum);
        tv_borPosDay.setText(borPosDay);
        tv_address.setText(address);

        tv_number.setText(number);
        tv_homePageUrl.setText(homePageUrl);
        tv_dataTime.setText(dataTime);
    }
}