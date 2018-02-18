package com;

import android.app.Application;
import android.content.Context;
import android.content.Intent;


import com.xianjielee.goodssearcher.QRCodeSender;

import cn.bmob.v3.Bmob;


/**
 * @Author lixianjie
 * @Des TODO
 * @Time 2017/1/25/0025
 */

public class App extends Application {

    public static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        //第一：默认初始化
        Bmob.initialize(this, "02dc3a0b726ba6cb30474a0303451267");
        startService(new Intent(this, QRCodeSender.class));
    }
}
