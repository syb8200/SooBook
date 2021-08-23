package com.choonoh.soobook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MylibAdapter extends RecyclerView.Adapter<MylibAdapter.MyViewHolder> {

    private Context mContext;
    private List<MylibList> mData;

    public MylibAdapter(Context mContext, List<MylibList> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        v = inflater.inflate(R.layout.activity_mylib_list, parent, false);

        return new MyViewHolder(v);
    }




    @Override
    public void onBindViewHolder(@NonNull @NotNull MyViewHolder holder, int position) {

        Glide.with(mContext)
                .load(mData.get(position).getCoverSmallUrl())
                .into(holder.coverSmallUrl);

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView coverSmallUrl;

        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            coverSmallUrl = itemView.findViewById(R.id.coverSmallUrl);

        }
    }

}
