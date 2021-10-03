package com.choonoh.soobook;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRadioButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WriteMemo extends AppCompatActivity {
    AppCompatRadioButton radio_left, radio_right;

    private ArrayList<SpinnerItem> mSpinnerList;
    private SpinnerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_memo);


        //다 읽음, 읽는 중 라디오 버튼
        radio_left = findViewById(R.id.radio_left);
        radio_right = findViewById(R.id.radio_right);

        initList();

        Spinner stars_spinner = findViewById(R.id.stars_spinner);
        mAdapter = new SpinnerAdapter(this, mSpinnerList);
        stars_spinner.setAdapter(mAdapter);


        //스피너 리스트 아이템 클릭했을 때
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


    //다 읽음, 읽는 중 라디오 버튼 클릭 시
    public void onRadioButtonClicked(View view){
        boolean isSelected = ((AppCompatRadioButton)view).isChecked();
        switch(view.getId()){
            case R.id.radio_left:
                if(isSelected){
                    radio_left.setTextColor(Color.parseColor("#FFFFFF"));
                    radio_right.setTextColor(Color.parseColor("#FF5F68"));
                }
                break;
            case R.id.radio_right:
                if(isSelected){
                    radio_right.setTextColor(Color.parseColor("#FFFFFF"));
                    radio_left.setTextColor(Color.parseColor("#FF5F68"));

                }
                break;
        }
    }


    //별점1~5 이미지 리스트에 추가
    private void initList(){
        mSpinnerList = new ArrayList<>();
        mSpinnerList.add(new SpinnerItem(R.drawable.stars_5));
        mSpinnerList.add(new SpinnerItem(R.drawable.stars_4));
        mSpinnerList.add(new SpinnerItem(R.drawable.stars_3));
        mSpinnerList.add(new SpinnerItem(R.drawable.stars_2));
        mSpinnerList.add(new SpinnerItem(R.drawable.stars_1));
    }

}
