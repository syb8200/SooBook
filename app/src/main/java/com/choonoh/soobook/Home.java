package com.choonoh.soobook;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;


public class Home extends AppCompatActivity {

    HomeFragment homeFragment;
    HomeCategoryFragment homeCategoryFragment;
    String user_email, user_UID;
 /*   @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        homeFragment =(HomeFragment) getSupportFragmentManager().findFragmentById(R.id.homeFragment);
        homeCategoryFragment = new HomeCategoryFragment();



    }*/

    RecordFragment recordFragment;
    ProfileFragment profileFragment;

    static String bottom_frag = "home";

    long backKeyPressedTime = 0;
    Toast toast;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_nav);

        homeFragment = new HomeFragment();
        recordFragment = new RecordFragment();
        profileFragment = new ProfileFragment();

        user_email = getIntent().getStringExtra("user_email");
        user_UID = getIntent().getStringExtra("user_UID");


        final BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        switch(bottom_frag){
            case "home":
                getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
                break;
            case "record":
                getSupportFragmentManager().beginTransaction().replace(R.id.container, recordFragment).commit();
                break;
            case "profile":
                getSupportFragmentManager().beginTransaction().replace(R.id.container, profileFragment).commit();
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    Bundle bundle = new Bundle();
                    @Override
                    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.home:
                                getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
                                return true;

                            case R.id.record:
                                getSupportFragmentManager().beginTransaction().replace(R.id.container, recordFragment).commit();
                                bundle.putString("user_email", user_email);
                                bundle.putString("user_UID", user_UID);
                                recordFragment.setArguments(bundle);
                                return true;

                                case R.id.profile:
                                getSupportFragmentManager().beginTransaction().replace(R.id.container, profileFragment).commit();
                                bundle.putString("user_email", user_email);
                                bundle.putString("user_UID", user_UID);
                                profileFragment.setArguments(bundle);
                                return true;
                        }
                        return false;
                    }
                }
        );

    }

    @Override
    public void onBackPressed(){
        if(System.currentTimeMillis() > backKeyPressedTime + 2000){
            backKeyPressedTime = System.currentTimeMillis();
            toast = Toast.makeText(Home.this, "뒤로가기 버튼을 다시 한 번 눌러주세요.", Toast.LENGTH_SHORT);
            toast.show();
            handler.postDelayed(new Runnable(){
                @Override
                public void run(){
                    toast.cancel();
                }
            }, 1000);
            return;
        }
        if(System.currentTimeMillis() <= backKeyPressedTime + 2000){
            finish();
            toast.cancel();
        }
    }



    /*
public void onFragmentChanged(int index){
        if(index==0){
            getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
        }
    if(index==1){
        getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
    }
}
*/
}