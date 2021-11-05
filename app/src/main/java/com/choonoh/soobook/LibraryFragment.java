package com.choonoh.soobook;

import android.app.LauncherActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LibraryFragment extends Fragment {
    String book_img, book_isbn, book_title;
    TextView add_btn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_library, container, false);

        String user_email = getArguments().getString("user_email");
        String user_UID = getArguments().getString("user_UID");

        add_btn = root.findViewById(R.id.add_btn);

        add_btn.setOnClickListener(v->{
            Intent intent = new Intent(getContext(), SearchBook.class);
            intent.putExtra("user_email", user_email);
            intent.putExtra("user_UID", user_UID);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });


        GridView gridView = root.findViewById(R.id.reading_gridview);
        GridListAdapter adapter = new GridListAdapter();



        FirebaseDatabase database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동
        DatabaseReference databaseReference = database.getReference("Mylib/"+user_UID+"/"); // DB 테이블 연결

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    MylibList mylibList = snapshot.getValue(MylibList.class);
                    book_img = mylibList.getImg();
                    book_isbn = mylibList.getisbn();
                    book_title = mylibList.getTitle();

                    adapter.addItem(mylibList);
                }
                gridView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Mylib", String.valueOf(databaseError.toException())); // 에러문 출력
            }
        });

        GridView gridView1 = root.findViewById(R.id.read_gridview);
        GridListAdapter adapter1 = new GridListAdapter();


        FirebaseDatabase database2 = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동
        DatabaseReference databaseReference1 = database2.getReference("Oldlib/"+user_UID+"/"); // DB 테이블 연결

        databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    MylibList oldlibList = snapshot.getValue(MylibList.class);
                    book_img = oldlibList.getImg();
                    book_isbn = oldlibList.getisbn();
                    book_title = oldlibList.getTitle();

                    adapter1.addItem(oldlibList);
                }
                gridView1.setAdapter(adapter1);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Oldlib", String.valueOf(databaseError.toException())); // 에러문 출력
            }
        });
        return root;
    }
}
