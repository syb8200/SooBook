package com.choonoh.soobook;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Home extends AppCompatActivity {

    HomeFragment homeFragment;
    HomeCategoryFragment homeCategoryFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        homeFragment =(HomeFragment) getSupportFragmentManager().findFragmentById(R.id.homeFragment);
       homeCategoryFragment = new HomeCategoryFragment();

        // homeCategoryFragment =(HomeCategoryFragment) getSupportFragmentManager().findFragmentById(R.id.homeCategoryFragment);



    }
public void onFragmentChanged(int index){
        if(index==0){
            getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
        }
    if(index==1){
        getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
    }
}

}