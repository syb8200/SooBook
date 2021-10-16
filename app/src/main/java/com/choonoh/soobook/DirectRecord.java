package com.choonoh.soobook;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DirectRecord extends AppCompatActivity {

    TextView date_picker, start_time, end_time;
    ImageButton back_btn;
    Button select_read_book;

    Calendar myCalendar = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener myDatePicker = (view, year, month, dayOfMonth) -> {
        myCalendar.set(Calendar.YEAR, year);
        myCalendar.set(Calendar.MONTH, month);
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        updateLabel();
    };

        @Override
        protected void onCreate (Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_direct_record);

            //뒤로가기 버튼
            back_btn = findViewById(R.id.back_btn);
            back_btn.setOnClickListener(v -> {
                Intent intent=new Intent(DirectRecord.this, RecordFragment.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            });

            //날짜 선택 텍스트뷰
            date_picker = findViewById(R.id.date_picker);
            date_picker.setOnClickListener(v -> new DatePickerDialog(
                    DirectRecord.this, myDatePicker, myCalendar.get(Calendar.YEAR),
                    myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show());

            //시작 시간 선택 텍스트뷰
            start_time = findViewById(R.id.start_time);
            start_time.setOnClickListener(v -> {
                Calendar mstartTime = Calendar.getInstance();
                int hour = mstartTime.get(Calendar.HOUR_OF_DAY);
                int minute = mstartTime.get(Calendar.MINUTE);
                TimePickerDialog mTimPicker;
                mTimPicker = new TimePickerDialog(DirectRecord.this, (timePicker, selectedHour, selectedMinute) -> {
                    String state = "AM";
                    //선택한 시간이 12 넘을 경우 "PM"으로 변경 및 -12시간 하여 출력
                    if(selectedHour > 12){
                        selectedHour -= 12;
                        state = "PM";
                    }
                    //EditText에 출력할 형식 지정
                    start_time.setText(state+" "+ selectedHour + "시" + selectedMinute + "분");
                }, hour, minute, false);
                mTimPicker.setTitle("Select Time");
                mTimPicker.show();
            });

            //종료 시간 선택 텍스트뷰
            end_time = findViewById(R.id.end_time);
            end_time.setOnClickListener(v -> {
                Calendar mendTime = Calendar.getInstance();
                int hour = mendTime.get(Calendar.HOUR_OF_DAY);
                int minute = mendTime.get(Calendar.MINUTE);
                TimePickerDialog mTimPicker;
                mTimPicker = new TimePickerDialog(DirectRecord.this, (timePicker, selectedHour, selectedMinute) -> {
                    String state = "AM";
                    //선택한 시간이 12 넘을 경우 "PM"으로 변경 및 -12시간 하여 출력
                    if(selectedHour > 12){
                        selectedHour -= 12;
                        state = "PM";
                    }
                    //EditText에 출력할 형식 지정
                    end_time.setText(state + " " + selectedHour + "시" + selectedMinute + "분");
                }, hour, minute, false);
                mTimPicker.setTitle("Select Time");
                mTimPicker.show();
            });

            select_read_book = findViewById(R.id.select_read_book);
            select_read_book.setOnClickListener(v -> {
                Intent intent=new Intent(DirectRecord.this, SelectReadBook.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            });
        }

        private void updateLabel() {
            String myFormat = "yyyy//MM/dd";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);

            date_picker = findViewById(R.id.date_picker);
            date_picker.setText(sdf.format(myCalendar.getTime()));
        }
}
