package com.month.markethelper.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.month.markethelper.db.entity.Goods;

public class SimpleGoodsBean implements MultiItemEntity {

    public static final int TYPE_TITLE = 1;
    public static final int TYPE_CONTENT = 2;

    private int itemType;
    private String title;
    private Goods goods;
    private int spanSize;

    public SimpleGoodsBean(String title) {
        this.title = title;
        this.itemType = TYPE_TITLE;
        this.spanSize = 2;
    }

    public SimpleGoodsBean(Goods goods) {
        this.goods = goods;
        this.itemType = TYPE_CONTENT;
        this.spanSize = 1;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    public String getTitle() {
        return title;
    }

    public Goods getGoods() {
        return goods;
    }

    public int getSpanSize() {
        return spanSize;
    }
}
