package com.geetest.onelogingithubdemo;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {
    private ImageView imageView;
    private LinearLayout linear;
    private OneLoginUtils oneLoginUtils;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        oneLoginUtils = new OneLoginUtils(this,oneLoginResult);
        oneLoginUtils.oneLoginInit();
        getWindow().setBackgroundDrawable(null);
        //拿到这个权限可以更方便的进行网关验证
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            Log.i(OneLoginUtils.TAG, "开启权限会提高成功率哟!");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
        } else {
            Log.i(OneLoginUtils.TAG, "您的权限已开启,欢迎更好的使用极验产品 oneLogin!");
        }
        findViewById(R.id.gtm_btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startOneLogin();
            }
        });
    }

    private void init() {
        imageView = (ImageView) findViewById(R.id.gtm_iv);
        linear = (LinearLayout) findViewById(R.id.gtm_ll);
        ObjectAnimator animator = ObjectAnimator.ofFloat(imageView, "translationY", 0.0f, 0.0f, 0.0f, -280.0f);
        ValueAnimator val = ValueAnimator.ofFloat(0.0f, 0.0f, 0.0f, 1.0f);
        val.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                linear.setAlpha((Float) animation.getAnimatedValue());
            }
        });
        AnimatorSet set = new AnimatorSet();
        set.setDuration(1500);
        set.playTogether(animator, val);
        set.start();
    }

    private void startOneLogin() {
        progressDialog = ProgressDialog.show(MainActivity.this, null, "验证加载中", true, true);
        oneLoginUtils.oneLoginPreGetToken();
    }

    private OneLoginResult oneLoginResult = new OneLoginResult() {
        @Override
        public void onResult() {
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
        }

        @Override
        public void startVerify() {
            if (progressDialog != null) {
                progressDialog.show();
            }
        }

        @Override
        public void endVerify() {
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        oneLoginUtils.oneLoginCancel();
    }
}
