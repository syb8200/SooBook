package com.choonoh.soobook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.bluetooth.BluetoothAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;


public class RecordFragment extends Fragment {
    int h = 0, m = 0, s = 0;
    String hh, mm, ss;
    BluetoothSPP bt;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_record, container, false);
        bt = new BluetoothSPP(this.getContext()); //Initializing * 수정 *

        if (!bt.isBluetoothAvailable()) { //블루투스 사용 불가
            Toast.makeText(getActivity().getApplicationContext() //* 수정 *
                    , "블루투스를 사용할 수 없습니다."
                    , Toast.LENGTH_SHORT).show();
            getActivity().finish(); //* 수정 *
        }

        // ------------------------------ 데이터 수신부 ----------------------------- //
        bt.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() { //데이터 수신
            public void onDataReceived(byte[] data, String message) {
                TextView hour = getView().findViewById(R.id.hour);  //* 수정 *
                TextView minute = getView().findViewById(R.id.minute);            //* 수정 *
                TextView second = getView().findViewById(R.id.second);            //* 수정 *

                String[] array = message.split(",");
                int measured = Integer.parseInt(array[0]);

                s = measured;
                if(s == 60)
                    m += 1;
                if(m == 60)
                    h += 1;

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

        // ------------------------------ 데이터 수신부 ----------------------------- //

        bt.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener() { //연결됐을 때
            public void onDeviceConnected(String name, String address) {
//                TextView isConnect = getActivity().findViewById(R.id.isConnect);
//                isConnect.setText(R.string.yes_connect);
                Toast.makeText(getActivity().getApplicationContext()
                        , name + "와 연결되었습니다."
                        , Toast.LENGTH_SHORT).show();
            }

            public void onDeviceDisconnected() { //연결해제
//                TextView isConnect = getActivity().findViewById(R.id.isConnect);
//                isConnect.setText(R.string.not_connect);
                Toast.makeText(getActivity().getApplicationContext()
                        , "연결이 해제되었습니다", Toast.LENGTH_SHORT).show();
            }

            public void onDeviceConnectionFailed() { //연결실패
//                TextView isConnect = getActivity().findViewById(R.id.isConnect);
//                isConnect.setText(R.string.not_connect);
                Toast.makeText(getActivity().getApplicationContext()
                        , "연결할 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        Button btnConnect = rootView.findViewById(R.id.btnConnect); //연결시도
        btnConnect.setOnClickListener(v -> {
            if (bt.getServiceState() == BluetoothState.STATE_CONNECTED) {
                bt.disconnect();
            } else {
                Intent intent = new Intent(getActivity().getApplicationContext(), DeviceList.class);
                startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
            }
        });

        return rootView;
    }
    public void onDestroy() {
        super.onDestroy();
        bt.stopService(); //블루투스 중지
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
                        , "블루투스를 사용할 수 없습니다."
                        , Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
        }
    }
}
