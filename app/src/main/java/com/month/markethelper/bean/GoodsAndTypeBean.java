package com.month.markethelper.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.month.markethelper.db.entity.Goods;

/**
 * 商品或商品类型
 * 用于分类展示数据
 */
public class GoodsAndTypeBean implements MultiItemEntity {

    public static final int TYPE_TITLE = 1;
    public static final int TYPE_CONTENT = 2;

    private int itemType;
    private String title;
    private Goods goods;
    private int spanSize;

    public GoodsAndTypeBean(String title) {
        this.title = title;
        this.itemType = TYPE_TITLE;
        this.spanSize = 2;
    }

    public GoodsAndTypeBean(Goods goods, String type) {
        this.goods = goods;
        this.title = type;
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
