package com.choonoh.soobook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.bluetooth.BluetoothAdapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;

public class RecordFragment extends Fragment {
    int h = 0, m = 0, s = 0, i = 0, is_first = 1;
    int measured = 0, pre_measured = 0, valid_value = 0;
    String hh, mm, ss;
    String book_img, book_isbn, book_title, book_auth, book_pub, weekDay;
    String year, month, day, totalBookNum, totalReadBookNum, mon, tue, wed, thu, fri, sat, sun;
    String year_db, month_db, day_db, totalBookNum_db, totalReadBookNum_db, mon_db, tue_db, wed_db, thu_db, fri_db, sat_db, sun_db;
    BluetoothSPP bt;
    String user_email, user_UID;
    ImageButton direct_record;
    long startTimeNum, endTimeNum;
    MylibList[] myLibLists = new MylibList[50];

    private final DatabaseReference mPostReference = FirebaseDatabase.getInstance().getReference();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_record, container, false);
        Bundle bundle = getArguments();

        direct_record = rootView.findViewById(R.id.direct_record);
        direct_record.setOnClickListener(v->{
            Intent intent = new Intent(getContext(), DirectRecord.class);
            intent.putExtra("user_email", user_email);
            intent.putExtra("user_UID", user_UID);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        user_email = bundle.getString("user_email");
        user_UID = bundle.getString("user_UID");

        GridView gridView = rootView.findViewById(R.id.list_gridview);
        RecordGridListAdapter adapter = new RecordGridListAdapter();

        FirebaseDatabase database = FirebaseDatabase.getInstance(); // ?????????????????? ?????????????????? ??????
        DatabaseReference databaseReference = database.getReference("Mylib/"+user_UID+"/"); // DB ????????? ??????

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            int i = 0;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    MylibList mylibList = snapshot.getValue(MylibList.class);
                    book_img = mylibList.getImg();
                    book_isbn = mylibList.getisbn();
                    book_title = mylibList.getTitle();
                    book_auth = mylibList.getauth();
                    book_pub = mylibList.getPub();
                    adapter.addItem(mylibList);

                    myLibLists[i] = mylibList;
                    i++;
                }
                gridView.setAdapter(adapter);
                Log.e("child ??????", String.valueOf(i)); // ????????? ??????
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Mylib", String.valueOf(databaseError.toException())); // ????????? ??????
            }
        });

        bt = new BluetoothSPP(this.getContext()); //Initializing

        if (!bt.isBluetoothAvailable()) { //???????????? ?????? ??????
            Toast.makeText(getActivity().getApplicationContext() //* ?????? *
                    , "??????????????? ????????? ??? ????????????."
                    , Toast.LENGTH_SHORT).show();
            getActivity().finish(); //* ?????? *
        }
        // ------------------------------ ????????? ????????? ----------------------------- //
        //????????? ??????
        bt.setOnDataReceivedListener((data, message) -> {
            TextView hour = getView().findViewById(R.id.hour);
            TextView minute = getView().findViewById(R.id.minute);
            TextView second = getView().findViewById(R.id.second);

            String[] array = message.split(",");
            measured = Integer.parseInt(array[0]);

            if(pre_measured > 0 && measured == -1){
                androidx.constraintlayout.widget.ConstraintLayout constraintLayout = rootView.findViewById(R.id.const_record);
                constraintLayout.setVisibility(View.VISIBLE);
                valid_value = pre_measured;
            }
            pre_measured = measured;
            Log.e("array[0]", array[0]);

            s = measured;
            if(s > 0){
                if(s == 1)
                    startTimeNum = System.currentTimeMillis();
                if(s >= 60) {
                    s -= (s/60)*60;
                    if(s % 60 == 0)
                        m += 1;
                }
                if(m >= 60){
                    m -= (m/60)*60;
                    if(m % 60 == 0)
                        h += 1;
                }

                if(h < 10)
                    hh = "0" + h;
                else
                    hh = "" + h;

                if(m < 10)
                    mm = "0" + m;
                else
                    mm = "" + m;

                if(s < 10)
                    ss = "0" + s;
                else
                    ss = "" + s;

                hour.setText(hh);
                minute.setText(mm);
                second.setText(ss);
            }
        });

        // ------------------------------ ????????? ????????? ----------------------------- //
        bt.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener() { //???????????? ???
            public void onDeviceConnected(String name, String address) {
//                TextView isConnect = getActivity().findViewById(R.id.isConnect);
//                isConnect.setText(R.string.yes_connect);
                Toast.makeText(getActivity().getApplicationContext()
                        , name + "??? ?????????????????????."
                        , Toast.LENGTH_SHORT).show();
            }

            public void onDeviceDisconnected() { //????????????
//                TextView isConnect = getActivity().findViewById(R.id.isConnect);
//                isConnect.setText(R.string.not_connect);
                Toast.makeText(getActivity().getApplicationContext()
                        , "????????? ?????????????????????", Toast.LENGTH_SHORT).show();
            }

            public void onDeviceConnectionFailed() { //????????????
//                TextView isConnect = getActivity().findViewById(R.id.isConnect);
//                isConnect.setText(R.string.not_connect);
                Toast.makeText(getActivity().getApplicationContext()
                        , "????????? ??? ????????????.", Toast.LENGTH_SHORT).show();
            }
        });

        //???????????? ?????? (DeviceList??? ??????)
        Button btnConnect = rootView.findViewById(R.id.btnConnect); //????????????
        btnConnect.setOnClickListener(v -> {
            if (bt.getServiceState() == BluetoothState.STATE_CONNECTED) {
                bt.disconnect();
            } else {
                Intent intent = new Intent(getActivity().getApplicationContext(), DeviceList.class);
                startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
            }
        });

        gridView.setOnItemClickListener((parent, v, position, id) -> {
            endTimeNum = System.currentTimeMillis();

            SimpleDateFormat weekdayFormat = new SimpleDateFormat("EE", Locale.getDefault());
            SimpleDateFormat month_day = new SimpleDateFormat("MM-dd");
            SimpleDateFormat hour_minute = new SimpleDateFormat("hh:mm");
            SimpleDateFormat month_day_hour_minute = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            Date startTimeDate = new Date(startTimeNum);
            Date endTimeDate = new Date(endTimeNum);

            weekDay = weekdayFormat.format(endTimeNum);
            String readTime = Integer.toString(valid_value);
            String startTime = hour_minute.format(startTimeDate);
            String endTime = hour_minute.format(endTimeDate);
            String date = month_day.format(startTimeDate);
            String firebaseKey = month_day_hour_minute.format(endTimeDate);

            Log.e("readTime", readTime + ", " + startTime + ", " + endTime + ", " + date + ", " + firebaseKey + ", " + weekDay);

            Map<String, Object> childUpdates = new HashMap<>();
            Map<String, Object> postValues = null;

            //String uid, String readTime , String startTime, String endTime, String date
            FirebaseReadTimePost post = new FirebaseReadTimePost(readTime, startTime, endTime, date);
            postValues = post.toMap();
            String root ="/ReadTime/" + user_UID + "/" + firebaseKey + "/";
            childUpdates.put(root, postValues);
           mPostReference.updateChildren(childUpdates);

            Log.e("myLibLists", myLibLists[(position)].img + ", " + myLibLists[(position)].title + ", " + myLibLists[(position)].auth
                    + ", " + myLibLists[(position)].pub);

            DatabaseReference mPostReference2 = FirebaseDatabase.getInstance().getReference("ReadTime/info/"+user_UID);
            ValueEventListener postListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        switch (i){
                            case 0:
                                day_db = snapshot.getValue().toString();
                                break;
                            case 1:
                                fri_db = snapshot.getValue().toString();
                                break;
                            case 2:
                                mon_db = snapshot.getValue().toString();
                                break;
                            case 3:
                                month_db = snapshot.getValue().toString();
                                break;
                            case 4:
                                sat_db = snapshot.getValue().toString();
                                break;
                            case 5:
                                sun_db = snapshot.getValue().toString();
                                break;
                            case 6:
                                thu_db = snapshot.getValue().toString();
                                break;
                            case 7:
                                totalBookNum_db = snapshot.getValue().toString();
                                break;
                            case 8:
                                totalReadBookNum_db = snapshot.getValue().toString();
                                break;
                            case 9:
                                tue_db = snapshot.getValue().toString();
                                break;
                            case 10:
                                wed_db = snapshot.getValue().toString();
                                break;
                            case 11:
                                year_db = snapshot.getValue().toString();

                                SimpleDateFormat yyyy = new SimpleDateFormat("yyyy");
                                SimpleDateFormat MM = new SimpleDateFormat("MM");
                                SimpleDateFormat dd = new SimpleDateFormat("dd");

                                String end_year = yyyy.format(endTimeNum);
                                String end_month = MM.format(endTimeNum);
                                String end_day = dd.format(endTimeNum);
                                if(year_db.equals(end_year) && month_db.equals(end_month) && Integer.parseInt(day_db) + 7 > Integer.parseInt(end_day)){
                                    switch (weekDay){
                                        case "???":
                                            mon_db = Integer.toString(Integer.parseInt(mon_db) + valid_value);
                                            break;
                                        case "???":
                                            tue_db = Integer.toString(Integer.parseInt(tue_db) + valid_value);
                                            break;
                                        case "???":
                                            wed_db = Integer.toString(Integer.parseInt(wed_db) + valid_value);
                                            break;
                                        case "???":
                                            thu_db = Integer.toString(Integer.parseInt(thu_db) + valid_value);
                                            break;
                                        case "???":
                                            fri_db = Integer.toString(Integer.parseInt(fri_db) + valid_value);
                                            break;
                                        case "???":
                                            sat_db = Integer.toString(Integer.parseInt(sat_db) + valid_value);
                                            break;
                                        case "???":
                                            sun_db = Integer.toString(Integer.parseInt(sun_db) + valid_value);
                                            break;
                                    }
                                    Log.e("result(if ?????????)", mon_db + ", " + tue_db + ", " + wed_db + ", " + thu_db + ", "
                                    + fri_db + ", " + sat_db + ", " + sun_db + ", ");

                                    Map<String, Object> childUpdates = new HashMap<>();
                                    Map<String, Object> postValues = null;

                                    //String uid, String readTime , String startTime, String endTime, String date
                                    FirebaseReadTimePost post = new FirebaseReadTimePost(readTime, startTime, endTime, date);
                                    postValues = post.toMap();
                                    String root ="/ReadTime/" + user_UID + "/" + firebaseKey + "/";
                                    childUpdates.put(root, postValues);
                                    mPostReference.updateChildren(childUpdates);

                                    Map<String, Object> childUpdates2 = new HashMap<>();
                                    Map<String, Object> postValues2 = new HashMap<>();

                                    //mon, tue, wed, thu, fri, sat, sun;
                                    postValues2.put("mon", mon_db);
                                    postValues2.put("tue", tue_db);
                                    postValues2.put("wed", wed_db);
                                    postValues2.put("thu", thu_db);
                                    postValues2.put("fri", fri_db);
                                    postValues2.put("sat", sat_db);
                                    postValues2.put("sun", sun_db);

                                    postValues2.put("year", year_db);
                                    postValues2.put("month", month_db);
                                    postValues2.put("day", day_db);

                                    postValues2.put("totalBookNum", totalBookNum_db);
                                    postValues2.put("totalReadBookNum", totalReadBookNum_db);

                                    root ="/ReadTime/info/" + user_UID + "/";
                                    childUpdates2.put(root, postValues2);
                                    mPostReference.updateChildren(childUpdates2);

                                    Intent intent=new Intent(getContext(), WriteMemo.class);

                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.putExtra("img", myLibLists[(position)].img);
                                    intent.putExtra("title", myLibLists[(position)].title);
                                    intent.putExtra("auth", myLibLists[(position)].auth);
                                    intent.putExtra("pub", myLibLists[(position)].pub);
                                    intent.putExtra("isbn", myLibLists[(position)].isbn);

                                    startActivity(intent);
                               }
                        }
                        i++;
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Getting Post failed, log a message
                    Log.e("StatisticsFragment", "loadPost:onCancelled", databaseError.toException());
                }
            };
            mPostReference2.addValueEventListener(postListener);
//            DatabaseReference mPostReference2 = FirebaseDatabase.getInstance().getReference("ReadTime/info/");
//            ValueEventListener postListener = new ValueEventListener() {
//
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                        String[] splitText = dataSnapshot.getValue().toString().split("=");
//                        Log.e("dataSnapshot.getKey() ", splitText[0]);
//                        Log.e("UID ", "{" + user_UID);
//
//                        if(splitText[0].equals("{" + user_UID) && is_first == 1) {
//                            Log.e("is same?: ", "YES");
//                            MyReadInfoList myReadInfoList = snapshot.getValue(MyReadInfoList.class);
//                            year = myReadInfoList.getYear();
//                            month = myReadInfoList.getMonth();
//                            day = myReadInfoList.getDay();
//
//                            totalBookNum = myReadInfoList.getTotalBookNum();
//                            totalReadBookNum = myReadInfoList.getTotalReadBookNum();
//
//                            mon = myReadInfoList.getMon();
//                            tue = myReadInfoList.getTue();
//                            wed = myReadInfoList.getWed();
//                            thu = myReadInfoList.getThu();
//                            fri = myReadInfoList.getFri();
//                            sat = myReadInfoList.getSat();
//                            sun = myReadInfoList.getSun();
//
//                            Log.e("??????", mon + ", " + tue + ", " + wed + ", " + thu + ", "
//                                    + fri + ", " + sat + ", " + sun + ", ");
//
//                            SimpleDateFormat yyyy = new SimpleDateFormat("yyyy");
//                            SimpleDateFormat MM = new SimpleDateFormat("MM");
//                            SimpleDateFormat dd = new SimpleDateFormat("dd");
//
//                            String end_year = yyyy.format(endTimeNum);
//                            String end_month = MM.format(endTimeNum);
//                            String end_day = dd.format(endTimeNum);
//                            if(year.equals(end_year) && month.equals(end_month) && Integer.parseInt(day) + 7 > Integer.parseInt(end_day)){
//                                switch (weekDay){
//                                    case "???":
//                                        mon = Integer.toString(Integer.parseInt(mon) + valid_value);
//                                        break;
//                                    case "???":
//                                        tue = Integer.toString(Integer.parseInt(tue) + valid_value);
//                                        break;
//                                    case "???":
//                                        wed = Integer.toString(Integer.parseInt(wed) + valid_value);
//                                        break;
//                                    case "???":
//                                        thu = Integer.toString(Integer.parseInt(thu) + valid_value);
//                                        break;
//                                    case "???":
//                                        fri = Integer.toString(Integer.parseInt(fri) + valid_value);
//                                        break;
//                                    case "???":
//                                        sat = Integer.toString(Integer.parseInt(sat) + valid_value);
//                                        break;
//                                    case "???":
//                                        sun = Integer.toString(Integer.parseInt(sun) + valid_value);
//                                        break;
//                                }
//                                Log.e("result(if ?????????)", mon + ", " + tue + ", " + wed + ", " + thu + ", "
//                                + fri + ", " + sat + ", " + sun + ", ");
//
//                                Map<String, Object> childUpdates = new HashMap<>();
//                                Map<String, Object> postValues = null;
//
//                                //String uid, String readTime , String startTime, String endTime, String date
//                                FirebaseReadTimePost post = new FirebaseReadTimePost(readTime, startTime, endTime, date);
//                                postValues = post.toMap();
//                                String root ="/ReadTime/" + user_UID + "/" + firebaseKey + "/";
//                                childUpdates.put(root, postValues);
//                                mPostReference.updateChildren(childUpdates);
//
//                                Map<String, Object> childUpdates2 = new HashMap<>();
//                                Map<String, Object> postValues2 = new HashMap<>();
//
//                                //mon, tue, wed, thu, fri, sat, sun;
//                                postValues2.put("mon", mon);
//                                postValues2.put("tue", tue);
//                                postValues2.put("wed", wed);
//                                postValues2.put("thu", thu);
//                                postValues2.put("fri", fri);
//                                postValues2.put("sat", sat);
//                                postValues2.put("sun", sun);
//
//                                postValues2.put("year", year);
//                                postValues2.put("month", month);
//                                postValues2.put("day", day);
//
//                                postValues2.put("totalBookNum", totalBookNum);
//                                postValues2.put("totalReadBookNum", totalReadBookNum);
//
//                                root ="/ReadTime/info/" + user_UID + "/";
//                                childUpdates2.put(root, postValues2);
//                                mPostReference.updateChildren(childUpdates2);
//                                is_first = 0;
//                           }
//                        }
//                        i++;
//                    }
//                }
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//                    // Getting Post failed, log a message
//                    Log.e("RecordFragment", "loadPost:onCancelled", databaseError.toException());
////                }
//            };
//            mPostReference2.addValueEventListener(postListener);

//                Intent intent=new Intent(getContext(), WriteMemo.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                intent.putExtra("img", myLibLists[(position)].img);
//                intent.putExtra("title", myLibLists[(position)].title);
//                intent.putExtra("auth", myLibLists[(position)].auth);
//                intent.putExtra("pub", myLibLists[(position)].pub);
//
//                startActivity(intent);
        });
        return rootView;
    }
    public void onDestroy() {
        super.onDestroy();
        bt.stopService(); //???????????? ??????
    }

    public void onStart() {
        super.onStart();
        if (!bt.isBluetoothEnabled()) { //
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, BluetoothState.REQUEST_ENABLE_BT);
        } else {
            if (!bt.isServiceAvailable()) {
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_OTHER);
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
            if (resultCode == Activity.RESULT_OK)
                bt.connect(data);
        } else if (requestCode == BluetoothState.REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_OTHER);
            } else {
                Toast.makeText(getActivity().getApplicationContext()
                        , "??????????????? ????????? ??? ????????????."
                        , Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
        }
    }
}
