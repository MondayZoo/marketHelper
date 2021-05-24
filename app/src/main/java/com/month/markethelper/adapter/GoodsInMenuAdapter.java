package com.month.markethelper.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.month.markethelper.R;
import com.month.markethelper.bean.GoodsAndTypeBean;
import com.month.markethelper.db.entity.Goods;
import com.month.markethelper.utils.BitmapUtils;

import org.jetbrains.annotations.NotNull;

public class GoodsInMenuAdapter extends BaseMultiItemQuickAdapter<GoodsAndTypeBean, BaseViewHolder> {

    private Context context;

    public GoodsInMenuAdapter(Context context) {
        this.context = context;
        //绑定布局对应的类型
        addItemType(GoodsAndTypeBean.TYPE_TITLE, R.layout.item_category_text);
        addItemType(GoodsAndTypeBean.TYPE_CONTENT, R.layout.item_goods_in_menu);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, GoodsAndTypeBean item) {
        switch (helper.getItemViewType()) {
            //根据类型设置数据
            case GoodsAndTypeBean.TYPE_TITLE:
                TextView textView = helper.getView(R.id.category_tv);
                textView.setText(item.getTitle());
                textView.setTextSize(14);
                break;
            case GoodsAndTypeBean.TYPE_CONTENT:
                Goods goods = item.getGoods();
                Bitmap bitmap = BitmapUtils.stringToBitmap(goods.getUrl());
                ImageView imageView = helper.getView(R.id.goods_pic_iv);
                Glide.with(context).load(bitmap).into(imageView);
                //设置商品名
                helper.setText(R.id.goods_name_tv, goods.getName());
                //设置商品介绍
                helper.setText(R.id.goods_intro_tv, goods.getIntro());
                //设置单价 格式 xx元/单位
                helper.setText(R.id.goods_price_tv,
                        String.format(context.getString(R.string.string_goods_price_with_unit),
                                String.valueOf(goods.getPrice()),
                                goods.getUnit()));
                //设置购买按钮
                helper.setVisible(R.id.goods_tips_tv, goods.getInventory() <= 50);
                helper.setVisible(R.id.goods_buy_tv, goods.getInventory() > 50);
                break;
            default:
                break;
            }
        }
}

