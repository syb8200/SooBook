package com.choonoh.soobook;

import android.app.LauncherActivity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class FriendFragment extends Fragment {
    String nick;
    static ArrayList<String> arrayIndex = new ArrayList<String>();
    List<FrList> frList;
    RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    ImageView fr_iv;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_friend, container, false);
        List<FrList> frList;
        String user_email = getArguments().getString("user_email");
        String user_UID = getArguments().getString("user_UID");

        frList = new ArrayList<>();
        recyclerView = root.findViewById(R.id.fr_recyclerView);


        FrAdapter adapter = new FrAdapter(getContext(),frList);

        FirebaseDatabase database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동
        DatabaseReference databaseReference = database.getReference("Friend/"+user_UID+"/"); // DB 테이블 연결

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                                             @Override
                                                             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                                 for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                                     FrList mylibList = snapshot.getValue(FrList.class);
                                                                     nick = mylibList.getNick();
                                                                     adapter.addItem(mylibList);
                                                                 }

                                                                 mLayoutManager = new LinearLayoutManager(getContext());
                                                                 recyclerView.setLayoutManager(mLayoutManager);

                                                                 recyclerView.setAdapter(adapter);
                                                             }@Override
            public void onCancelled (@NonNull DatabaseError databaseError){
                Log.e("FrFragment", String.valueOf(databaseError.toException())); // 에러문 출력
            }


            MySwipeHelper swipeHelper= new MySwipeHelper(getContext(), recyclerView,200) {
                @Override
                public void instantiatrMyButton(RecyclerView.ViewHolder viewHolder, List<MyButton> buffer) {
                    buffer.add(new MyButton(getContext(),
                            "삭제",
                            20,
                            R.drawable.ic_baseline_delete_24,
                            Color.parseColor("#FF3C30"),
                            pos -> {
                                Toast.makeText(getContext(), "삭제되었습니다.", Toast.LENGTH_SHORT).show();
                                Log.e("TAG", viewHolder.getAdapterPosition() + "");
                                String fr_uid = frList.get(viewHolder.getAdapterPosition()).getUid();

                                FirebaseDatabase database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동
                                DatabaseReference data = database.getReference("Friend/" + user_UID + "/" + fr_uid);
                                data.removeValue();

                                frList.remove(viewHolder.getAdapterPosition());                // 해당 항목 삭제
                                adapter.notifyItemRemoved(viewHolder.getAdapterPosition());    // Adapter에 알려주기.
                            }));
                }
            }
                    ;

                  /*


            }*/
                    // swipeHelper


                });      return root;}}

