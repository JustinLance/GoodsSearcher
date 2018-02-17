package com.xianjielee.goodssearcher.activity;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.xianjielee.goodssearcher.R;
import com.xianjielee.goodssearcher.db.GoodsBean;
import com.xianjielee.goodssearcher.util.ToastUitl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListResultActivity extends ListActivity {

    private List<Map<String, String>> adapterDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayList<GoodsBean> data = getIntent().getParcelableArrayListExtra("data");
        adapterDatas = new ArrayList<>();
        for (GoodsBean datum : data) {
            adapterDatas.add(datum.toMap());
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(this,
                adapterDatas,
                R.layout.item_search_list,
                new String[]{GoodsBean.KEY_NAME, GoodsBean.KEY_BAR_CODE, GoodsBean.KEY_BRAND},
                new int[]{R.id.tv_name, R.id.tv_barcode, R.id.tv_brand});
        setListAdapter(simpleAdapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        GoodsBean bean = GoodsBean.toGoodsBean(adapterDatas.get(position));
        if (bean == null) {
            ToastUitl.showShort("出现未知错误...");
        } else {
            GoodsDetailsActivity.go(this, bean);
        }
    }

    public static void go(Context context, List<GoodsBean> datas) {
        context.startActivity(new Intent(context, ListResultActivity.class).putParcelableArrayListExtra("data", (ArrayList<? extends Parcelable>) datas));
    }
}
