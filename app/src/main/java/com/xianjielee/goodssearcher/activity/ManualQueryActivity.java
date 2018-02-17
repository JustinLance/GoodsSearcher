package com.xianjielee.goodssearcher.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.xianjielee.goodssearcher.R;
import com.xianjielee.goodssearcher.db.GoodsBean;
import com.xianjielee.goodssearcher.db.GoodsDao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @Author lixianjie
 * @Des 手動輸入商品名稱查詢
 * @Time 2017/1/26/0026
 */
public class ManualQueryActivity extends Activity {

    private EditText mManualQuery;
    private Button mBarCodeQuery;
    private Button mExit;
    private GoodsDao mGoodsDao;
    private HashMap<String, String> mQueryMap;
    private ListView mQueryResult;
    private List<GoodsBean> mDatas = new ArrayList<>();
    private MyAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_query);
        init();
        initView();
        initEvent();
    }

    private void init() {
        mGoodsDao = new GoodsDao();
        mQueryMap = new HashMap<>();
    }

    private void initView() {
        mManualQuery = (EditText) findViewById(R.id.et_manual_query);
        mBarCodeQuery = (Button) findViewById(R.id.bt_scan_barcode);
        mExit = (Button) findViewById(R.id.bt_exit);
        mQueryResult = (ListView) findViewById(R.id.lv_query_result);
        mAdapter = new MyAdapter();
        mQueryResult.setAdapter(mAdapter);
    }

    private void initEvent() {
        mQueryResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GoodsDetailsActivity.go(ManualQueryActivity.this, mDatas.get(position));
            }
        });

        mBarCodeQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ManualQueryActivity.this, QueryGoodsActivity.class));
            }
        });

        mExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mManualQuery.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mQueryMap.clear();
                mQueryMap.put(GoodsBean.KEY_NAME, s.toString());
                mDatas = mGoodsDao.query(mQueryMap);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mDatas.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = LayoutInflater.from(ManualQueryActivity.this)
                    .inflate(R.layout.item_query_result, parent, false);
            GoodsBean bean = mDatas.get(position);
            ((TextView) view.findViewById(R.id.tv_name)).setText(bean.getName());
            ((TextView) view.findViewById(R.id.tv_buy_in_price)).setText(bean.getBuyInPrice());
            return view;
        }
    }
}
