package com.lixianjie.goodssearcher.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author lixianjie
 * @Des 数据库操作类
 * @Time 2017/1/25/0025
 */

public class GoodsDao {
    private GoodsDatabaseHelper mGoodsDatabaseHelper;

    public GoodsDao()
    {
        mGoodsDatabaseHelper = new GoodsDatabaseHelper();
    }

    /**
     * @param goodsDbBean
     * @return
     */
    public boolean add(GoodsBean goodsDbBean)
    {
        boolean flag = false;
        HashMap<String,String> map = new HashMap<>();
        map.put(GoodsBean.KEY_BAR_CODE, goodsDbBean.getBarCode());
        List<GoodsBean> query = query(map);
        if (query != null && query.size() > 0)
        {
            GoodsBean bean = query.get(0);
            update(goodsDbBean);
        }else
        {
            SQLiteDatabase database = mGoodsDatabaseHelper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(GoodsBean.KEY_BAR_CODE, goodsDbBean.getBarCode());
            contentValues.put(GoodsBean.KEY_NAME, goodsDbBean.getName());
            contentValues.put(GoodsBean.KEY_BRAND, goodsDbBean.getBrand());
            contentValues.put(GoodsBean.KEY_BUY_IN_PRICE, goodsDbBean.getBuyInPrice());
            contentValues.put(GoodsBean.KEY_BUY_IN_UNIT_PRICE, goodsDbBean.getBuyInUnitPrice());
            contentValues.put(GoodsBean.KEY_DESC, goodsDbBean.getDesc());
            contentValues.put(GoodsBean.KEY_STANDARD, goodsDbBean.getStandard());
            contentValues.put(GoodsBean.KEY_RETAIL_PRICE, goodsDbBean.getRetailPrice());

            long insert = database.insert(GoodsDatabaseHelper.TABLE_NAME, null, contentValues);
            if (insert > 0)
            {
                flag = true;
            }
            database.close();
        }
        return flag;
    }

    public boolean delete()
    {
        return false;
    }

    /**
     * @param barcode
     * @param argName
     * @param argValue
     * @return
     */
    public boolean update(String barcode, String argName, String argValue)
    {
        boolean flag = false;
        SQLiteDatabase db = mGoodsDatabaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(argName, argValue);
        int result = db.update(GoodsDatabaseHelper.TABLE_NAME, values, GoodsBean.KEY_BAR_CODE + "=?", new String[]{barcode});
        if (result > 0)
        {
            flag = true;
        }
        db.close();
        return flag;
    }

    /**
     *
     * @param bean
     * @return
     */
    public boolean update(GoodsBean bean)
    {
        String barCode = bean.getBarCode();
        update(barCode, GoodsBean.KEY_BRAND, bean.getBrand());
        update(barCode, GoodsBean.KEY_BUY_IN_PRICE, bean.getBuyInPrice());
        update(barCode, GoodsBean.KEY_NAME, bean.getName());
        update(barCode, GoodsBean.KEY_STANDARD, bean.getStandard());
        update(barCode, GoodsBean.KEY_BUY_IN_UNIT_PRICE, bean.getBuyInUnitPrice());
        update(barCode, GoodsBean.KEY_RETAIL_PRICE, bean.getRetailPrice());
        return update(barCode, GoodsBean.KEY_DESC, bean.getDesc());
    }

    /**
     *
     * @param map
     * @return
     */
    public List<GoodsBean> query(HashMap<String,String> map)
    {
        StringBuilder selections = new StringBuilder();
        StringBuilder selectArgs = new StringBuilder();
        Set<Map.Entry<String,String>> set = map.entrySet();
        int count = 0;
        for(Map.Entry<String,String> me : set){
            String key = me.getKey();
            String value = me.getValue();
            if(count != set.size() - 1){
                selections.append(key);
                selections.append("=? or ");

                selectArgs.append(value);
                selectArgs.append(",");
            }else{
                selections.append(key);
                selections.append("=? ");
                selectArgs.append(value);
            }
            count++;
        }

        SQLiteDatabase db = mGoodsDatabaseHelper.getReadableDatabase();
        String sql = "select * from " + GoodsDatabaseHelper.TABLE_NAME + " where "+selections.toString();
        System.out.println(sql);
        System.out.println(selectArgs);
        // select * from aaa where name=? and password=?
        //		Cursor cursor = db.query("students", new String[]{"number","name","_id","sex","phone"}, selections, new String[]{selectArgs}, null, null, null, null);
        //		Cursor cursor = db.query("students", new String[]{"number","name","_id","sex","phone"}, "name=? and password=?", new String[]{name,password}, null, null, null, null);
        Cursor cursor = db.rawQuery(sql, new String[]{selectArgs.toString()});

        GoodsBean bean = null;
        List<GoodsBean> list = new ArrayList<>();
        while(cursor.moveToNext()){
            Integer _id = cursor.getInt(0);
            String barCode = cursor.getString(1);
            String name = cursor.getString(2);
            String brand = cursor.getString(3);
            String buyInPrice = cursor.getString(4);
            String buyInUnitPrice = cursor.getString(5);
            String retailPrice = cursor.getString(6);
            String standard = cursor.getString(7);
            String desc = cursor.getString(8);
            bean = new GoodsBean();
            bean.setId_(_id);
            bean.setName(name);
            bean.setBarCode(barCode);
            bean.setBuyInPrice(buyInPrice);
            bean.setBuyInUnitPrice(buyInUnitPrice);
            bean.setStandard(standard);
            bean.setDesc(desc);
            bean.setBrand(brand);
            bean.setRetailPrice(retailPrice);
            list.add(bean);
        }
        db.close();
        return list;
    }

}
