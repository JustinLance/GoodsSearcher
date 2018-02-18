package com.xianjielee.goodssearcher.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.xianjielee.goodssearcher.R;
import com.xianjielee.goodssearcher.db.GoodsBean;
import com.xianjielee.goodssearcher.ui.view.GoodsDetailsItem;
import com.xianjielee.goodssearcher.util.ToastUitl;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import dmax.dialog.SpotsDialog;

/**
 * @Author lixianjie
 * @Des TODO
 * @Time 2017/1/26/0026
 */

public class GoodsDetailsActivity extends Activity {

    public static final int MODE_LOOK_OVER = 0;
    public static final int MODE_EDIT = 1;

    private GoodsBean mGoodsBean;

    private GoodsDetailsItem mName;
    private GoodsDetailsItem mBarCode;
    private GoodsDetailsItem mBrand;
    private GoodsDetailsItem mBuyInPrice;
    private GoodsDetailsItem mBuyInUnitPrice;
    private GoodsDetailsItem mDesc;
    private GoodsDetailsItem mStandard;
    private GoodsDetailsItem mRetailPrice;

    private Button mBtnEditMode;
    private Button mContinueScan;
    private Button mExit;
    private SpotsDialog spotsDialog;
    private int mEditMode;

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
        Parcelable goodsDbBean = getIntent().getParcelableExtra("GoodsBean");
        if (goodsDbBean != null) {
            if (goodsDbBean instanceof GoodsBean) {
                mGoodsBean = (GoodsBean) goodsDbBean;
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
        mBtnEditMode = (Button) findViewById(R.id.bt_edit);
        mContinueScan = (Button) findViewById(R.id.bt_continue);
        mExit = (Button) findViewById(R.id.bt_return);

        if (mGoodsBean != null) {
            mName.setItemDetails(mGoodsBean.getName());
            mBarCode.setItemDetails(mGoodsBean.getBarCode());
            mBrand.setItemDetails(mGoodsBean.getBrand());
            mBuyInPrice.setItemDetails(mGoodsBean.getBuyInPrice());
            mBuyInUnitPrice.setItemDetails(mGoodsBean.getBuyInUnitPrice());
            mDesc.setItemDetails(mGoodsBean.getDesc());
            mStandard.setItemDetails(mGoodsBean.getStandard());
            mRetailPrice.setItemDetails(mGoodsBean.getRetailPrice());
        }
    }

    private void initEvent() {
        mExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEditMode == MODE_EDIT) {
                    // 退出编辑模式
                    changeEditMode(false);
                    mExit.setText("退出");
                } else {
                    finish();
                }
            }
        });

        mContinueScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GoodsDetailsActivity.this, QueryGoodsActivity.class));
                finish();
            }
        });

        mBtnEditMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEditMode == MODE_LOOK_OVER) {
                    mExit.setText("退出编辑");
                    changeEditMode(true);
                } else {
                    if (mEditMode == MODE_EDIT) {
                        showLoading();
                        save();
                    }
                }
            }
        });
    }

    private void changeEditMode(boolean isEdit) {
        mEditMode = isEdit ? MODE_EDIT : MODE_LOOK_OVER;

        mName.enterEditMode(isEdit);
        mBarCode.enterEditMode(isEdit);
        mBrand.enterEditMode(isEdit);
        mBuyInPrice.enterEditMode(isEdit);
        mBuyInUnitPrice.enterEditMode(isEdit);
        mDesc.enterEditMode(isEdit);
        mStandard.enterEditMode(isEdit);
        mRetailPrice.enterEditMode(isEdit);

        mBtnEditMode.setText(isEdit ? "保存修改" : "进入商品编辑模式");
    }

    private void save() {
        hideLoading();
        spotsDialog = new SpotsDialog(GoodsDetailsActivity.this);
        spotsDialog.show();
        final GoodsBean bean = GoodsBean.copy(mGoodsBean);
        bean.setBrand(mBrand.getDetails());
        bean.setName(mName.getDetails());
        bean.setBuyInPrice(mBuyInPrice.getDetails());
        bean.setDesc(mDesc.getDetails());
        bean.setBuyInUnitPrice(mBuyInUnitPrice.getDetails());
        bean.setStandard(mStandard.getDetails());
        bean.setRetailPrice(mRetailPrice.getDetails());

//                boolean update = new GoodsDao().update(mGoodsBean);
//                if (update) {
//                    ToastUitl.showShort("保存成功");
//                } else {
//                    ToastUitl.showShort("保存失敗");
//                }

        new BmobQuery<GoodsBean>()
                .addWhereEqualTo("barCode", mGoodsBean.getBarCode())
                .addWhereEqualTo("name", mGoodsBean.getName())
                .addWhereEqualTo("standard", mGoodsBean.getStandard())
                .findObjects(new FindListener<GoodsBean>() {
                    @Override
                    public void done(List<GoodsBean> query, BmobException e) {
                        if (query.size() > 0) {
                            GoodsBean goodsDbBean = query.get(0);
                            bean.update(goodsDbBean.getObjectId(), new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    spotsDialog.dismiss();
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
                        spotsDialog.dismiss();
                        ToastUitl.showShort("保存失败");
                    }
                });
    }

    private void hideLoading() {
        if (spotsDialog != null) {
            spotsDialog.dismiss();
        }
    }

    private void showLoading() {
        if (spotsDialog != null) {
            spotsDialog.dismiss();
        }
        spotsDialog = new SpotsDialog(this);
        spotsDialog.show();
    }

    public static void go(Context context, GoodsBean goods) {
        Intent intent = new Intent(context, GoodsDetailsActivity.class);
        intent.putExtra("GoodsBean", (Parcelable) goods);
        context.startActivity(intent);
    }
}
