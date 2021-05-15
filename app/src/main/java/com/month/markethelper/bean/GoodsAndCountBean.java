package com.month.markethelper.bean;

import com.month.markethelper.db.entity.Goods;

/**
 * 商品及其购买数量
 */
public class GoodsAndCountBean {

    private Goods goods;
    private int count;

    public GoodsAndCountBean(Goods goods, int count) {
        this.goods = goods;
        this.count = count;
    }

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
