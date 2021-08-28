package com.choonoh.soobook;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO : process click event.
                    int position = getAdapterPosition() ;
                    if (position != RecyclerView.NO_POSITION) {
                    Log.d("Click", "position = " + position);

                        String uid = arrayList.get(position).getuid(); //친구uid
                        String isbn = arrayList.get(position).getisbn();
                        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                        dialog.setTitle("삭제")
                                .setMessage("읽는 책 목록에서 삭제하시겠습니까?")
                                .setPositiveButton("네", (dialog1, which) -> {
                                    FirebaseDatabase database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동
                                    DatabaseReference data = database.getReference("Mylib/"+uid+"/"+isbn);
                                    data.removeValue();

                                    Intent intent= new Intent(context, Mylib.class);
                                    intent.putExtra("user_UID",uid);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    context.startActivity(intent);
                                    Toast.makeText(context, "읽는책을 삭제하였습니다", Toast.LENGTH_SHORT).show();
                                })
                                .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(context, "삭제를 취소했습니다.", Toast.LENGTH_SHORT).show();

                                    }
                                })
                                .create()
                                .show();

                }}
            });

            this.book_isbn = itemView.findViewById(R.id.mylib_isbn);
            book_img = itemView.findViewById(R.id.coverSmallUrl);
        }

    }

}
