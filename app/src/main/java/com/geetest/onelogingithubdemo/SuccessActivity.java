package com.geetest.onelogingithubdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class SuccessActivity extends AppCompatActivity {
    private ImageView imageView;
    private GTMMyView gtmlayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);
        getWindow().setBackgroundDrawable(null);
        findViewById(R.id.btn_back_success).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
        imageView = (ImageView) findViewById(R.id.gtm_back_iv);
        gtmlayout = (GTMMyView) findViewById(R.id.gtm_view);
        gtmlayout.start();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
