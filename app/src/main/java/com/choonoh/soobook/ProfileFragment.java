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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class ProfileFragment extends Fragment implements View.OnClickListener{

    ImageButton settings,logout_btn;
    Button profile_edit;
    TextView statistics_tab, library_tab, friend_tab;       //changepw_btn, logout_txt_btn, del_id_btn;
    View under_bar1, under_bar2, under_bar3;

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

        //changepw_btn = root.findViewById(R.id.changpw_btn);
        //logout_txt_btn = root.findViewById(R.id.logout_txt_btn);
        //del_id_btn = root.findViewById(R.id.del_id_btn);

        //상단
        settings = root.findViewById(R.id.settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getActivity(), Settings.class);
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




    /*
        library_tab.setOnClickListener(v -> {

            Log.e("user: ",user_email);
            Intent intent = new Intent(getActivity(), Mylib.class);
            intent.putExtra("user_email", user_email);
            intent.putExtra("user_UID", user_UID);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        });



    //여기에 들어가지 않는 버튼들
        changepw_btn.setOnClickListener(v -> {

          AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
            dialog.setTitle("비밀번호 변경")
                    .setMessage("가입하신 이메일로 비밀번호 재설정 링크를 보내드립니다. 계속 진행하시겠습니까?")
                    .setPositiveButton("네", (dialog1, which) -> {
                        mAuth = FirebaseAuth.getInstance();
                        mAuth.sendPasswordResetEmail(user_email).addOnCompleteListener(new OnCompleteListener<Void>() { //해당 이메일 주소로 비밀번호 재설정
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getActivity(), "해당 이메일로 비밀번호 재설정 링크를 보냈습니다.", Toast.LENGTH_SHORT).show();
                                    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                                    firebaseAuth.signOut();
                                    Intent intent = new Intent(getActivity(), Login.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    getActivity().finish();
                                }
                            }

                        });
                    })
                    .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .create()
                    .show();



        });
        del_id_btn.setOnClickListener(v -> {

            AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
            dialog.setTitle("회원탈퇴")
                    .setMessage("탈퇴시 모든 정보가 삭제되며 복구하실 수 없습니다.\n그래도 회원탈퇴를 진행하시겠습니까?")
                    .setPositiveButton("네", (dialog1, which) -> {
                        FirebaseDatabase database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동
                        DatabaseReference data = database.getReference("User/" + user_UID);
                        data.removeValue();
               //         DatabaseReference data2 = database.getReference("Friend/" + user_UID);
               //         data2.removeValue();
               //         DatabaseReference data3 = database.getReference("Book/" + user_UID);
               //         data3.removeValue();

                        mAuth = FirebaseAuth.getInstance();
                        mAuth.getCurrentUser().delete();

                        Toast.makeText(getActivity(), "지금까지 수북을 이용해주셔서 감사합니다.", Toast.LENGTH_SHORT).show();

                        getActivity().finishAffinity();

                    })
                    .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .create()
                    .show();
        });

        logout_txt_btn.setOnClickListener(v -> {
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth.signOut();
            Intent intent = new Intent(getActivity(), Login.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            getActivity().finish();
        });

     */



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