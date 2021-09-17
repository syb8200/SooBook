package com.choonoh.soobook;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRadioButton;

public class WriteMemo extends AppCompatActivity {
    AppCompatRadioButton radio_left, radio_right;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_memo);
        radio_left = findViewById(R.id.radio_left);
        radio_right = findViewById(R.id.radio_right);
    }

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
                    radio_right.setTextColor(Color.parseColor("#FF5F68"));
                    radio_left.setTextColor(Color.parseColor("#FFFFFF"));
                }
                break;
        }
    }
}
