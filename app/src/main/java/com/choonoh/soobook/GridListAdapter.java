package com.choonoh.soobook;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class GridListAdapter extends BaseAdapter {

    ArrayList<MylibList> items = new ArrayList<MylibList>();
    Context context;

    public void addItem(MylibList item){
        items.add(item);

    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }





    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        context = parent.getContext();
        MylibList mylibList = items.get(position);

        if(convertView==null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item, parent, false);
        }

        TextView numberText = convertView.findViewById(R.id.numberText);
        ImageView bookImg = convertView.findViewById(R.id.book_img);
        Uri book= Uri.parse(mylibList.getImg());
        Log.e("uri", book.toString());
        numberText.setText(mylibList.getTitle());

        Glide.with(context).load(book).into(bookImg);

        //클릭시 디테일뷰로 넘어감 (근데 제목이랑  isbn이랑 사진밖에 안넘어감)

     /*   convertView.setOnClickListener(v ->  {

            String title = items.get(position).getTitle();
     //      String author = items.get(position).getAuthor();
     //    String publisher = items.get(position).getPublisher();
        //    String customerReviewRank = items.get(position).getCustomerReviewRank();
            String isbn = items.get(position).getisbn();
     //       String pubDate = items.get(position).getPubDate();
      //      String description = items.get(position).getDescription();
            String cover = items.get(position).getImg();

            Intent intent = new Intent(context,BookDetailActivity.class);
            intent.putExtra("title", title);
         //   intent.putExtra("author", author);
        //    intent.putExtra("publisher",publisher);
        //    intent.putExtra("customerReviewRank", customerReviewRank);
            intent.putExtra("isbn",isbn);
        //    intent.putExtra("pubDate", pubDate);
         //   intent.putExtra("description",description);
            intent.putExtra("coverSmallUrl", cover);

            context.startActivity(intent);
        });*/
        return convertView;
    }
}
