package com.choonoh.soobook;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CustomUserAdapter extends RecyclerView.Adapter<CustomUserAdapter.CustomViewHolder> {

    private ArrayList<User> arrayList;
    private Context context;

    public CustomUserAdapter(ArrayList<User> arrayList, Context context) {
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

        holder.itemView.setOnClickListener(v -> {

            String uid = arrayList.get(position).getUid(); //친구uid
            String user_uid =arrayList.get(position).getUser_uid(); //본인 uid
            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
            dialog.setTitle("친구 삭제")
                    .setMessage("이 친구를 친구목록에서 삭제하시겠습니까?")
                    .setPositiveButton("네", (dialog1, which) -> {
                        FirebaseDatabase database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동
                        DatabaseReference data = database.getReference("Friend/"+user_uid+"/"+uid);
                        data.removeValue();

                        Intent intent= new Intent(context, Home.class);
                        intent.putExtra("fragment","myPage");
                        intent.putExtra("user_UID",user_uid);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);
                        Toast.makeText(context, "친구를 삭제하였습니다", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(context, "친구 삭제를 취소했습니다.", Toast.LENGTH_SHORT).show();

                        }
                    })
                    .create()
                    .show();
        });
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