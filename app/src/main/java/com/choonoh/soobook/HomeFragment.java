package com.choonoh.soobook;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment implements View.OnClickListener{

    TextView bestseller_tab, wishlist_tab;
    View under_bar1, under_bar2;
    ImageButton search_book;

    HomeCategoryFragment bestsellerFragment;
    WishlistFragment wishlistFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);

        search_book = rootView.findViewById(R.id.search_book);
        search_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getActivity(), SearchBookTitle.class);
                startActivity(intent);
            }
        });

        bestseller_tab = rootView.findViewById(R.id.total_books);
        wishlist_tab = rootView.findViewById(R.id.wishlist_tab);

        under_bar1 = rootView.findViewById(R.id.under_bar1);
        under_bar2 = rootView.findViewById(R.id.under_bar2);

        bestseller_tab.setOnClickListener(this);
        wishlist_tab.setOnClickListener(this);


        bestsellerFragment = new HomeCategoryFragment();
        wishlistFragment = new WishlistFragment();

        getFragmentManager().beginTransaction().replace(R.id.child_container, bestsellerFragment).commit();

        return rootView;
    }

    @Override
    public void onClick(View v){

        switch (v.getId()){

            case R.id.total_books:
                bestseller_tab.setTextColor(Color.parseColor("#FF5F68"));
                wishlist_tab.setTextColor(Color.parseColor("#B9BABE"));
                under_bar1.setVisibility(View.VISIBLE);
                under_bar2.setVisibility(View.GONE);

              getFragmentManager().beginTransaction().replace(R.id.child_container, bestsellerFragment).commit();
                break;

            case R.id.wishlist_tab:
                bestseller_tab.setTextColor(Color.parseColor("#B9BABE"));
                wishlist_tab.setTextColor(Color.parseColor("#FF5F68"));
                under_bar1.setVisibility(View.GONE);
                under_bar2.setVisibility(View.VISIBLE);

                getFragmentManager().beginTransaction().replace(R.id.child_container, wishlistFragment).commit();
                break;

        }

    }


}
