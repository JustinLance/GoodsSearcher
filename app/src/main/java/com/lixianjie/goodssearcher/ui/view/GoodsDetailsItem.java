package com.lixianjie.goodssearcher.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lixianjie.goodssearcher.R;

import static com.MyApplication.mContext;

/**
 * @Author lixianjie
 * @Des 商品詳情條目
 * @Time 2017/1/26/0026
 */

public class GoodsDetailsItem extends LinearLayout {
    private TextView mTvTitle;
    private EditText mEtDetails;

    public GoodsDetailsItem(Context context)
    {
        this(context, null);
    }

    public GoodsDetailsItem(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public GoodsDetailsItem(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs)
    {
        setOrientation(LinearLayout.VERTICAL);
        LayoutInflater.from(getContext()).inflate(R.layout.layout_goods_details, this, true);
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.GoodsDetailsItem);
        String itemTitle = typedArray.getString(R.styleable.GoodsDetailsItem_itemTitle);
        String itemDetails = typedArray.getString(R.styleable.GoodsDetailsItem_itemDetails);
        boolean isEdit = typedArray.getBoolean(R.styleable.GoodsDetailsItem_isEdit, true);
        typedArray.recycle();
        mTvTitle = (TextView) findViewById(R.id.tv_item_title);
        mEtDetails = (EditText) findViewById(R.id.et_details);
        mTvTitle.setText(itemTitle);
        if (!isEdit)
        {
            enterEditMode(isEdit);
        }
    }

    public void enterEditMode(boolean isEdit)
    {
        mEtDetails.setClickable(isEdit);
        mEtDetails.setFocusable(isEdit);
        mEtDetails.setEnabled(isEdit);
    }

    public void setItemDetails(String details)
    {
        mEtDetails.setText(details);
    }

    public void setItemTitle(String title)
    {
        mTvTitle.setText(title);
    }

    public String getDetails()
    {
        return mEtDetails.getText().toString();
    }
}
