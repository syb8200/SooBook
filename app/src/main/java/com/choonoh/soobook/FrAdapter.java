package com.choonoh.soobook;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FrAdapter extends RecyclerView.Adapter<FrAdapter.MyViewHolder> {

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser = firebaseAuth.getCurrentUser();

    private final Context mContext;
    private List<FrList> mData;
    String user_uid = currentUser.getUid();
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
        v = inflater.inflate(R.layout.activity_friend_list, parent, false);

        return new MyViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull @NotNull MyViewHolder holder, int position) {
        String fr_uid = mData.get(position).getUid();
        holder.nick.setText(mData.get(position).getNick());
        holder.state.setText(mData.get(position).getState());

        //리스트 아이템 클릭 시 데이터 전달
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user_UID = currentUser.getUid();

                String nick = holder.nick.getText().toString();
                Intent intent = new Intent(mContext, FriendLibrary.class);
                intent.putExtra("nick", nick);
                intent.putExtra("uid", fr_uid);

                mContext.startActivity(intent);
            }

        });
    }
    @Override
    public int getItemCount() {
        return mData.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView nick, state;
        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            nick = itemView.findViewById(R.id.fr_nick);
            state = itemView.findViewById(R.id.fr_state);

        }
    }

}

