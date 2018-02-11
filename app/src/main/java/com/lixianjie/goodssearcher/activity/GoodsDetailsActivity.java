package com.lixianjie.goodssearcher.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.lixianjie.goodssearcher.R;
import com.lixianjie.goodssearcher.db.GoodsBean;
import com.lixianjie.goodssearcher.ui.view.GoodsDetailsItem;
import com.lixianjie.goodssearcher.util.ToastUitl;

import java.io.Serializable;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * @Author lixianjie
 * @Des TODO
 * @Time 2017/1/26/0026
 */

public class GoodsDetailsActivity extends Activity {

    private GoodsBean mGoodsDbBean;
    private GoodsDetailsItem mName;
    private GoodsDetailsItem mBarCode;
    private GoodsDetailsItem mBrand;
    private GoodsDetailsItem mBuyInPrice;
    private GoodsDetailsItem mBuyInUnitPrice;
    private GoodsDetailsItem mDesc;
    private GoodsDetailsItem mStandard;
    private GoodsDetailsItem mRetailPrice;
    private Button mEditMode;
    private Button mContinueScan;
    private Button mExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_details);
        init();
        initView();
        initEvent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mName.requestFocus();
    }

    private void init() {
        Serializable goodsDbBean = getIntent().getSerializableExtra("GoodsBean");
        if (goodsDbBean != null) {
            if (goodsDbBean instanceof GoodsBean) {
                mGoodsDbBean = (GoodsBean) goodsDbBean;
            }
        }
    }

    private void initView() {
        mName = (GoodsDetailsItem) findViewById(R.id.gdi_name);
        mBarCode = (GoodsDetailsItem) findViewById(R.id.gdi_barcode);
        mBrand = (GoodsDetailsItem) findViewById(R.id.gdi_brand);
        mBuyInPrice = (GoodsDetailsItem) findViewById(R.id.gdi_buy_in_price);
        mBuyInUnitPrice = (GoodsDetailsItem) findViewById(R.id.gdi_buy_in_unit_price);
        mDesc = (GoodsDetailsItem) findViewById(R.id.gdi_desc);
        mStandard = (GoodsDetailsItem) findViewById(R.id.gdi_standard);
        mRetailPrice = (GoodsDetailsItem) findViewById(R.id.gdi_retail_price);
        mEditMode = (Button) findViewById(R.id.bt_edit);
        mContinueScan = (Button) findViewById(R.id.bt_continue);
        mExit = (Button) findViewById(R.id.bt_return);

        if (mGoodsDbBean != null) {
            mName.setItemDetails(mGoodsDbBean.getName());
            mBarCode.setItemDetails(mGoodsDbBean.getBarCode());
            mBrand.setItemDetails(mGoodsDbBean.getBrand());
            mBuyInPrice.setItemDetails(mGoodsDbBean.getBuyInPrice());
            mBuyInUnitPrice.setItemDetails(mGoodsDbBean.getBuyInUnitPrice());
            mDesc.setItemDetails(mGoodsDbBean.getDesc());
            mStandard.setItemDetails(mGoodsDbBean.getStandard());
            mRetailPrice.setItemDetails(mGoodsDbBean.getRetailPrice());
        }
    }

    private void initEvent() {
        mExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mContinueScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GoodsDetailsActivity.this, QueryGoodsActivity.class));
                finish();
            }
        });

        mEditMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGoodsDbBean.setBrand(mBrand.getDetails());
                mGoodsDbBean.setName(mName.getDetails());
                mGoodsDbBean.setBuyInPrice(mBuyInPrice.getDetails());
                mGoodsDbBean.setDesc(mDesc.getDetails());
                mGoodsDbBean.setBuyInUnitPrice(mBuyInUnitPrice.getDetails());
                mGoodsDbBean.setStandard(mStandard.getDetails());
//                boolean update = new GoodsDao().update(mGoodsDbBean);
//                if (update) {
//                    ToastUitl.showShort("保存成功");
//                } else {
//                    ToastUitl.showShort("保存失敗");
//                }

                new BmobQuery<GoodsBean>().addWhereEqualTo("barCode", mGoodsDbBean.getBarCode()).findObjects(new FindListener<GoodsBean>() {
                    @Override
                    public void done(List<GoodsBean> query, BmobException e) {
                        if (query.size() > 0) {
                            GoodsBean goodsDbBean = query.get(0);
                            mGoodsDbBean.update(goodsDbBean.getObjectId(), new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if (e == null) {
                                        ToastUitl.showShort("保存成功");
                                        Log.e("bmob", "更新成功");
                                    } else {
                                        ToastUitl.showShort("保存失败");
                                        Log.e("bmob", "更新失败：" + e.getMessage() + "," + e.getErrorCode());
                                    }
                                }
                            });
                            return;
                        }
                        ToastUitl.showShort("保存失败");
                    }
                });
            }
        });
    }
}
