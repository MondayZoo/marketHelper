package com.month.markethelper.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.month.markethelper.R;
import com.month.markethelper.bean.GoodsAndCountBean;
import com.month.markethelper.db.entity.Goods;
import com.month.markethelper.utils.BitmapUtils;

import org.jetbrains.annotations.NotNull;

public class GoodsInBagAdapter extends BaseQuickAdapter<GoodsAndCountBean, BaseViewHolder> {

    private Context context;

    public GoodsInBagAdapter(Context context, int layoutResId) {
        super(layoutResId);
        this.context = context;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, GoodsAndCountBean item) {
        Goods goods = item.getGoods();
        helper.setText(R.id.goods_name_tv, goods.getName());
        helper.setText(R.id.goods_price_tv, "￥" + goods.getPrice() * item.getCount());
        helper.setText(R.id.goods_count_tv, String.valueOf(item.getCount()));
        Bitmap bitmap = BitmapUtils.stringToBitmap(goods.getUrl());
        ImageView imageView = helper.getView(R.id.goods_pic_iv);
        Glide.with(context).load(bitmap).into(imageView);
    }
}

