package com.choonoh.soobook;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.text.Layout;
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


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

public class Settings extends AppCompatActivity {
    Button logout_btn;
    //ImageButton back_btn;
    View changepw_btn, del_btn;
    String user_email, user_UID;
    TextView email_tv;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


                user_email = getIntent().getStringExtra("user_email");
                user_UID = getIntent().getStringExtra("user_UID");
                changepw_btn = findViewById(R.id.changepw_btn);
                logout_btn = findViewById(R.id.logout_btn);
                del_btn = findViewById(R.id.del_btn);
                email_tv = findViewById(R.id.email_tv);
                email_tv.setText(user_email);
               // back_btn = findViewById(R.id.back_btn);
               /*
                back_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v){

                    }
                }); */


        changepw_btn.setOnClickListener(v -> {

          AlertDialog.Builder dialog = new AlertDialog.Builder(Settings.this);
            dialog.setTitle("???????????? ??????")
                    .setMessage("???????????? ???????????? ???????????? ????????? ????????? ??????????????????. ?????? ?????????????????????????")
                    .setPositiveButton("???", (dialog1, which) -> {
                        mAuth = FirebaseAuth.getInstance();
                        mAuth.sendPasswordResetEmail(user_email).addOnCompleteListener(new OnCompleteListener<Void>() { //?????? ????????? ????????? ???????????? ?????????
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(Settings.this, "?????? ???????????? ???????????? ????????? ????????? ???????????????.", Toast.LENGTH_SHORT).show();
                                    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                                    firebaseAuth.signOut();
                                    Intent intent = new Intent(Settings.this, Login.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    Settings.this.finish();
                                }
                            }

                        });
                    })
                    .setNegativeButton("?????????", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .create()
                    .show();



        });
        del_btn.setOnClickListener(v -> {

            AlertDialog.Builder dialog = new AlertDialog.Builder(Settings.this);
            dialog.setTitle("????????????")
                    .setMessage("????????? ?????? ????????? ???????????? ???????????? ??? ????????????.\n????????? ??????????????? ?????????????????????????")
                    .setPositiveButton("???", (dialog1, which) -> {
                        FirebaseDatabase database = FirebaseDatabase.getInstance(); // ?????????????????? ?????????????????? ??????
                        DatabaseReference data = database.getReference("User/" + user_UID);
                        data.removeValue();
               //         DatabaseReference data2 = database.getReference("Friend/" + user_UID);
               //         data2.removeValue();
               //         DatabaseReference data3 = database.getReference("Book/" + user_UID);
               //         data3.removeValue();

                        mAuth = FirebaseAuth.getInstance();
                        mAuth.getCurrentUser().delete();

                        Toast.makeText(Settings.this, "???????????? ????????? ?????????????????? ???????????????.", Toast.LENGTH_SHORT).show();

                        Settings.this.finishAffinity();

                    })
                    .setNegativeButton("?????????", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .create()
                    .show();
        });

        logout_btn.setOnClickListener(v -> {
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth.signOut();
            Intent intent = new Intent(Settings.this, Login.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(intent);
            Settings.this.finish();

        });



            }


}
