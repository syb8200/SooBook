package com.choonoh.soobook;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.loader.content.CursorLoader;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.net.URI;
import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static androidx.constraintlayout.motion.utils.Oscillator.TAG;


public class ProfileFragment extends Fragment implements View.OnClickListener{

    ImageButton settings,logout_btn;
    Button profile_edit, changest_btn;
    TextView statistics_tab, library_tab, friend_tab, nickname_tv, state_tv;       //changepw_btn, logout_txt_btn, del_id_btn;
    View under_bar1, under_bar2, under_bar3;
    ImageView profile_img;
    CircleImageView ProfileImage;

    String nickname;
    StatisticsFragment statisticsFragment;
    LibraryFragment libraryFragment;
    FriendFragment friendFragment;
    private File tempFile;
    private Uri imageUri;
    private String pathUri;
    public static final int PICK_FROM_ALBUM = 1;
    String profileImageUrl;



    FirebaseDatabase database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동
    FirebaseStorage storage = FirebaseStorage.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        String user_email = getArguments().getString("user_email");
        String user_UID = getArguments().getString("user_UID");
        String profileImgUrl = null;
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        StorageReference pathReference = storageReference.child("usersprofileImages");
        if(pathReference == null){
            Log.e("profile pic: ","프로필사진이 없습니당");
        }else {
            StorageReference submitProfile = storageReference.child("usersprofileImages/"+user_UID);

            Log.e("profile pic: ", "잘됨");

            submitProfile.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(getActivity()).load(uri).into(profile_img);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("profile pic: ", String.valueOf(e));
                }
            });


        }
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
        profile_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProfileEdit.class);
                intent.putExtra("user_email", user_email);
                intent.putExtra("user_UID", user_UID);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

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
        state_tv = root.findViewById(R.id.state_tv);
        profile_img  = root.findViewById(R.id.profile_img);


        DatabaseReference databaseReference = database.getReference("User/" + user_UID + "/nick"); // DB 테이블 연결FirebaseDatabase.getInstance().getReference().addListenerForSingleValueEvent(new ValueEventListener() {
        DatabaseReference databaseReference2 = database.getReference("User/" + user_UID + "/state"); // DB 테이블 연결FirebaseDatabase.getInstance().getReference().addListenerForSingleValueEvent(new ValueEventListener() {


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
        databaseReference2.addListenerForSingleValueEvent(new ValueEventListener() {
                                                              @Override
                                                              public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                  Object value = snapshot.getValue(Object.class);
                                                                  state_tv.setText(value.toString());
                                                              }

                                                              @Override
                                                              public void onCancelled(@NonNull DatabaseError error) {

                                                              }
                                                          }

        );



        return root;

    }




    @Override
    public void onClick(View v){
        Bundle bundle = new Bundle();
        String user_email = getArguments().getString("user_email");
        String user_UID = getArguments().getString("user_UID");
        switch (v.getId()){

            case R.id.statistics_tab:
                StatisticsFragment statisticsFragment = new StatisticsFragment();
                statistics_tab.setTextColor(Color.parseColor("#FF5F68"));
                library_tab.setTextColor(Color.parseColor("#B9BABE"));
                friend_tab.setTextColor(Color.parseColor("#B9BABE"));
                under_bar1.setVisibility(View.VISIBLE);
                under_bar2.setVisibility(View.GONE);
                under_bar3.setVisibility(View.GONE);
                bundle.putString("user_email", user_email);
                bundle.putString("user_UID", user_UID);
                statisticsFragment.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.child_container, statisticsFragment).commit();
                break;

            case R.id.library_tab:
                LibraryFragment libraryFragment = new LibraryFragment();
                statistics_tab.setTextColor(Color.parseColor("#B9BABE"));
                library_tab.setTextColor(Color.parseColor("#FF5F68"));
                friend_tab.setTextColor(Color.parseColor("#B9BABE"));
                under_bar1.setVisibility(View.GONE);
                under_bar2.setVisibility(View.VISIBLE);
                under_bar3.setVisibility(View.GONE);
                bundle.putString("user_email", user_email);
                bundle.putString("user_UID", user_UID);
                libraryFragment.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.child_container, libraryFragment).commit();
                break;

            case R.id.friend_tab:
                FriendFragment friendFragment = new FriendFragment();
                statistics_tab.setTextColor(Color.parseColor("#B9BABE"));
                library_tab.setTextColor(Color.parseColor("#B9BABE"));
                friend_tab.setTextColor(Color.parseColor("#FF5F68"));
                under_bar1.setVisibility(View.GONE);
                under_bar2.setVisibility(View.GONE);
                under_bar3.setVisibility(View.VISIBLE);
                bundle.putString("user_email", user_email);
                bundle.putString("user_UID", user_UID);
                friendFragment.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.child_container, friendFragment).commit();
                break;

        }

    }
}
