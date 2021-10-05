package com.choonoh.soobook;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class WishlistFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup child_container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_wishlist, child_container, false);
        FirebaseAuth  firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        String user_UID = currentUser.getUid();
        String user_email = currentUser.getEmail();

        return rootView;
    }
}
