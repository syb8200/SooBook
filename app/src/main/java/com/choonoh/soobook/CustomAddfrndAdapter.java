package com.choonoh.soobook;
// 변경하기 전 거
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAddfrndAdapter extends RecyclerView.Adapter<CustomAddfrndAdapter.CustomViewHolder> {

    private ArrayList<User> arrayList;
    private Context context;


    public CustomAddfrndAdapter(ArrayList<User> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_user, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        holder.tv_email.setText(arrayList.get(position).getEmail());
        holder.tv_uid.setText(arrayList.get(position).getUid());


    }

    @Override
    public int getItemCount() {
        // 삼항 연산자
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        TextView tv_email;
        TextView tv_uid;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tv_email = itemView.findViewById(R.id.tv_email);
            this.tv_uid = itemView.findViewById(R.id.tv_uid);
        }
    }
}
