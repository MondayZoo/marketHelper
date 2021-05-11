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
import com.month.markethelper.bean.SimpleGoodsBean;
import com.month.markethelper.utils.BitmapUtils;

import org.jetbrains.annotations.NotNull;

public class GoodsInMenuAdapter extends BaseMultiItemQuickAdapter<SimpleGoodsBean, BaseViewHolder> {

    private Context context;

    public GoodsInMenuAdapter(Context context) {
        this.context = context;
        //绑定布局对应的类型
        addItemType(SimpleGoodsBean.TYPE_TITLE, R.layout.item_category_text);
        addItemType(SimpleGoodsBean.TYPE_CONTENT, R.layout.item_goods_in_menu);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, SimpleGoodsBean item) {
        switch (helper.getItemViewType()) {
            //根据类型设置数据
            case SimpleGoodsBean.TYPE_TITLE:
                TextView textView = helper.getView(R.id.category_tv);
                textView.setText(item.getTitle());
                textView.setTextSize(14);
                break;
            case SimpleGoodsBean.TYPE_CONTENT:
                Bitmap bitmap = BitmapUtils.stringToBitmap(item.getGoods().getUrl());
                ImageView imageView = helper.getView(R.id.goods_pic_iv);
                Glide.with(context).load(bitmap).into(imageView);
                //设置商品名
                helper.setText(R.id.goods_name_tv, item.getGoods().getName());
                //设置商品介绍
                helper.setText(R.id.goods_intro_tv, item.getGoods().getIntro());
                //设置单价 格式 xx元/单位
                helper.setText(R.id.goods_price_tv,
                        String.format(context.getString(R.string.string_goods_price_with_unit),
                                String.valueOf(item.getGoods().getPrice()),
                                item.getGoods().getUnit()));
                break;
            default:
                break;
            }
        }
}

