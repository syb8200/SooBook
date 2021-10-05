package com.choonoh.soobook;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class SpinnerAdapter extends ArrayAdapter<SpinnerItem>{

    public SpinnerAdapter(Context context, ArrayList<SpinnerItem> spinnerList){
        super(context, 0, spinnerList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable @org.jetbrains.annotations.Nullable View convertView, @NonNull @NotNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent){
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(
              R.layout.activity_stars_spinner, parent, false
            );
        }

        ImageView imageViewStars = convertView.findViewById(R.id.imageView);

        SpinnerItem currentItem = getItem(position);

        if(currentItem != null){
            imageViewStars.setImageResource(currentItem.getmStarsImage());
        }

        return convertView;
    }
}


