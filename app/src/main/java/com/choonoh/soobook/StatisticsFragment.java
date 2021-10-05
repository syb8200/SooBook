package com.choonoh.soobook;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.charts.LineChart; 


import java.util.ArrayList;

public class StatisticsFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_statistics, container, false);

        //선 그래프
        LineChart lineChart;

        ArrayList<Entry> entry_chart = new ArrayList<>();


        lineChart = root.findViewById(R.id.bar_chart);//layout의 id
        LineData chartData = new LineData();

        entry_chart.add(new Entry(2, 5));
    /* 만약 (2, 3) add하고 (2, 5)한다고해서
    기존 (2, 3)이 사라지는게 아니라 x가 2인곳에 y가 3, 5의 점이 찍힘 */

        LineDataSet lineDataSet = new LineDataSet(entry_chart, "꺽은선1");
        chartData.addDataSet(lineDataSet);

        lineChart.setData(chartData);

        lineChart.invalidate();

        return root;
    }
}