package com.lixianjie.goodssearcher.db;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;


/**
 * @Author lixianjie
 * @Des TODO
 * @Time 2017/1/25/0025
 */

public class GoodsBean extends BmobObject implements Serializable {
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
}
