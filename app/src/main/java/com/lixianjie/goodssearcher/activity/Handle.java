package com.lixianjie.goodssearcher.activity;

import android.graphics.Bitmap;
import android.os.Handler;

import com.google.zxing.Result;
import com.mining.app.zxing.view.ViewfinderView;

/**
 * @Author lixianjie
 * @Des TODO
 * @Time 2017/1/26/0026
 */

public interface Handle {
    Handler getHandler();

    void drawViewfinder();

    void handleDecode(Result result, Bitmap barcode);

    ViewfinderView getViewfinderView();
}
