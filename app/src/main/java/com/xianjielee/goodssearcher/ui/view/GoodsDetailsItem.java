package com.xianjielee.goodssearcher.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xianjielee.goodssearcher.R;

import static com.MyApplication.mContext;

/**
 * @Author lixianjie
 * @Des 商品詳情條目
 * @Time 2017/1/26/0026
 */

public class GoodsDetailsItem extends LinearLayout {
    private static final String TAG = GoodsDetailsItem.class.getSimpleName();
    private TextView mTvTitle;
    private EditText mEtDetails;
    private ImageButton controlEncrypt;

    public GoodsDetailsItem(Context context) {
        this(context, null);
    }

    public GoodsDetailsItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GoodsDetailsItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        setOrientation(LinearLayout.VERTICAL);
        LayoutInflater.from(getContext()).inflate(R.layout.layout_goods_details, this, true);
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.GoodsDetailsItem);
        String itemTitle = typedArray.getString(R.styleable.GoodsDetailsItem_itemTitle);
        String itemDetails = typedArray.getString(R.styleable.GoodsDetailsItem_itemDetails);
        boolean isEdit = typedArray.getBoolean(R.styleable.GoodsDetailsItem_isEdit, true);
        int inputType = typedArray.getInt(R.styleable.GoodsDetailsItem_android_inputType, EditorInfo.TYPE_NULL);
        boolean contentEncrypt = typedArray.getBoolean(R.styleable.GoodsDetailsItem_detailsContentEncrypt, false);
        typedArray.recycle();
        mTvTitle = (TextView) findViewById(R.id.tv_item_title);
        mEtDetails = (EditText) findViewById(R.id.et_details);
        if (inputType != EditorInfo.TYPE_NULL) {
            controlEncrypt = (ImageButton) findViewById(R.id.ib_control_details_encrypt);
        }
        if (contentEncrypt) {
            controlDetailsContentEncrypt(true);
            controlEncrypt.setVisibility(VISIBLE);
            controlEncrypt.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getActionMasked()) {
                        case MotionEvent.ACTION_DOWN:
                            controlDetailsContentEncrypt(false);
                            break;
                        case MotionEvent.ACTION_UP:
                            controlDetailsContentEncrypt(true);
                            break;
                    }
                    return false;
                }
            });
        }
        mTvTitle.setText(itemTitle);
        if (!isEdit) {
            enterEditMode(isEdit);
        }
    }

    public void enterEditMode(boolean isEdit) {
        mEtDetails.setClickable(isEdit);
        mEtDetails.setFocusable(isEdit);
        mEtDetails.setEnabled(isEdit);
    }

    public void setItemDetails(String details) {
        mEtDetails.setText(details);
    }

    public void setItemTitle(String title) {
        mTvTitle.setText(title);
    }

    public String getDetails() {
        return mEtDetails.getText().toString();
    }

    public void controlDetailsContentEncrypt(boolean encrypt) {
        mEtDetails.setTransformationMethod(encrypt ? PasswordTransformationMethod.getInstance() : HideReturnsTransformationMethod.getInstance());
    }
}
