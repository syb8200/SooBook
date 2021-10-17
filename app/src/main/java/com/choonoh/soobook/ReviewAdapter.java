package com.choonoh.soobook;

import android.content.Context;
import android.content.Intent;
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

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.MyViewHolder> {

    private Context mContext;
    private List<ReviewList> mData;

    public ReviewAdapter(Context mContext, List<ReviewList> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }
    public void addItem(ReviewList item){
        mData.add(item);

    }

    @NonNull
    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        v = inflater.inflate(R.layout.activity_review_list, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyViewHolder holder, int position) {
        holder.nick.setText(mData.get(position).getNick());
        holder.review.setText(mData.get(position).getReview());
        holder.star.setText(mData.get(position).getStar());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView nick;
        TextView review;
        TextView star;

        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            nick = itemView.findViewById(R.id.review_nick);
            review = itemView.findViewById(R.id.review_content);
            star = itemView.findViewById(R.id.tv_star);

        }
    }

}
