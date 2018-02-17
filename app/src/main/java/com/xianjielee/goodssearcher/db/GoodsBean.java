package com.xianjielee.goodssearcher.db;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import cn.bmob.v3.BmobObject;


/**
 * @Author lixianjie
 * @Des TODO
 * @Time 2017/1/25/0025
 */

public class GoodsBean extends BmobObject implements Parcelable {

    /** 自增长键值 */
    public static final String KEY_ROWID = "id_";
    /** 商品名称 */
    public static final String KEY_NAME = "name";
    /** 商品品牌 */
    public static final String KEY_BRAND = "brand";
    /** 商品条形码 */
    public static final String KEY_BAR_CODE = "barcode";
    /** 商品规格 */
    public static final String KEY_STANDARD = "standard";
    /** 商品进货价 */
    public static final String KEY_BUY_IN_PRICE = "buy_in_price";
    /** 商品进货单价 */
    public static final String KEY_BUY_IN_UNIT_PRICE = "buy_in_unit_price";
    /** 商品零售价 */
    public static final String KEY_RETAIL_PRICE = "retail_price";
    /** 商品备注或描述 */
    public static final String KEY_DESC = "desc";

    private int id_;
    private String name;
    private String barCode;
    private String brand;
    private String standard;
    private String buyInPrice;
    private String buyInUnitPrice;
    private String desc;
    private String retailPrice;

    public GoodsBean() {
    }

    public GoodsBean(Parcel in) {
        id_ = in.readInt();
        name = in.readString();
        barCode = in.readString();
        brand = in.readString();
        standard = in.readString();
        buyInPrice = in.readString();
        buyInUnitPrice = in.readString();
        desc = in.readString();
        retailPrice = in.readString();
    }

    public static final Creator<GoodsBean> CREATOR = new Creator<GoodsBean>() {
        @Override
        public GoodsBean createFromParcel(Parcel in) {
            return new GoodsBean(in);
        }

        @Override
        public GoodsBean[] newArray(int size) {
            return new GoodsBean[size];
        }
    };

    public String getRetailPrice()
    {
        return retailPrice;
    }

    public void setRetailPrice(String retailPrice)
    {
        this.retailPrice = retailPrice;
    }

    public int getId_()
    {
        return id_;
    }

    public String getBrand()
    {
        return brand;
    }

    public void setBrand(String brand)
    {
        this.brand = brand;
    }

    public void setId_(int id_)
    {
        this.id_ = id_;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getBarCode()
    {
        return barCode;
    }

    public void setBarCode(String barCode)
    {
        this.barCode = barCode;
    }

    public String getStandard()
    {
        return standard;
    }

    public void setStandard(String standard)
    {
        this.standard = standard;
    }

    public String getBuyInPrice()
    {
        return buyInPrice;
    }

    public void setBuyInPrice(String buyInPrice)
    {
        this.buyInPrice = buyInPrice;
    }

    public String getBuyInUnitPrice()
    {
        return buyInUnitPrice;
    }

    public void setBuyInUnitPrice(String buyInUnitPrice)
    {
        this.buyInUnitPrice = buyInUnitPrice;
    }

    public String getDesc()
    {
        return desc;
    }

    public void setDesc(String desc)
    {
        this.desc = desc;
    }

    @Override
    public String toString()
    {
        return "GoodsBean{" +
                "id_=" + id_ +
                ", name='" + name + '\'' +
                ", barCode='" + barCode + '\'' +
                ", brand='" + brand + '\'' +
                ", standard='" + standard + '\'' +
                ", buyInPrice='" + buyInPrice + '\'' +
                ", buyInUnitPrice='" + buyInUnitPrice + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }

    public Map<String, String> toMap() {
        HashMap<String, String> map = new HashMap<>();
        map.put(KEY_NAME, getName());
        map.put(KEY_BRAND, getBrand());
        map.put(KEY_BAR_CODE, getBarCode());
        map.put(KEY_STANDARD, getStandard());
        map.put(KEY_BUY_IN_PRICE, getBuyInPrice());
        map.put(KEY_BUY_IN_UNIT_PRICE, getBuyInUnitPrice());
        map.put(KEY_RETAIL_PRICE, getRetailPrice());
        map.put(KEY_DESC, getDesc());
        return map;
    }

    public static GoodsBean toGoodsBean(Map<String, String> values) {
        if (values == null || values.size() <= 0) {
            return null;
        }
        GoodsBean goodsBean = new GoodsBean();
        goodsBean.setName(values.get(KEY_NAME));
        goodsBean.setBrand(values.get(KEY_BRAND));
        goodsBean.setBarCode(values.get(KEY_BAR_CODE));
        goodsBean.setStandard(values.get(KEY_STANDARD));
        goodsBean.setBuyInPrice(values.get(KEY_BUY_IN_PRICE));
        goodsBean.setBuyInUnitPrice(values.get(KEY_BUY_IN_UNIT_PRICE));
        goodsBean.setRetailPrice(values.get(KEY_RETAIL_PRICE));
        goodsBean.setDesc(values.get(KEY_DESC));
        return goodsBean;
    }

    public static GoodsBean copy(GoodsBean source) {
        GoodsBean data = new GoodsBean();
        data.setName(source.getName());
        data.setBrand(source.getBrand());
        data.setBarCode(source.getBarCode());
        data.setStandard(source.getStandard());
        data.setBuyInPrice(source.getBuyInPrice());
        data.setBuyInUnitPrice(source.getBuyInUnitPrice());
        data.setRetailPrice(source.getRetailPrice());
        data.setDesc(source.getDesc());
        return data;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id_);
        parcel.writeString(name);
        parcel.writeString(barCode);
        parcel.writeString(brand);
        parcel.writeString(standard);
        parcel.writeString(buyInPrice);
        parcel.writeString(buyInUnitPrice);
        parcel.writeString(desc);
        parcel.writeString(retailPrice);
    }
}
