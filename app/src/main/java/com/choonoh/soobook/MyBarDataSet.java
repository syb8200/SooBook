package com.choonoh.soobook;

import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.List;

public class MyBarDataSet extends BarDataSet {


    public MyBarDataSet(List<BarEntry> yVals, String label) {
        super(yVals, label);
    }

    @Override
    public int getColor(int index) {
        if(getEntryForIndex(index).getY() <= 30.0) // less than 95 green
            return mColors.get(0);
        else if(getEntryForIndex(index).getY() < 60.0) // less than 100 orange
            return mColors.get(1);
        else if(getEntryForIndex(index).getY() < 90.0) // less than 100 orange
            return mColors.get(2);
        else if(getEntryForIndex(index).getY() < 120.0) // less than 100 orange
            return mColors.get(3);
        else // greater or equal than 100 red
            return mColors.get(4);
    }

}