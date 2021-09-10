package com.choonoh.soobook;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ProfileFragment extends Fragment implements View.OnClickListener{

    ImageButton settings,logout_btn;
    Button profile_edit;
    TextView statistics_tab, library_tab, friend_tab, nickname_tv;       //changepw_btn, logout_txt_btn, del_id_btn;
    View under_bar1, under_bar2, under_bar3;
    String nickname;
    StatisticsFragment statisticsFragment;
    LibraryFragment libraryFragment;
    FriendFragment friendFragment;

    private FirebaseAuth mAuth;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        String user_email = getArguments().getString("user_email");
        String user_UID = getArguments().getString("user_UID");

        //상단
        settings = root.findViewById(R.id.settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Settings.class);
                intent.putExtra("user_email", user_email);
                intent.putExtra("user_UID", user_UID);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        //프로필 수정 버튼
        profile_edit = root.findViewById(R.id.profile_edit);

        //탭 3개
        statistics_tab = root.findViewById(R.id.statistics_tab);
        library_tab = root.findViewById(R.id.library_tab);
        friend_tab = root.findViewById(R.id.friend_tab);
        under_bar1 = root.findViewById(R.id.under_bar1);
        under_bar2 = root.findViewById(R.id.under_bar2);
        under_bar3 = root.findViewById(R.id.under_bar3);

        statistics_tab.setOnClickListener(this);
        library_tab.setOnClickListener(this);
        friend_tab.setOnClickListener(this);

        statisticsFragment = new StatisticsFragment();
        libraryFragment = new LibraryFragment();
        friendFragment = new FriendFragment();

        getFragmentManager().beginTransaction().replace(R.id.child_container, statisticsFragment).commit();


        nickname_tv = root.findViewById(R.id.nickname_tv);

        FirebaseDatabase database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동

        DatabaseReference databaseReference = database.getReference("User/" + user_UID + "/nick"); // DB 테이블 연결FirebaseDatabase.getInstance().getReference().addListenerForSingleValueEvent(new ValueEventListener() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Object value = snapshot.getValue(Object.class);
                nickname_tv.setText(value.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    return root;

}

    @Override
    public void onClick(View v){

        switch (v.getId()){

            case R.id.statistics_tab:
                statistics_tab.setTextColor(Color.parseColor("#FF5F68"));
                library_tab.setTextColor(Color.parseColor("#B9BABE"));
                friend_tab.setTextColor(Color.parseColor("#B9BABE"));
                under_bar1.setVisibility(View.VISIBLE);
                under_bar2.setVisibility(View.GONE);
                under_bar3.setVisibility(View.GONE);

                getFragmentManager().beginTransaction().replace(R.id.child_container, statisticsFragment).commit();
                break;

            case R.id.library_tab:
                statistics_tab.setTextColor(Color.parseColor("#B9BABE"));
                library_tab.setTextColor(Color.parseColor("#FF5F68"));
                friend_tab.setTextColor(Color.parseColor("#B9BABE"));
                under_bar1.setVisibility(View.GONE);
                under_bar2.setVisibility(View.VISIBLE);
                under_bar3.setVisibility(View.GONE);

                getFragmentManager().beginTransaction().replace(R.id.child_container, libraryFragment).commit();
                break;

            case R.id.friend_tab:
                statistics_tab.setTextColor(Color.parseColor("#B9BABE"));
                library_tab.setTextColor(Color.parseColor("#B9BABE"));
                friend_tab.setTextColor(Color.parseColor("#FF5F68"));
                under_bar1.setVisibility(View.GONE);
                under_bar2.setVisibility(View.GONE);
                under_bar3.setVisibility(View.VISIBLE);

                getFragmentManager().beginTransaction().replace(R.id.child_container, friendFragment).commit();
                break;

        }

    }
}