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

import java.util.ArrayList;
import java.util.List;

public class BestsellerAdapter extends RecyclerView.Adapter<BestsellerAdapter.MyViewHolder> {

    private Context mContext;
    private List<BestsellerList> mData;

    public BestsellerAdapter(Context mContext, List<BestsellerList> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        v = inflater.inflate(R.layout.activity_bestseller_list, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyViewHolder holder, int position) {
        holder.rank.setText(mData.get(position).getRank());
        holder.title.setText(mData.get(position).getTitle());
        holder.author.setText(mData.get(position).getAuthor());
        holder.publisher.setText(mData.get(position).getPublisher());
        holder.customerReviewRank.setText(mData.get(position).getCustomerReviewRank());

        Glide.with(mContext)
                .load(mData.get(position).getCoverSmallUrl())
                .into(holder.coverSmallUrl);

        holder.itemView.setOnClickListener(v ->  {
            String rank= mData.get(position).getRank();
            String title = mData.get(position).getTitle();
            String author = mData.get(position).getAuthor();
            String publisher = mData.get(position).getPublisher();
            String customerReviewRank = mData.get(position).getCustomerReviewRank();
            String isbn = mData.get(position).getIsbn();
            String pubDate = mData.get(position).getPubDate();
            String description = mData.get(position).getDescription();
            String cover = mData.get(position).getCoverSmallUrl();

            Intent intent = new Intent(mContext,BookDetailActivity.class);
            intent.putExtra("rank",rank);
            intent.putExtra("title", title);
            intent.putExtra("author", author);
            intent.putExtra("publisher",publisher);
            intent.putExtra("customerReviewRank", customerReviewRank);
            intent.putExtra("isbn",isbn);
            intent.putExtra("pubDate", pubDate);
            intent.putExtra("description",description);
            intent.putExtra("coverSmallUrl", cover);

            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView rank;
        TextView title;
        TextView author;
        TextView publisher;
        TextView customerReviewRank;
        ImageView coverSmallUrl;

        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            rank = itemView.findViewById(R.id.rank);
            title = itemView.findViewById(R.id.title);
            author = itemView.findViewById(R.id.author);
            publisher = itemView.findViewById(R.id.publisher);
            customerReviewRank = itemView.findViewById(R.id.customerReviewRank);
            coverSmallUrl = itemView.findViewById(R.id.coverSmallUrl);

        }
    }
}
