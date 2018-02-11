package com.lixianjie.goodssearcher.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/**
 * @Author lixianjie
 * @Des TODO
 * @Time 2017/1/30/0030
 */

public class CustomRelativeLayout extends RelativeLayout {
    public CustomRelativeLayout(Context context)
    {
        this(context, null);
    }

    public CustomRelativeLayout(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public CustomRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {


        return super.onTouchEvent(event);
    }
}
