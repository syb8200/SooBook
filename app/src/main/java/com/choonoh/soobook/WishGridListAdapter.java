package com.choonoh.soobook;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class WishGridListAdapter extends BaseAdapter {

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


        ImageView bookImg = convertView.findViewById(R.id.book_img);
        Uri book= Uri.parse(mylibList.getImg());
        Log.e("uri", book.toString());


        Glide.with(context).load(book).into(bookImg);


        convertView.setOnClickListener(v ->  {
            String title = items.get(position).getTitle();
            String auth = items.get(position).getauth();
            String pub = items.get(position).getPub();
            String isbn = items.get(position).getisbn();
            String cover = items.get(position).getImg();

            Intent intent = new Intent(context,WishDetailActivity.class);
            intent.putExtra("title", title);
            intent.putExtra("auth", auth);
            intent.putExtra("pub",pub);
            intent.putExtra("isbn",isbn);
            intent.putExtra("cover", cover);

            context.startActivity(intent);
        });
        return convertView;
    }
}
