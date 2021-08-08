package com.choonoh.soobook;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

public class BestSellerFragment extends Fragment implements View.OnClickListener {

    Best_Year_Fragment best_year_fragment;
    Best_Month_Fragment best_month_fragment;
    Best_Week_Fragment best_week_fragment;


    Button best_year, best_month, best_week;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup child_container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_bestseller, child_container, false);

        best_year = rootView.findViewById(R.id.best_year);
        best_month = rootView.findViewById(R.id.best_month);
        best_week = rootView.findViewById(R.id.best_week);

        best_year.setOnClickListener(this);
        best_month.setOnClickListener(this);
        best_week.setOnClickListener(this);

        best_year_fragment = new Best_Year_Fragment();
        best_month_fragment = new Best_Month_Fragment();
        best_week_fragment = new Best_Week_Fragment();


        getFragmentManager().beginTransaction().replace(R.id.child_child_container, best_year_fragment).commit();

        return rootView;
    }

    @Override
    public void onClick(View v){

        switch (v.getId()){

            case R.id.best_year:
                best_year.setBackgroundColor(Color.parseColor("#FF5F68"));
                best_month.setBackgroundColor(Color.parseColor("#C4C4C4"));
                best_week.setBackgroundColor(Color.parseColor("#C4C4C4"));

                getFragmentManager().beginTransaction().replace(R.id.child_child_container, best_year_fragment).commit();
                break;

            case R.id.best_month:
                best_year.setBackgroundColor(Color.parseColor("#C4C4C4"));
                best_month.setBackgroundColor(Color.parseColor("#FF5F68"));
                best_week.setBackgroundColor(Color.parseColor("#C4C4C4"));

                getFragmentManager().beginTransaction().replace(R.id.child_child_container, best_month_fragment).commit();
                break;

            case R.id.best_week:
                best_year.setBackgroundColor(Color.parseColor("#C4C4C4"));
                best_month.setBackgroundColor(Color.parseColor("#C4C4C4"));
                best_week.setBackgroundColor(Color.parseColor("#FF5F68"));

                getFragmentManager().beginTransaction().replace(R.id.child_child_container, best_week_fragment).commit();
                break;
        }

    }

}
