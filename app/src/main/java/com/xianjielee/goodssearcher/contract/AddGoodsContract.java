package com.xianjielee.goodssearcher.contract;

/**
 * @Author lixianjie
 * @Des TODO
 * @Time 2017/1/26/0026
 */

public interface AddGoodsContract {

    interface View {

    }

    interface presenter {
        /**
         * 跳转条码扫描
         */
        void goToScanBarCode();
    }

    interface model{

    }
}
