package com.choonoh.soobook;

import android.app.DownloadManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class TestFindFriendActivity extends AppCompatActivity {

    DatabaseReference mUserRef;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    ImageButton SearchButton;
    EditText SearchInputText;

    RecyclerView SearchResultList;

    DatabaseReference allUsersDatabaseRef;

    String user_email, user_UID;


    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.test_find_friend);

        user_email = getIntent().getStringExtra("user_email");
        user_UID = getIntent().getStringExtra("user_UID");
        Log.e(this.getClass().getName(), user_UID + "&" + user_email);

        allUsersDatabaseRef = FirebaseDatabase.getInstance().getReference().child("User");

        SearchResultList = findViewById(R.id.search_result_list);
        SearchResultList.setHasFixedSize(true);
        SearchResultList.setLayoutManager(new LinearLayoutManager(this));

        SearchButton = findViewById(R.id.search_people_friends_button);
        SearchInputText = findViewById(R.id.search_box_input);


        SearchButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String searchBoxInput = SearchInputText.getText().toString();
                SearchPeopleAndFriends(searchBoxInput);
            }
        });

    }

    private void SearchPeopleAndFriends (String searchBoxInput){
        Toast.makeText(this, "Searching...", Toast.LENGTH_SHORT).show();
        Query searchPeopleandFriendsQuery = allUsersDatabaseRef.orderByChild("nick")
                .startAt(searchBoxInput).endAt(searchBoxInput+ "\uf8ff");

        FirebaseRecyclerAdapter<TestFindFriends, TestFindFriendsViewHolder> firebaseRecyclerAdapter
                = new FirebaseRecyclerAdapter<TestFindFriends, TestFindFriendsViewHolder>
                (
                    TestFindFriends.class,
                        R.layout.test_all_users_display_layout,
                        TestFindFriendsViewHolder.class,
                        searchPeopleandFriendsQuery

                )
        {
            @Override
            protected void populateViewHolder(TestFindFriendsViewHolder viewHolder, TestFindFriends model, int position )
            {
                viewHolder.setNickname(model.getNickname());
                viewHolder.setStatus(model.getStatus());
                viewHolder.setProfileimage(getApplicationContext(), model.getProfileimage());
            }
        };

        SearchResultList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class TestFindFriendsViewHolder extends RecyclerView.ViewHolder{
        View mView;

        public TestFindFriendsViewHolder(View itemView){
            super(itemView);
            mView = itemView;
        }

        public void setProfileimage(Context ctx, String profileimage){
            CircleImageView myImage = (CircleImageView) mView.findViewById(R.id.all_users_profile_image);
            Picasso.get().load(profileimage).placeholder(R.drawable.profile_first).into(myImage);
        }

        public void setNickname(String nick){
            TextView myName = mView.findViewById(R.id.all_users_full_name);
            myName.setText(nick);
        }

        public void setStatus(String status){
            TextView myStatus = mView.findViewById(R.id.all_users_status);
            myStatus.setText(status);
        }

    }

}
