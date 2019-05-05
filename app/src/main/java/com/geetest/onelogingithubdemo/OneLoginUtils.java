package com.geetest.onelogingithubdemo;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;

import com.geetest.onelogin.OneLoginHelper;
import com.geetest.onelogin.OneLoginThemeConfig;
import com.geetest.onelogin.listener.AbstractOneLoginListener;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by 谷闹年 on 2019/4/1.
 * OneLogin工具类
 */
public class OneLoginUtils {

    /**
     * 后台配置的服务校验接口
     */
    public static final String CHECK_PHONE_URL = "";

    /**
     * 后台申请的 oneLogin ID
     */
    public static final String APP_ID = "";

    /**
     * 返回状态为200则表示成功
     */
    public static final int ONE_LOGIN_SUCCESS_STATUS = 200;

    /**
     * 日志 TAG
     */
    public static final String TAG = "OneLogin";


    private Handler backHandler;
    private Handler mainHandler = new Handler(Looper.getMainLooper());
    private Context context;
    private OneLoginResult oneLoginResult;

    public OneLoginUtils(Context context, OneLoginResult oneLoginResult) {
        this.context = context;
        this.oneLoginResult = oneLoginResult;
        HandlerThread handlerThread = new HandlerThread("oneLogin-demo");
        handlerThread.start();
        backHandler = new Handler(handlerThread.getLooper());
    }

    /**
     * 初始化 需在 <p>onCreate</p> 方法内使用
     */
    public void oneLoginInit() {
        OneLoginHelper.with().init(context);
    }

    /**
     * 关闭 需在页面关闭时候调用
     */
    public void oneLoginCancel() {
        OneLoginHelper.with().cancel();
    }

    /**
     * 预取号接口
     */
    public void oneLoginPreGetToken() {
        OneLoginHelper.with().preGetToken(APP_ID, 5000, new AbstractOneLoginListener() {
            @Override
            public void onResult(JSONObject jsonObject) {
                oneLoginResult.onResult();
                Log.i(TAG, "预取号结果为：" + jsonObject.toString());
                try {
                    int status = jsonObject.getInt("status");
                    if (status == ONE_LOGIN_SUCCESS_STATUS) {
                        oneLoginRequestToken();
                    } else {
                        ToastUtils.toastMessage(context, "预取号失败");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 取号接口
     */
    private void oneLoginRequestToken() {
        OneLoginHelper.with().requestToken(new OneLoginThemeConfig.Builder().setLogoImgPath("one_login_geetest_logo").build(), new AbstractOneLoginListener() {
            @Override
            public void onResult(final JSONObject jsonObject) {
                Log.i(TAG, "取号结果为：" + jsonObject.toString());
                try {
                    int status = jsonObject.getInt("status");
                    if (status == ONE_LOGIN_SUCCESS_STATUS) {
                        final String process_id = jsonObject.getString("process_id");
                        final String token = jsonObject.getString("token");
                        oneLoginResult.startVerify();
                        backHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                verify(process_id, token);
                            }
                        });
                    } else {
                        ToastUtils.toastMessage(context, "取号失败");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void verify(String id, String token) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("process_id", id);
            jsonObject.put("token", token);
            jsonObject.put("id_2_sign", APP_ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String result = HttpUtils.requestNetwork(CHECK_PHONE_URL, jsonObject);
        Log.i(TAG, "校验结果为:" + result);
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                oneLoginResult.endVerify();
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    int status = jsonObject1.getInt("status");
                    if (status == ONE_LOGIN_SUCCESS_STATUS) {
                        context.startActivity(new Intent(context, SuccessActivity.class));
                    } else {
                        ToastUtils.toastMessage(context, "校验失败");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }
}
