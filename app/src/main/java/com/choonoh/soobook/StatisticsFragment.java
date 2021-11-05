package com.choonoh.soobook;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class StatisticsFragment extends Fragment {
    String user_email, user_UID, totalBookNum, totalReadBookNum;
    BarChart barChart;
    PieChart pieChart;
    String mon, tue, wed, thu, fri, sat, sun;
    BarDataSet bardataset;
    BarData barData;
    Button target_books_btn;
    ProgressBar progressBar;
    TextView no_target_books_txt, target_books_txt, read_books_txt,  edit_target;
    int MylibNum = 0, OldlibNum = 0, i;

    ArrayList<BarEntry> barArrList;
    ArrayList<String> barLabels;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_statistics, container, false);
        Bundle bundle = getArguments();
        user_email = bundle.getString("user_email");
        user_UID = bundle.getString("user_UID");
        Log.e("stati_Frag uid",user_UID);

        barChart = root.findViewById(R.id.barChart);
        pieChart = root.findViewById(R.id.piechart);
        progressBar = root.findViewById(R.id.progressBar);
        read_books_txt = root.findViewById(R.id.read_books_txt);
        target_books_btn = root.findViewById(R.id.target_books_btn);
        target_books_txt = root.findViewById(R.id.target_books_txt);
        no_target_books_txt = root.findViewById(R.id.no_target_books_txt);
        edit_target = root.findViewById(R.id. edit_target);

        DatabaseReference mPostReference = database.getReference("ReadTime/" + user_UID + "/info/");
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i = 0;
                String month = "", readBookNum = "", targetBookNum = "";
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if(i == 0)
                        month = snapshot.getValue().toString();
                    else if(i == 1)
                        readBookNum = snapshot.getValue().toString();
                    else
                        targetBookNum = snapshot.getValue().toString();
                    i++;
                }
                SimpleDateFormat present_MM = new SimpleDateFormat("MM");
                Date endTimeDate = new Date(System.currentTimeMillis());
                String present_month = present_MM.format(endTimeDate);
                if(month == "") {
                    Log.e("null입니다 : ", month + ", " + readBookNum + ", " + targetBookNum);

                    target_books_btn.setVisibility(View.VISIBLE);
                    no_target_books_txt.setVisibility(View.VISIBLE);
                } else if(Integer.parseInt(month) != Integer.parseInt(present_month)) {
                    Log.e("달이 바뀌었습니다 : ", month + ", " + readBookNum + ", " + targetBookNum);

                    target_books_btn.setVisibility(View.VISIBLE);
                    no_target_books_txt.setVisibility(View.VISIBLE);
                } else{
                    Log.e("목표가 있군!! : ", month + ", " + readBookNum + ", " + targetBookNum);
                    read_books_txt.setText(readBookNum);
                    target_books_txt.setText(targetBookNum);
                    progressBar.setProgress(Integer.parseInt(readBookNum));
                    progressBar.setMax(Integer.parseInt(targetBookNum));

                    //progressBar.setBackgroundColor(Color.parseColor("#FF5F68"));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.e("StatisticsFragment", "loadPost:onCancelled", databaseError.toException());
            }
        };
        mPostReference.addValueEventListener(postListener);

        DatabaseReference databaseReference2 = database.getReference("Mylib/" + user_UID + "/");
        databaseReference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    MylibNum++;
                }
                Log.e("MylibNum child 갯수", String.valueOf(MylibNum)); // 에러문 출력
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("StaticFragment", String.valueOf(databaseError.toException())); // 에러문 출력
            }
        });

        DatabaseReference databaseReference3 = database.getReference("Oldlib/" + user_UID + "/");
        databaseReference3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // MylibList mylibList = snapshot.getValue(MylibList.class);
                    OldlibNum++;
                }
                Log.e("OldlibNum child 갯수", String.valueOf(OldlibNum)); // 에러문 출력
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("StaticFragment", String.valueOf(databaseError.toException())); // 에러문 출력
            }
        });


        DatabaseReference mPostReference4 = FirebaseDatabase.getInstance().getReference("ReadTime/info/"+user_UID);
        ValueEventListener postListener4 = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if(i == 7){
                        totalBookNum = snapshot.getValue().toString();
                    }
                    else if(i == 8) {
                        totalReadBookNum = snapshot.getValue().toString();
                        //initializing data
                        if(!totalBookNum.equals("0") && !totalReadBookNum.equals(0)) {
                            showPieChart(totalBookNum, totalReadBookNum);
                        } else{
                            pieChart.setVisibility(View.INVISIBLE);
                        }
                    }
                    i++;
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.e("StatisticsFragment", "loadPost:onCancelled", databaseError.toException());
            }
        };
        mPostReference4.addValueEventListener(postListener4);

        showBarChart();
        initBarChart();
        target_books_btn.setOnClickListener(v -> {
            EditText et = new EditText(getContext());
            FrameLayout dialogConatainer = new FrameLayout(getContext());
            FrameLayout.LayoutParams params = new  FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            params.leftMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
            params.rightMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);

            et.setLayoutParams(params);
            dialogConatainer.addView(et);

            AlertDialog.Builder alt_bld = new AlertDialog.Builder(getContext(),R.style.MyAlertDialogStyle);
            alt_bld.setTitle("목표 독서량 설정").setMessage("목표 독서량을 입력해주세요.(숫자)").setCancelable(
                    false).setView(dialogConatainer).setPositiveButton("확인",
                    (dialog, id) -> {
                        String readBookNum = "0";
                        String targetBookNum = et.getText().toString();
                        SimpleDateFormat MM = new SimpleDateFormat("MM");
                        Date endTimeDate = new Date(System.currentTimeMillis());
                        String month = MM.format(endTimeDate);
                        Map<String, Object> childUpdates = new HashMap<>();
                        Map<String, Object> postValues = null;

                        //String uid, String readTime , String startTime, String endTime, String date
                        FirebaseTaregetBookNumPost post = new FirebaseTaregetBookNumPost(targetBookNum, readBookNum, month);
                        DatabaseReference databaseReference = database.getReference();
                        postValues = post.toMap();
                        String dbroot ="/ReadTime/" + user_UID + "/info/";
                        childUpdates.put(dbroot, postValues);
                        databaseReference.updateChildren(childUpdates);

                        no_target_books_txt.setVisibility(View.GONE);
                        target_books_btn.setVisibility(View.GONE);

                        target_books_txt.setText(targetBookNum);

                        progressBar.setMax(Integer.parseInt(targetBookNum));
                        progressBar.setProgress(0);
                    });
            AlertDialog alert = alt_bld.create();
            alert.show();
        });


        edit_target.setOnClickListener(v -> {
            EditText et = new EditText(getContext());
            FrameLayout dialogConatainer = new FrameLayout(getContext());
            FrameLayout.LayoutParams params = new  FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            params.leftMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
            params.rightMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);

            et.setLayoutParams(params);
            dialogConatainer.addView(et);

            AlertDialog.Builder alt_bld = new AlertDialog.Builder(getContext(),R.style.MyAlertDialogStyle);
            alt_bld.setTitle("목표 독서량 변경").setMessage("목표 독서량을 입력해주세요.(숫자)").setCancelable(
                    false).setView(dialogConatainer).setPositiveButton("확인",
                    (dialog, id) -> {

                        String targetBookNum = et.getText().toString();
                        SimpleDateFormat MM = new SimpleDateFormat("MM");
                        Date endTimeDate = new Date(System.currentTimeMillis());
                        String month = MM.format(endTimeDate);
                        Map<String, Object> childUpdates = new HashMap<>();
                        Map<String, Object> postValues = null;
                        FirebaseDatabase database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동
                        DatabaseReference databaseReference = database.getReference("/ReadTime/" + user_UID + "/info/targetBookNum"); // DB 테이블 연결FirebaseDatabase.getInstance().getReference().addListenerForSingleValueEvent(new ValueEventListener() {

                        databaseReference.setValue(targetBookNum);

                //        progressBar.setMax(Integer.parseInt(targetBookNum));
                    });
            AlertDialog alert = alt_bld.create();
            alert.show();
        });


/*
        barArrList = new ArrayList<>();
        barArrList.add(new BarEntry(8f, 0));
        barArrList.add(new BarEntry(2f, 1));
        barArrList.add(new BarEntry(5f, 2));
        barArrList.add(new BarEntry(20f, 3));
        barArrList.add(new BarEntry(15f, 4));
        barArrList.add(new BarEntry(19f, 5));

        bardataset = new BarDataSet(barArrList, "Cells");

        barLabels = new ArrayList<>();
        barLabels.add("2016");
        barLabels.add("2015");
        barLabels.add("2014");
        barLabels.add("2013");
        barLabels.add("2012");
        barLabels.add("2011");

        barData = new BarData(barLabels, bardataset);
        barChart.setData(barData); // set the data and list of labels into chart
        barChart.setDescription("Set Bar Chart Description Here");  // set the description
        bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
        barChart.animateY(1500);
        */
/*
        ArrayList<Double> valueList = new ArrayList<>();
        ArrayList<BarEntry> entries = new ArrayList<>();
        String title = "Title";

        //input data
        for(int i = 1; i < 6; i++){
            valueList.add(i * 100.1);
        }

        //fit the data into a bar
        for (int i = 1; i < valueList.size(); i++) {
            BarEntry barEntry = new BarEntry(i, valueList.get(i).floatValue());
            entries.add(barEntry);
        }

        BarDataSet barDataSet = new BarDataSet(entries, title);

        BarData data = new BarData(barDataSet);
        barChart.setData(data);
        barChart.invalidate();


 */
        /*
        //hiding the grey background of the chart, default false if not set
        barChart.setDrawGridBackground(false);
        //remove the bar shadow, default false if not set
        barChart.setDrawBarShadow(false);
        //remove border of the chart, default false if not set
        barChart.setDrawBorders(false);
        barChart.setDescription(null);
/*
        //remove the description label text located at the lower right corner
        Description description = new Description();
        description.setEnabled(false);
        barChart.setDescription(description);

 */;
/*
        XAxis xAxis = barChart.getXAxis();
        //change the position of x-axis to the bottom
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);*/
        //set the horizontal distance of the grid line
/*
        xAxis.setGranularity(1f);

 *//*
        //hiding the x-axis line, default true if not set
        xAxis.setDrawAxisLine(false);
        //hiding the vertical grid lines, default true if not set
        xAxis.setDrawGridLines(false);

        YAxis leftAxis = barChart.getAxisLeft();
        //hiding the left y-axis line, default true if not set
        leftAxis.setDrawAxisLine(false);

        YAxis rightAxis = barChart.getAxisRight();
        //hiding the right y-axis line, default true if not set
        rightAxis.setDrawAxisLine(false);

        Legend legend = barChart.getLegend();
        //setting the shape of the legend form to line, default square shape
        legend.setForm(Legend.LegendForm.LINE);
        //setting the text size of the legend
        legend.setTextSize(11f);*/
        //setting the alignment of legend toward the chart
/*
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        //setting the stacking direction of legend
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        //setting the location of legend outside the chart, default false if not set
        legend.setDrawInside(false);
*/
        return root;
    }
    private void showPieChart(String ttBookNum, String ttReadBookNum){
        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        String label = "type";

        Map<String, Integer> typeAmountMap = new HashMap<>();
        double all = Integer.parseInt(totalBookNum);
        double read = Integer.parseInt(totalReadBookNum);
        int readResult = 0;
        // System.out.println(read*100.0/all);
        readResult = (int) (read*100.0/all);


        typeAmountMap.put("완독", (int) (read*100.0/all));
        typeAmountMap.put("미완독", 100-(int)(read/all*100.0));

        //initializing colors for the entries
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#B9BABE"));
        colors.add(Color.parseColor("#FF5F68"));

        //input data and fit data into pie chart entry
        for(String type: typeAmountMap.keySet()){
            pieEntries.add(new PieEntry(typeAmountMap.get(type).floatValue(), type));
        }

        //collecting the entries with label name
        PieDataSet pieDataSet = new PieDataSet(pieEntries,label);
        //setting text size of the value
        pieDataSet.setValueTextSize(12f);
        //providing color list for coloring different entries
        pieDataSet.setColors(colors);
        //grouping the data set from entry to chart
        PieData pieData = new PieData(pieDataSet);
        //showing the value of the entries, default true if not set
        pieData.setDrawValues(true);

        pieChart.setData(pieData);
        pieChart.invalidate();
        pieData.setValueFormatter(new PercentFormatter());
        pieChart.animateY(1500);
        pieChart.getDescription().setEnabled(false);
        pieChart.getLegend().setEnabled(false);
    }
    private void showBarChart(){
        ArrayList<Double> valueList = new ArrayList<Double>();
        ArrayList<BarEntry> entries = new ArrayList<>();
        String title = "Title";



        DatabaseReference mPostReference5 = FirebaseDatabase.getInstance().getReference("ReadTime/info/"+user_UID);
        ValueEventListener postListener5 = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if(i == 1){
                        fri = snapshot.getValue().toString();
                    } else if(i == 2){
                        mon = snapshot.getValue().toString();
                    } else if(i == 4){
                        sat = snapshot.getValue().toString();
                    } else if(i == 5){
                        sun = snapshot.getValue().toString();
                    } else if(i == 6){
                        thu = snapshot.getValue().toString();
                    } else if(i == 9){
                        tue = snapshot.getValue().toString();
                    } else if(i == 10){
                        wed = snapshot.getValue().toString();
                        Log.e("bar char 요일", mon + ", " + tue + ", " + wed + ", " + thu + ", " + fri + ", " +
                                sat + ", " + sun + ", ");
                    }
                    i++;
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.e("StatisticsFragment", "loadPost:onCancelled", databaseError.toException());
            }
        };
        mPostReference5.addValueEventListener(postListener5);


        //input data
        for(int i = 0; i < 5; i++){
            switch (i) {
                case 0:
                    valueList.add(60.0);
                    break;
                case 1:
                    valueList.add(120.0);
                    break;
                case 2:
                    valueList.add(90.0);
                    break;
                case 3:
                    valueList.add(150.0);
                    break;
                case 4:
                    valueList.add(30.0);
                    break;
            }

        }

        //fit the data into a bar
        for (int i = 0; i < valueList.size(); i++) {
            BarEntry barEntry = new BarEntry(i, valueList.get(i).floatValue());
            entries.add(barEntry);
        }

        BarDataSet barDataSet = new BarDataSet(entries, title);

        BarData data = new BarData(barDataSet);
        barChart.setData(data);
        barChart.invalidate();

        barDataSet.setColors(ContextCompat.getColor(getContext(), R.color.sb_main_2), ContextCompat.getColor(getContext(),
                R.color.sb_main), ContextCompat.getColor(getContext(), R.color.sb_main_3), ContextCompat.getColor(getContext(), R.color.sb_main_5),
                ContextCompat.getColor(getContext(), R.color.sb_main_1));
        //barDataSet.setColor(ContextCompat.getColor(getContext(), R.color.sb_main));
        initBarDataSet(barDataSet);
        barChart.setTouchEnabled(false);
    }

    private void initBarDataSet(BarDataSet barDataSet){
        //Changing the color of the bar
        //barDataSet.setColor(Color.parseColor("#304567"));
        //Setting the size of the form in the legend
        barDataSet.setFormSize(15f);
        //showing the value of the bar, default true if not set
        barDataSet.setDrawValues(false);
        //setting the text size of the value of the bar
        barDataSet.setValueTextSize(12f);
    }

    private void initBarChart(){
        //hiding the grey background of the chart, default false if not set
        barChart.setDrawGridBackground(false);
        //remove the bar shadow, default false if not set
        barChart.setDrawBarShadow(false);
        //remove border of the chart, default false if not set
        barChart.setDrawBorders(false);

        //remove the description label text located at the lower right corner
        Description description = new Description();
        description.setEnabled(false);
        barChart.setDescription(description);

        //setting animation for y-axis, the bar will pop up from 0 to its value within the time we set
        barChart.animateY(1500);
        //setting animation for x-axis, the bar will pop up separately within the time we set
/*
        XAxis xAxis = barChart.getXAxis();

*/
        final ArrayList<String> xAxisLabel = new ArrayList<>();
        xAxisLabel.add("Mon");
        xAxisLabel.add("Tue");
        xAxisLabel.add("Wed");
        xAxisLabel.add("Thu");
        xAxisLabel.add("Fri");

        XAxis xAxis = barChart.getXAxis();
        //change the position of x-axis to the bottom
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //set the horizontal distance of the grid line
        xAxis.setGranularity(1f);
        //hiding the x-axis line, default true if not set
        xAxis.setDrawAxisLine(false);
        //hiding the vertical grid lines, default true if not set
        xAxis.setDrawGridLines(false);

        xAxis.setValueFormatter((value, axis) -> xAxisLabel.get((int) value));


        YAxis leftAxis = barChart.getAxisLeft();
        //hiding the left y-axis line, default true if not set
        leftAxis.setDrawAxisLine(false);

        YAxis rightAxis = barChart.getAxisRight();

        //hiding the right y-axis line, default true if not set
        rightAxis.setDrawAxisLine(false);
        rightAxis.setEnabled(false);
        barChart.getLegend().setEnabled(false);
/*
        Legend legend = barChart.getLegend();
        //setting the shape of the legend form to line, default square shape
        legend.setForm(Legend.LegendForm.LINE);
        //setting the text size of the legend
        legend.setTextSize(11f);
        //setting the alignment of legend toward the chart
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        //setting the stacking direction of legend
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        //setting the location of legend outside the chart, default false if not set
        legend.setDrawInside(false);
*/
    }
}