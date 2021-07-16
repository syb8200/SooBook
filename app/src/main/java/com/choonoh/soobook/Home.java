package com.choonoh.soobook;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Home extends AppCompatActivity {

    FriLibFragment friLibFragment;
    MyLibFragment myLibFragment;
  //  BeforeFindLibFragment beforeFindLibFragment;
    FindLibFragment findLibFragment;
    MyPageFragment myPageFragment;

    String user_email, user_UID;
    String bottom_frag = "my_lib";

    private long backKeyPressedTime = 0;
    private Toast toast;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottom_frag = getIntent().getStringExtra("fragment");
        user_email = getIntent().getStringExtra("user_email");
        user_UID = getIntent().getStringExtra("user_UID");

        Log.e(this.getClass().getName(), user_email + ", " + user_UID + ", " + bottom_frag);

        friLibFragment = new FriLibFragment();
        myLibFragment = new MyLibFragment();
       findLibFragment = new FindLibFragment();
    //    beforeFindLibFragment = new BeforeFindLibFragment();
        myPageFragment = new MyPageFragment();
        final BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        switch (bottom_frag) {
            default:
            case "my_lib":
                getSupportFragmentManager().beginTransaction().replace(R.id.container, myLibFragment).commit();
                Bundle toMyLibFrag = new Bundle();
                toMyLibFrag.putString("user_email", user_email);
                toMyLibFrag.putString("user_UID", user_UID);
                myLibFragment.setArguments(toMyLibFrag);
                bottomNavigationView.setSelectedItemId(R.id.my_lib);
                break;
            case "fri_lib":
                getSupportFragmentManager().beginTransaction().replace(R.id.container, friLibFragment).commit();
                Bundle toFriendLibFrag = new Bundle();
                toFriendLibFrag.putString("user_email", user_email);
                toFriendLibFrag.putString("user_UID", user_UID);
                friLibFragment.setArguments(toFriendLibFrag);
                bottomNavigationView.setSelectedItemId(R.id.fri_lib);
                break;
            case "find_lib":
                getSupportFragmentManager().beginTransaction().replace(R.id.container, findLibFragment).commit();
                Bundle findLibFrag = new Bundle();
                findLibFrag.putString("user_email", user_email);
                findLibFrag.putString("user_UID", user_UID);
                findLibFragment.setArguments(findLibFrag);
                bottomNavigationView.setSelectedItemId(R.id.find_lib);
                break;
            case "myPage":
                getSupportFragmentManager().beginTransaction().replace(R.id.container, myPageFragment).commit();
                Bundle myPage = new Bundle();
                myPage.putString("user_email", user_email);
                myPage.putString("user_UID", user_UID);
                myPageFragment.setArguments(myPage);
                bottomNavigationView.setSelectedItemId(R.id.my_page);
                break;
        }
        bottomNavigationView.setOnNavigationItemSelectedListener(
                menuItem -> {
                    switch (menuItem.getItemId()) {
                        case R.id.fri_lib:
                            getSupportFragmentManager().beginTransaction().replace(R.id.container, friLibFragment).commit();
                            Bundle toFirLibFrag = new Bundle();
                            toFirLibFrag.putString("user_email", user_email);
                            toFirLibFrag.putString("user_UID", user_UID);
                            friLibFragment.setArguments(toFirLibFrag);
                            return true;
                        case R.id.my_lib:
                            getSupportFragmentManager().beginTransaction().replace(R.id.container, myLibFragment).commit();
                            Bundle toMyLibFrag = new Bundle();
                            toMyLibFrag.putString("user_email", user_email);
                            toMyLibFrag.putString("user_UID", user_UID);
                            myLibFragment.setArguments(toMyLibFrag);
                            return true;
                        case R.id.find_lib:
                            getSupportFragmentManager().beginTransaction().replace(R.id.container, findLibFragment).commit();
                            Bundle toFindLibFrag = new Bundle();
                            toFindLibFrag.putString("user_email", user_email);
                            toFindLibFrag.putString("user_UID", user_UID);
                             findLibFragment.setArguments(toFindLibFrag);
                            return true;
                        case R.id.my_page:
                            getSupportFragmentManager().beginTransaction().replace(R.id.container, myPageFragment).commit();
                            Bundle toMyPageFrag = new Bundle();
                            toMyPageFrag.putString("user_email", user_email);
                            toMyPageFrag.putString("user_UID", user_UID);
                            myPageFragment.setArguments(toMyPageFrag);
                            return true;
                    }
                    return false;
                }
        );
    }
    public void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
        Bundle bundle = new Bundle();
        bundle.putString("user_email", user_email);
        bundle.putString("user_UID", user_UID);
        fragment.setArguments(bundle);
    }
    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            toast = Toast.makeText(Home.this, "'뒤로' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT); toast.show();
            handler.postDelayed(toast::cancel, 1000);
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            finish();
            toast.cancel();
        }
    }
}