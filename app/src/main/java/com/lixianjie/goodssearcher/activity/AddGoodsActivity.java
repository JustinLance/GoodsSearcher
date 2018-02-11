package com.lixianjie.goodssearcher.activity;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.lixianjie.goodssearcher.R;
import com.lixianjie.goodssearcher.db.GoodsBean;
import com.lixianjie.goodssearcher.db.GoodsDao;
import com.lixianjie.goodssearcher.ui.view.InputGoodsAttribute;
import com.lixianjie.goodssearcher.util.ToastUitl;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;


public class AddGoodsActivity extends Activity {

    private final static int SCANNIN_GREQUEST_CODE = 1;
    private static final String TAG = AddGoodsActivity.class.getSimpleName();

    private EditText mInputBarCode;
    private Button mScanBarcode;
    private ImageView mScanBarCodeBitmap;
    private InputGoodsAttribute mInputName;
    private InputGoodsAttribute mInputBrand;
    private InputGoodsAttribute mInputBuyInPrice;
    private InputGoodsAttribute mInputBuyInUnitPrice;
    private InputGoodsAttribute mInputStandard;
    private InputGoodsAttribute mInputDesc;
    private InputGoodsAttribute mInputRetailPrice;
    private Button mBtSave;
    private GoodsDao mGoodsDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_goods);
        init();
        initView();

        initEvent();
        //点击按钮跳转到二维码扫描界面，这里用的是startActivityForResult跳转
        //扫描完了之后调到该界面
        //				Intent intent = new Intent();
        //				intent.setClass(AddGoodsActivity.this, MipcaActivityCapture.class);
        //				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //				startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
    }

    private void init() {
        mGoodsDao = new GoodsDao();
    }

    private void initView() {
        mInputBarCode = (EditText) findViewById(R.id.et_input_bar_code);
        mScanBarcode = (Button) findViewById(R.id.bt_scan_barcode);
        mScanBarCodeBitmap = (ImageView) findViewById(R.id.iv_scan_bitmap);
        mInputName = (InputGoodsAttribute) findViewById(R.id.input_goods_name);
        mInputBrand = (InputGoodsAttribute) findViewById(R.id.input_brand);
        mInputBuyInPrice = (InputGoodsAttribute) findViewById(R.id.input_buy_in_price);
        mInputRetailPrice = (InputGoodsAttribute) findViewById(R.id.input_retail_price);
        mInputBuyInUnitPrice = (InputGoodsAttribute) findViewById(R.id.input_buy_in_unit_price);
        mInputDesc = (InputGoodsAttribute) findViewById(R.id.input_desc);
        mBtSave = (Button) findViewById(R.id.bt_save);
        mInputStandard = (InputGoodsAttribute) findViewById(R.id.input_standard);

        Intent intent = getIntent();
        boolean hasBarcode = intent.hasExtra("barcode");
        if (hasBarcode) {
            Bundle bundle = intent.getExtras();
            mInputBarCode.setText(bundle.getString("barcode"));
            //显示
            mScanBarCodeBitmap.setImageBitmap((Bitmap) intent.getParcelableExtra("bitmap"));
        }
    }

    private void initEvent() {
        mScanBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(AddGoodsActivity.this, MipcaActivityCapture.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
            }
        });

        mBtSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String barCode = mInputBarCode.getText().toString();
                if (TextUtils.isEmpty(barCode)) {
                    ToastUitl.showShort("条形码不能为空");
                    return;
                }
                String buyInPrice = mInputBuyInPrice.getInputText();
                if (TextUtils.isEmpty(buyInPrice)) {
                    ToastUitl.showShort("进货价不能为空");
                    return;
                }
                String name = mInputName.getInputText();
                String brand = mInputBrand.getInputText();
                String buyInUnitPrice = mInputBuyInUnitPrice.getInputText();
                String standard = mInputStandard.getInputText();
                String desc = mInputDesc.getInputText();
                String retailPrice = mInputRetailPrice.getInputText();
                GoodsBean bean = new GoodsBean();
                bean.setName(name);
                bean.setBarCode(barCode);
                bean.setStandard(standard);
                bean.setBuyInPrice(buyInPrice);
                bean.setBuyInUnitPrice(buyInUnitPrice);
                bean.setBrand(brand);
                bean.setDesc(desc);
                bean.setRetailPrice(retailPrice);
                boolean isAdd = mGoodsDao.add(bean);
                if (isAdd) {
                    ToastUitl.showShort("添加商品成功");
                } else
                    ToastUitl.showShort("添加商品失敗");
                {

                }
                //                HashMap<String, String> map = new HashMap<>();
                //                map.put(GoodsBean.KEY_BAR_CODE, barCode);
                //                List<GoodsBean> query = goodsDao.query(map);
                //                Log.e("-----------", "-----------" + query.get(0)
                //                        .toString());

                bean.save(new SaveListener<String>() {
                    @Override
                    public void done(String objectId, BmobException e) {
                        if (e == null) {
                            Log.e(TAG, "添加数据成功，返回objectId为：" + objectId);
                        } else {
                            Log.e(TAG, "创建数据失败：" + e.getMessage());
                        }
                    }
                });
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SCANNIN_GREQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    //显示扫描到的内容
                    mInputBarCode.setText(bundle.getString("result"));
                    //显示
                    mScanBarCodeBitmap.setImageBitmap((Bitmap) data.getParcelableExtra("bitmap"));
                }
                break;
        }
    }
}
