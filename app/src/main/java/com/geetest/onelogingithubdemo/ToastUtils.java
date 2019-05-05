package com.geetest.onelogingithubdemo;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by 谷闹年 on 2019/4/1.
 */
public class ToastUtils {

    public static void toastMessage(Context context, String param) {
        Toast.makeText(context, param, Toast.LENGTH_SHORT).show();
    }
}
