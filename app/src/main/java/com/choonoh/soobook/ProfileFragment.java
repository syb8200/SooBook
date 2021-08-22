package com.choonoh.soobook;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class ProfileFragment extends Fragment {

    ImageButton logout_btn;
    TextView admin_btn, add_btn, changepw_btn, logout_txt_btn, del_id_btn;
    private FirebaseAuth mAuth;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        String user_email = getArguments().getString("user_email");
        String user_UID = getArguments().getString("user_UID");
        changepw_btn = root.findViewById(R.id.changpw_btn);
        logout_txt_btn = root.findViewById(R.id.logout_txt_btn);
        del_id_btn = root.findViewById(R.id.del_id_btn);


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
               /*         DatabaseReference data2 = database.getReference("Friend/" + user_UID);
                        data2.removeValue();
                        DatabaseReference data3 = database.getReference("Book/" + user_UID);
                        data3.removeValue();
*/
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

        return root;
    }
}