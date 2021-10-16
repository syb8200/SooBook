package com.choonoh.soobook;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

public class HomeCategoryFragment extends Fragment implements View.OnClickListener {

    BestsellerFragment bestseller_fragment;
    NewFragment new_fragment;
    RecommendFragment recommend_fragment;

    Button category_best, category_recommend, category_new;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup child_container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_home_category, child_container, false);

        category_best = rootView.findViewById(R.id.category_best);
        category_recommend = rootView.findViewById(R.id.category_recommend);
        category_new = rootView.findViewById(R.id.category_new);

        category_best.setOnClickListener(this);
        category_recommend.setOnClickListener(this);
        category_new.setOnClickListener(this);

        bestseller_fragment = new BestsellerFragment();
        new_fragment = new NewFragment();
        recommend_fragment = new RecommendFragment();

        getFragmentManager().beginTransaction().replace(R.id.child_child_container, bestseller_fragment).commit();
        return rootView;
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.category_best:
                category_best.setBackgroundColor(Color.parseColor("#FF5F68"));
                category_recommend.setBackgroundColor(Color.parseColor("#C4C4C4"));
                category_new.setBackgroundColor(Color.parseColor("#C4C4C4"));

                getFragmentManager().beginTransaction().replace(R.id.child_child_container, bestseller_fragment).commit();
                break;

            case R.id.category_recommend:
                category_best.setBackgroundColor(Color.parseColor("#C4C4C4"));
                category_recommend.setBackgroundColor(Color.parseColor("#FF5F68"));
                category_new.setBackgroundColor(Color.parseColor("#C4C4C4"));

                getFragmentManager().beginTransaction().replace(R.id.child_child_container, recommend_fragment).commit();
                break;

            case R.id.category_new:
                category_best.setBackgroundColor(Color.parseColor("#C4C4C4"));
                category_recommend.setBackgroundColor(Color.parseColor("#C4C4C4"));
                category_new.setBackgroundColor(Color.parseColor("#FF5F68"));

                getFragmentManager().beginTransaction().replace(R.id.child_child_container, new_fragment).commit();
                break;
        }
    }
}
