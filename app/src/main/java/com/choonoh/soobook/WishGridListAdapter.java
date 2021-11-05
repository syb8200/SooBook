package com.choonoh.soobook;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class WishGridListAdapter extends BaseAdapter {

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = firebaseAuth.getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동

    final String user_UID = currentUser.getUid();
    final String user_email = currentUser.getEmail();


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

            /*
            Intent intent = new Intent(context,WishDetailActivity.class);
            intent.putExtra("title", title);
            intent.putExtra("auth", auth);
            intent.putExtra("pub",pub);
            intent.putExtra("isbn",isbn);
            intent.putExtra("cover", cover);

            context.startActivity(intent);
             */


            AlertDialog.Builder ad = new AlertDialog.Builder(context);
            ad.setTitle("위시리스트");
            ad.setMessage(title);

            ad.create();

            ad.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    FirebaseDatabase database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동
                    DatabaseReference data = database.getReference("Wishlist/" + user_UID + "/" + isbn);
                    data.removeValue();
                    Toast toast = Toast.makeText(context, "위시리스트에서 삭제되었습니다.", Toast.LENGTH_SHORT);
                    toast.show();
                    Handler handler = new Handler();
                    handler.postDelayed(toast::cancel, 1000);
                    dialog.dismiss();

                    items.remove(position);
                    notifyDataSetChanged();
                }
            });

            ad.setNegativeButton("읽는 책 추가", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (!isbn.equals("")) {

                        postFirebaseDatabase(true);

                        FirebaseDatabase database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동
                        DatabaseReference data = database.getReference("Wishlist/" + user_UID + "/" + isbn);
                        data.removeValue();
                        Toast toast = Toast.makeText(context, "읽는책에 추가되었습니다.", Toast.LENGTH_SHORT);
                        toast.show();
                        Handler handler = new Handler();
                        handler.postDelayed(toast::cancel, 1000);
                        dialog.dismiss();

                    } else {
                        Toast toast = Toast.makeText(context, "이미 등록한 책입니다.", Toast.LENGTH_SHORT);
                        toast.show();
                        Handler handler = new Handler();
                        handler.postDelayed(toast::cancel, 1000);
                        dialog.dismiss();
                    }
                }

                public void postFirebaseDatabase(boolean add) {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy년 MM월dd일 HH시mm분", Locale.KOREA);
                    //시간 좀 안맞음 수정해야함
                    long now = System.currentTimeMillis();
                    Date time = new Date(now);
                    String time2 = format.format(time);
                    DatabaseReference mPostReference = FirebaseDatabase.getInstance().getReference();
                    Map<String, Object> childUpdates = new HashMap<>();
                    Map<String, Object> postValues = null;
                    if (add) {
                        FirebaseMylibPost post = new FirebaseMylibPost(user_UID, user_email, isbn, title, cover, time2, auth, pub);
                        postValues = post.toMap();
                    }
                    String root = "/Mylib/" + user_UID + "/" + isbn;
                    childUpdates.put(root, postValues);
                    mPostReference.updateChildren(childUpdates);
                }
            });

            ad.show();

        });

        return convertView;
    }
}
