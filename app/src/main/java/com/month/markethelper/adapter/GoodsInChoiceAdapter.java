package com.month.markethelper.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.month.markethelper.R;
import com.month.markethelper.db.entity.Goods;
import com.month.markethelper.utils.BitmapUtils;

import org.jetbrains.annotations.NotNull;

public class GoodsInChoiceAdapter extends BaseQuickAdapter<Goods, BaseViewHolder> {

    private Context context;

    public GoodsInChoiceAdapter(Context context) {
        super(R.layout.item_goods_in_choice);
        this.context = context;
    }


    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, Goods goods) {
        baseViewHolder.setText(R.id.goods_name_tv, goods.getName());
        baseViewHolder.setText(R.id.goods_inventory_tv, "库存：" + goods.getInventory());
        ImageView imageView = baseViewHolder.findView(R.id.goods_pic_iv);
        Bitmap bitmap = BitmapUtils.stringToBitmap(goods.getUrl());
        Glide.with(context).load(bitmap).into(imageView);
    }
}

