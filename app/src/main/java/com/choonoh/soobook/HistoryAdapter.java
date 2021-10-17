package com.choonoh.soobook;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser = firebaseAuth.getCurrentUser();
    private final Context mContext;
    private List<HistoryReviewList> mData;

    public HistoryAdapter(Context mContext, List<HistoryReviewList> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }
    public void addItem(HistoryReviewList item){
        mData.add(item);

    }

    @NonNull
    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        v = inflater.inflate(R.layout.activity_record_history_list, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyViewHolder holder, int position) {
        String user_UID = currentUser.getUid();

        holder.title.setText(mData.get(position).getTitle());
        holder.date.setText(mData.get(position).getLast());
/*
        //리스트 아이템 클릭 시 데이터 전달
        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String user_UID = currentUser.getUid();
                //String myuid;

                String nick = holder.title.getText().toString();
                String uid = holder.date.getText().toString();


                Intent intent = new Intent(mContext,FriendLibrary.class);
                intent.putExtra("nick", nick);
                intent.putExtra("uid", uid);

                mContext.startActivity(intent);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView date;

        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.histoli_title);
            date = itemView.findViewById(R.id.histoli_date);

        }
    }

}
