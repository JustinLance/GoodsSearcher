package com.lixianjie.goodssearcher.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.MyApplication;

/**
 * @Author lixianjie
 * @Des 数据库
 * @Time 2017/1/24/0024
 */

public class GoodsDatabaseHelper extends SQLiteOpenHelper {

    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "goods.db";
    public static final String TABLE_NAME = "goods";

    public GoodsDatabaseHelper(){
        super(MyApplication.mContext, DB_NAME, null, DB_VERSION);
    }

    public GoodsDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, name, factory, version);
    }

    public GoodsDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler)
    {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        onUpgrade(db, 0, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        for (int version = oldVersion + 1; version <= newVersion; version++) {
            upgradeTo(db, version);
        }
    }

    /**
     * Upgrade database from (version - 1) to version.
     */
    private void upgradeTo(SQLiteDatabase db, int version) {
        switch (version) {
            case 1:
                createTable(db);
                break;
            case 2:
                break;
            case 3:
                break;
            case 103:
                break;
            case 4:
                break;
            default:
                throw new IllegalStateException("Don't know how to upgrade to " + version);
        }
    }

    private void createTable(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE if not exists " + TABLE_NAME + "(" +
                GoodsBean.KEY_ROWID + " integer primary key autoincrement," +
                GoodsBean.KEY_BAR_CODE + " text unique not null," +
                GoodsBean.KEY_NAME + " text," +
                GoodsBean.KEY_BRAND + " text," +
                GoodsBean.KEY_BUY_IN_PRICE + " text," +
                GoodsBean.KEY_BUY_IN_UNIT_PRICE + " text," +
                GoodsBean.KEY_RETAIL_PRICE + " text," +
                GoodsBean.KEY_STANDARD + " text," +
                GoodsBean.KEY_DESC + " text" +
                ");");
    }
}
