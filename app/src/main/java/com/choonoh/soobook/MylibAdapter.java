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

import java.util.ArrayList;
import java.util.List;

public class MylibAdapter extends RecyclerView.Adapter<MylibAdapter.MyViewHolder> {


    private ArrayList<MylibList> arrayList;
    private Context context;

    public MylibAdapter(ArrayList<MylibList> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_mylib_list, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }





    @Override
    public void onBindViewHolder(@NonNull @NotNull MyViewHolder holder, int position) {

        Glide.with(context)
                .load(arrayList.get(position).getImg())
                .into(holder.book_img);
        holder.book_isbn.setText(arrayList.get(position).getisbn());
    }

    @Override
    public int getItemCount() {
        // 삼항 연산자
        return (arrayList != null ? arrayList.size() : 0);
    }
    public class MyViewHolder  extends RecyclerView.ViewHolder {

        TextView book_isbn;
        ImageView book_img;

        public MyViewHolder (@NonNull View itemView) {
            super(itemView);
            this.book_isbn = itemView.findViewById(R.id.mylib_isbn);
            book_img = itemView.findViewById(R.id.coverSmallUrl);
        }

    }

}
