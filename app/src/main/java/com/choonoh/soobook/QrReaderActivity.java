package com.choonoh.soobook;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

public class QrReaderActivity extends AppCompatActivity implements DecoratedBarcodeView.TorchListener {
    private CaptureManager m_captureManager;
    ImageButton back_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_reader);

        back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(v -> {
            Intent intent = new Intent(QrReaderActivity.this, Home.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        findViewById(R.id.btn_skip_qr).setOnClickListener(view -> {
            Intent intent = new Intent();
            setResult(Activity.RESULT_OK, intent);
            finish();

            //키보드 올리기
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        });
        DecoratedBarcodeView decoratedBarcodeView = findViewById(R.id.qr_reader_view);
        decoratedBarcodeView.setTorchListener(this);
        m_captureManager = new CaptureManager(this, decoratedBarcodeView);
        m_captureManager.initializeFromIntent(getIntent(), savedInstanceState);
        m_captureManager.decode();
    }


    @Override
    public void onTorchOn() {
    }
    @Override
    public void onTorchOff() {
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        m_captureManager.onSaveInstanceState(outState);
    }
    @Override
    protected void onPause() {
        super.onPause();
        m_captureManager.onPause();
    }
    @Override
    protected void onResume() {
        super.onResume();
        m_captureManager.onResume();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        m_captureManager.onDestroy();
    }
}