package com.choonoh.soobook;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public class WishlistFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup child_container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_wishlist, child_container, false);

        return rootView;
    }
}
