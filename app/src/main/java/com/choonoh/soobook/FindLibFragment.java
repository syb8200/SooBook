package com.choonoh.soobook;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.NaverMapSdk;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.util.FusedLocationSource;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class FindLibFragment extends Fragment implements OnMapReadyCallback {

    TextView lib_name, lib_type, lib_number;
//    EditText et_search_text;
//    ImageButton search_btn;
    LinearLayout detail_view;

    boolean marker_exist = false;
    int only_zero;
    int marker_len, final_marker;
//    int cur_or_se;
    double latitude, longitude;
    String current_location_si, current_location_dong;
//    String  search_location;

    ArrayList<Library> list;
    Marker[] markers;
    Library library;
    AlertDialog dialog;
    String[][] library_info;
    // name, close_day, week(open, close), sat(open, close) holiday(open, close), bookNum, bookPosNum, borPosDay, address, number, homePageUrl, dataTime

    private FusedLocationSource mLocationSource;
    private NaverMap naverMap;

    private static final int PERMISSION_REQUEST_CODE = 100;
    private static final String[] PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    public static FindLibFragment newInstance() {
        return new FindLibFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_findlib, container, false);

        NaverMapSdk.getInstance(rootView.getContext()).setClient(
                new NaverMapSdk.NaverCloudPlatformClient("vjckuvmnki"));

        FragmentManager fm = getChildFragmentManager();
        MapFragment mapFragment = (MapFragment)fm.findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            fm.beginTransaction().add(R.id.map, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);

        lib_name = rootView.findViewById(R.id.name);
        lib_type = rootView.findViewById(R.id.type);
        lib_number = rootView.findViewById(R.id.number);
//        et_search_text = rootView.findViewById(R.id.search_text);
//        search_btn = rootView.findViewById(R.id.search_btn);
        detail_view = rootView.findViewById(R.id.detail_view);

        mLocationSource = new FusedLocationSource(this, PERMISSION_REQUEST_CODE);

//        search_btn.setOnClickListener(v -> {
////            search_location = et_search_text.getText().toString();
////            cur_or_se = 1;
////            Log.e(this.getClass().getName(), search_location);
////            if(search_location.equals("")) {
////                cur_or_se = 0;
//                GeoCoding geoCoding = new GeoCoding();
//                geoCoding.execute();
//                if(marker_exist) {
//                    marker_exist = false;
//                    for(int i = 0; i < markers.length; i++) {
//                        markers[i].setMap(null);
//                        markers[i] = null;
//                    }
//                }
//                marker_len = 0;
//                list = new ArrayList<>();
//                MyAsyncTask myAsyncTask = new MyAsyncTask();
//                myAsyncTask.execute();
////            } else {
////                if(marker_exist) {
////                    marker_exist = false;
////                    for(int i = 0; i < markers.length; i++) {
////                        markers[i].setMap(null);
////                        markers[i] = null;
////                    }
////                }
////                marker_len = 0;
////                list = new ArrayList<>();
////                MyAsyncTask myAsyncTask = new MyAsyncTask();
////                myAsyncTask.execute();
////            }
//        });
        detail_view.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), DetailView.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("name", library_info[0][final_marker]);
            intent.putExtra("type", library_info[1][final_marker]);
            intent.putExtra("close_day", library_info[2][final_marker]);
            intent.putExtra("weekOpenTime", library_info[3][final_marker]);
            intent.putExtra("weekCloseTime", library_info[4][final_marker]);

            intent.putExtra("satOpenTime", library_info[5][final_marker]);
            intent.putExtra("satCloseTime", library_info[6][final_marker]);
            intent.putExtra("holidayOpenTime", library_info[7][final_marker]);
            intent.putExtra("holidayCloseTime", library_info[8][final_marker]);

            intent.putExtra("bookNum", library_info[9][final_marker]);
            intent.putExtra("bookPosNum", library_info[10][final_marker]);
            intent.putExtra("borPosDay", library_info[11][final_marker]);
            intent.putExtra("address", library_info[12][final_marker]);

            intent.putExtra("number", library_info[13][final_marker]);
            intent.putExtra("homePageUrl", library_info[14][final_marker]);
            intent.putExtra("dataTime", library_info[15][final_marker]);
            startActivity(intent);
        });
        return rootView;
    }
    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        ActivityCompat.requestPermissions(this.getActivity(), PERMISSIONS, PERMISSION_REQUEST_CODE);
        only_zero = 0;
        this.naverMap = naverMap;
        this.naverMap.setLocationSource(mLocationSource);
        this.naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
        this.naverMap.addOnLocationChangeListener(location -> {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            if(only_zero == 0) {
                only_zero++;
//                cur_or_se = 0;
                GeoCoding geoCoding = new GeoCoding();
                geoCoding.execute();
                if(marker_exist) {
                    marker_exist = false;
                    for(int i = 0; i < markers.length; i++) {
                        markers[i].setMap(null);
                        markers[i] = null;
                    }
                }
                marker_len = 0;
                list = new ArrayList<>();
                MyAsyncTask myAsyncTask = new MyAsyncTask();
                myAsyncTask.execute();
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
            }
        }
    }
    private class MyAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            Log.e(this.getClass().getName(), String.valueOf(latitude));
            Log.e(this.getClass().getName(), String.valueOf(longitude));

//            Log.e(this.getClass().getName(), "0 or 1 : " + cur_or_se);
            String requestUrl = "";
            requestUrl = "http://api.data.go.kr/openapi/tn_pubr_public_lbrry_api"
                    + "?serviceKey=bCJdW6RD4qr5ygWtvTicA5sgPMvnvcpfzA3vXZj2k8HZ66cnR7OpoV24WdJgJMv7e3x2gu2swtG%2Bv84490FuAw%3D%3D"
                    + "&pageNo=0&numOfRows=100&type=xml" + "&ctprvnNm=" + current_location_si + "&signguNm=" + current_location_dong;
//                switch (cur_or_se) {
//                    case 0:
//                        Log.e(this.getClass().getName(), current_location_si + " " + current_location_dong);
//                        requestUrl = "http://api.data.go.kr/openapi/tn_pubr_public_lbrry_api"
//                                + "?serviceKey=bCJdW6RD4qr5ygWtvTicA5sgPMvnvcpfzA3vXZj2k8HZ66cnR7OpoV24WdJgJMv7e3x2gu2swtG%2Bv84490FuAw%3D%3D"
//                                + "&pageNo=0&numOfRows=100&type=xml" + "&ctprvnNm=" + current_location_si + "&signguNm=" + current_location_dong;
//                        break;
//                    case 1:
//                        Log.e(this.getClass().getName(), search_location);
//                        requestUrl = "http://api.data.go.kr/openapi/tn_pubr_public_lbrry_api"
//                                + "?serviceKey=bCJdW6RD4qr5ygWtvTicA5sgPMvnvcpfzA3vXZj2k8HZ66cnR7OpoV24WdJgJMv7e3x2gu2swtG%2Bv84490FuAw%3D%3D"
//                                + "&pageNo=0&numOfRows=100&type=xml" + "&signguNm=" + search_location;
//                        break;
//                }
                try {
                boolean name = false;
                boolean type = false;
                boolean closeDay = false;
                boolean weekOpenTime = false;
                boolean weekCloseTime = false;

                boolean satOpenTime = false;
                boolean satCloseTime = false;
                boolean holidayOpenTime = false;
                boolean holidayCloseTime = false;

                boolean bookNum = false;
                boolean borPosNum = false;
                boolean borPosDay = false;
                boolean address = false;

                boolean number = false;
                boolean homePageUrl = false;
                boolean dataTime = false;

                boolean latitude_tf = false;
                boolean longitude_tf = false;

                Log.e(this.getClass().getName(), requestUrl);
                URL url = new URL(requestUrl);
                InputStream is = url.openStream();
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser parser = factory.newPullParser();
                parser.setInput(new InputStreamReader(is, "UTF-8"));

                int eventType = parser.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    switch (eventType) {
                        case XmlPullParser.START_DOCUMENT:
                            break;
                        case XmlPullParser.END_DOCUMENT:
                            break;
                        case XmlPullParser.END_TAG:
                            if (parser.getName().equals("item") && library != null) {
                                if (marker_len < 20) {
                                    marker_len++;
                                    list.add(library);
                                }
                            }
                            break;
                        case XmlPullParser.START_TAG:
                            if (parser.getName().equals("item")) {
                                library = new Library();
                            }
                            if (parser.getName().equals("lbrryNm")) name = true;
                            if (parser.getName().equals("lbrrySe")) type = true;
                            if (parser.getName().equals("closeDay")) closeDay = true;
                            if (parser.getName().equals("weekdayOperOpenHhmm")) weekOpenTime = true;
                            if (parser.getName().equals("weekdayOperColseHhmm")) weekCloseTime = true;

                            if (parser.getName().equals("satOperOperOpenHhmm")) satOpenTime = true;
                            if (parser.getName().equals("satOperCloseHhmm")) satCloseTime = true;
                            if (parser.getName().equals("holidayOperOpenHhmm")) holidayOpenTime = true;
                            if (parser.getName().equals("holidayCloseOpenHhmm")) holidayCloseTime = true;

                            if (parser.getName().equals("bookCo")) bookNum = true;
                            if (parser.getName().equals("lonCo")) borPosNum = true;
                            if (parser.getName().equals("lonDaycnt")) borPosDay = true;
                            if (parser.getName().equals("rdnmadr")) address = true;

                            if (parser.getName().equals("phoneNumber")) number = true;
                            if (parser.getName().equals("homepageUrl")) homePageUrl = true;
                            if (parser.getName().equals("referenceDate")) dataTime = true;

                            if (parser.getName().equals("latitude")) latitude_tf = true;
                            if (parser.getName().equals("longitude")) longitude_tf = true;
                            break;
                        case XmlPullParser.TEXT:
                            if (name) {
                                library.setName(parser.getText());
                                name = false;
                            } else if (type) {
                                library.setType(parser.getText());
                                type = false;
                            } else if (closeDay) {
                                library.setCloseDay(parser.getText());
                                closeDay = false;
                            } else if (weekOpenTime) {
                                library.setWeekOpenTime(parser.getText());
                                weekOpenTime = false;
                            } else if (weekCloseTime) {
                                library.setWeekCloseTime(parser.getText());
                                weekCloseTime = false;
                            } else if (satOpenTime) {
                                library.setSatOpenTime(parser.getText());
                                satOpenTime = false;
                            } else if (satCloseTime) {
                                library.setSatCloseTime(parser.getText());
                                satCloseTime = false;
                            } else if (holidayOpenTime) {
                                library.setHolidayOpenTime(parser.getText());
                                holidayOpenTime = false;
                            } else if (holidayCloseTime) {
                                library.setHolidayCloseTime(parser.getText());
                                holidayCloseTime = false;
                            } else if (bookNum) {
                                library.setBookNum(parser.getText());
                                bookNum = false;
                            } else if (borPosNum) {
                                library.setBorPosNum(parser.getText());
                                borPosNum = false;
                            } else if (borPosDay) {
                                library.setBorPosDay(parser.getText());
                                borPosDay = false;
                            } else if (address) {
                                library.setAddress(parser.getText());
                                address = false;
                            } else if (number) {
                                library.setNumber(parser.getText());
                                number = false;
                            } else if (homePageUrl) {
                                library.setHomePageUrl(parser.getText());
                                homePageUrl = false;
                            } else if (latitude_tf) {
                                library.setLatitude(parser.getText());
                                latitude_tf = false;
                            } else if (longitude_tf) {
                                library.setLongitude(parser.getText());
                                longitude_tf = false;
                            } else if (dataTime) {
                                library.setDataTime(parser.getText());
                                dataTime = false;
                            }
                            break;
                    }
                    eventType = parser.next();
                }
            } catch (Exception e) { Log.e(this.getClass().getName(), "에러!!! : " + e); }
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            Log.e(this.getClass().getName(), "len : " + marker_len);
            if(marker_len != 0) {
                super.onPostExecute(s);
                Log.e(this.getClass().getName(), "onPostExecute len : " + marker_len);
                Log.e(this.getClass().getName(), "onPostExecute list_size() : " + list.size());

                library_info = new String[16][marker_len];
                markers = new Marker[marker_len];
                double latitude_marker = 0;
                double longitude_marker = 0;
                marker_exist = true;
                for (int i = 0; i < marker_len; i++) {
// name, type, close_day, weekOpenTime, weekCloseTime, satOpenTime, satCloseTime, holidayOpenTime, holidayCloseTime
// bookNum, bookPosNum, borPosDay, address, number, homePageUrl, dataTime
                    isEmpty(list.get(i).getName(), 0, i);
                    isEmpty(list.get(i).getType(), 1, i);
                    isEmpty(list.get(i).getCloseDay(), 2, i);
                    isEmpty(list.get(i).getWeekOpenTime(), 3, i);
                    isEmpty(list.get(i).getWeekCloseTime(), 4, i);

                    isEmpty(list.get(i).getSatOpenTime(), 5, i);
                    isEmpty(list.get(i).getSatCloseTime(), 6, i);
                    isEmpty(list.get(i).getHolidayOpenTime(), 7, i);
                    isEmpty(list.get(i).getHolidayCloseTime(), 8, i);

                    isEmpty(list.get(i).getBookNum(), 9, i);
                    isEmpty(list.get(i).getBorPosNum(), 10, i);
                    isEmpty(list.get(i).getBorPosDay(), 11, i);
                    isEmpty(list.get(i).getAddress(), 12, i);

                    isEmpty(list.get(i).getNumber(), 13, i);
                    isEmpty(list.get(i).getHomePageUrl(), 14, i);
                    isEmpty(list.get(i).getDataTime(), 15, i);

                    latitude_marker = Double.parseDouble(list.get(i).getLatitude());
                    longitude_marker = Double.parseDouble(list.get(i).getLongitude());
                    Marker marker = new Marker();
                    markers[i] = marker;
                    setMarker(markers[i], latitude_marker, longitude_marker);
                    final int finalI = i;
                    marker.setOnClickListener(overlay -> {
                        Log.e(this.getClass().getName(), library_info[0][finalI]);
                        lib_name.setText(library_info[0][finalI]);
                        lib_type.setText(library_info[1][finalI]);
                        lib_number.setText(library_info[13][finalI]);
                        detail_view.setVisibility(View.VISIBLE);
                        final_marker = finalI;
                        return true;
                    });
                }
                Log.e(this.getClass().getName(), "파싱 후 카메라 옮기기 전 lat, long : " + latitude_marker + ", " + longitude_marker);
                try {
                    CameraUpdate cameraUpdate = CameraUpdate.scrollTo(new LatLng(latitude_marker, longitude_marker));
                    naverMap.moveCamera(cameraUpdate);
                    Toast toast = Toast.makeText(getContext(), "총" + marker_len + "개의 도서관을 찾았어요.", Toast.LENGTH_SHORT); toast.show();
                    Handler handler = new Handler();
                    handler.postDelayed(toast::cancel, 1500);
                } catch (Exception e) {
                    Log.e(this.getClass().getName(), "검색 구냥 넘어가기");
                }
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("주변 도서관이 검색되지 않습니다.");
                builder.setPositiveButton("확인", null);
                dialog = builder.create();
                dialog.show();
            }
        }
        private  void isEmpty(String str, int num, int i) {
            if(str == null)
                library_info[num][i] = "";
            else
                library_info[num][i] = str;
        }
        private void setMarker(Marker marker,  double lat, double lng) {
            marker.setWidth(80);
            marker.setHeight(80);
            marker.setIconPerspectiveEnabled(true);     //원근감 표시
            marker.setIcon(OverlayImage.fromResource(R.drawable.marker));   //아이콘 지정
            marker.setAlpha(0.9f);  //마커의 투명도
            marker.setPosition(new LatLng(lat, lng));   //마커 위치
            marker.setMap(naverMap);   //마커 표시
        }
    }
    private class GeoCoding extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            Log.e(this.getClass().getName(), String.valueOf(latitude));
            Log.e(this.getClass().getName(), String.valueOf(longitude));
            if (latitude == 0 && longitude == 0) {
                Handler mHandler = new Handler(Looper.getMainLooper());
                mHandler.postDelayed(() -> {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setMessage("주변 도서관을 찾을 수 없습니다.");
                        builder.setPositiveButton("확인", null);
                        dialog = builder.create();
                        dialog.show();
                }, 0);

            } else {
                String requestUrl = "https://naveropenapi.apigw.ntruss.com/map-reversegeocode/v2/gc?request=coordsToaddr" +
                        "&coords=" + longitude + "," + latitude +
                        "&sourcecrs=epsg:4326&orders=admcode,legalcode,addr,roadaddr&output=xml" +
                        "&X-NCP-APIGW-API-KEY-ID=vjckuvmnki&X-NCP-APIGW-API-KEY=lpGVBI7LLYdJFSw4fo080h60axwkh5VkGlQdrZ4L";
                Log.e(this.getClass().getName(), requestUrl);
                try {
                    boolean area1 = false;
                    boolean area2 = false;
                    boolean name = false;

                    URL url = new URL(requestUrl);
                    InputStream is = url.openStream();
                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                    XmlPullParser parser = factory.newPullParser();
                    parser.setInput(new InputStreamReader(is, "UTF-8"));

                    int eventType = parser.getEventType();
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        switch (eventType) {
                            case XmlPullParser.START_DOCUMENT:
                                break;
                            case XmlPullParser.END_DOCUMENT:
                                break;
                            case XmlPullParser.END_TAG:
                                break;
                            case XmlPullParser.START_TAG:
                                if (parser.getName().equals("area1")) area1 = true;
                                if (parser.getName().equals("area2")) area2 = true;
                                if (parser.getName().equals("name")) name = true;
                                break;
                            case XmlPullParser.TEXT:
                                if(area1 && name) {
                                    current_location_si = parser.getText();
                                    area1 = false;
                                    name = false;
                                }
                                if (area2 && name) {
                                    current_location_dong = parser.getText();
                                    area2 = false;
                                    name = false;
                                }
                                break;
                        }
                        eventType = parser.next();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
}

