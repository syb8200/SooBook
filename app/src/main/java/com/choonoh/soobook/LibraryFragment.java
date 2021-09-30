package com.choonoh.soobook;

import android.app.LauncherActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
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
    private DatabaseReference mPostReference;
    private TextView add_mylib;
    private Button del_frnd;
    private ArrayList<MylibList> arrayList;
    static ArrayList<String> arrayIndex = new ArrayList<String>();
    private AdapterView adapter;
    String book_img, book_isbn;
    private String user_email, user_UID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_library, container, false);

      //String user_email = getArguments().getString("user_email");
     //   String user_UID = getArguments().getString("user_UID");
       // String user_email = "aaa";
        //String user_UID = "A1adj9jHzrWPCvjmHiz1kaUfzZ33";

        GridView gridView = root.findViewById(R.id.reading_gridview);
        GridListAdapter adapter = new GridListAdapter();



      //  adapter.addItem(new MylibList("dddd"));
     //   arrayList = new ArrayList<MylibList>(); // User 객체를 담을 어레이 리스트 (어댑터쪽으로)
        FirebaseDatabase database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동

        DatabaseReference databaseReference = database.getReference("Mylib/A1adj9jHzrWPCvjmHiz1kaUfzZ33/"); // DB 테이블 연결
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
             //   arrayList.clear(); // 기존 배열리스트가 존재하지않게 초기화
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) { // 반복문으로 데이터 List를 추출해냄
                    MylibList mylibList = snapshot.getValue(MylibList.class); // 만들어뒀던 User 객체에 데이터를 담는다.
                    book_img = mylibList.getImg();
                    book_isbn = mylibList.getisbn();
               //     arrayList.add(mylibList); // 담은 데이터들을 배열리스트에 넣고 리사이클러뷰로 보낼 준비
                    adapter.addItem(mylibList);
                }
                //  adapter.notifyDataSetChanged(); // 리스트 저장 및 새로고침
                gridView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 디비를 가져오던중 에러 발생 시
                Log.e("Mylib", String.valueOf(databaseError.toException())); // 에러문 출력
            }
        });
        //        adapter = new MylibAdapter(arrayList, getContext());

        //   recyclerView.setAdapter(adapter); // 리사이클러뷰에 어댑터 연결



        return root;
    }
}
