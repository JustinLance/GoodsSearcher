package com.lixianjie.goodssearcher.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lixianjie.goodssearcher.R;

/**
 * @Author lixianjie
 * @Des 輸入商品屬性
 * @Time 2017/1/25/0025
 */

public class InputGoodsAttribute extends LinearLayout {
    private Context mContext;
    private TextView mTvTitle;
    private EditText mEtInput;

    public InputGoodsAttribute(Context context)
    {
        this(context, null);
    }

    public InputGoodsAttribute(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public InputGoodsAttribute(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init(attrs);
    }

    private void init(AttributeSet attrs)
    {
        setOrientation(LinearLayout.VERTICAL);
        LayoutInflater.from(getContext()).inflate(R.layout.layout_goods_input, this, true);
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.InputGoodsAttribute);
        String title = typedArray.getString(R.styleable.InputGoodsAttribute_inputTitle);
        String inputHint = typedArray.getString(R.styleable.InputGoodsAttribute_inputHint);
        typedArray.recycle();
        mTvTitle = (TextView) findViewById(R.id.tv_item_title);
        mEtInput = (EditText) findViewById(R.id.et_input);

        mTvTitle.setText(title);
        mEtInput.setHint(inputHint);
    }

    public String getInputText(){
        return mEtInput.getText().toString();
    }

    public void setEditTextString(String string)
    {
        mEtInput.setText(string);
    }
}
