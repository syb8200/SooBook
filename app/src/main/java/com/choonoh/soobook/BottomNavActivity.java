package com.choonoh.soobook;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class BottomNavActivity extends AppCompatActivity {

    HomeFragment homeFragment;
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
                    @Override
                    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.home:
                                getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
                                return true;
                            case R.id.record:
                                getSupportFragmentManager().beginTransaction().replace(R.id.container, recordFragment).commit();
                                return true;
                            case R.id.profile:
                                getSupportFragmentManager().beginTransaction().replace(R.id.container, profileFragment).commit();
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
            toast = Toast.makeText(BottomNavActivity.this, "뒤로가기 버튼을 다시 한 번 눌러주세요.", Toast.LENGTH_SHORT);
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

}
