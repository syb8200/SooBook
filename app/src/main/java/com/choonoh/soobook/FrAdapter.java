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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FrAdapter extends RecyclerView.Adapter<FrAdapter.MyViewHolder> {

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser = firebaseAuth.getCurrentUser();
    private final Context mContext;
    private List<FrList> mData;

    public FrAdapter(Context mContext, List<FrList> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }
    public void addItem(FrList item){
        mData.add(item);

    }

    @NonNull
    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        v = inflater.inflate(R.layout.fragment_friend, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyViewHolder holder, int position) {

        holder.nick.setText(mData.get(position).getNick());

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView nick;
        ImageView imageView;
        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            nick = itemView.findViewById(R.id.fr_nick);
            imageView = itemView.findViewById(R.id.fr_circleImageView);

        }
    }

}
