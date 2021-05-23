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
    private int mode;
    public static final int MODE_IN_BAG = 0;
    public static final int MODE_IN_ORDER = 1;

    public GoodsInBagAdapter(Context context, int layoutResId) {
        super(layoutResId);
        this.context = context;

        if (layoutResId == R.layout.item_goods_in_bag) {
            mode = MODE_IN_BAG;
        } else {
            mode = MODE_IN_ORDER;
        }
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, GoodsAndCountBean item) {
        Goods goods = item.getGoods();
        helper.setText(R.id.goods_name_tv, goods.getName());
        helper.setText(R.id.goods_price_tv, "￥" + goods.getPrice() * item.getCount());
        Bitmap bitmap = BitmapUtils.stringToBitmap(goods.getUrl());
        ImageView imageView = helper.getView(R.id.goods_pic_iv);
        Glide.with(context).load(bitmap).into(imageView);
        //复用此适配器，UI调整
        if (mode == MODE_IN_BAG) {
            helper.setText(R.id.goods_count_tv, String.valueOf(item.getCount()));
        } else {
            helper.setText(R.id.goods_count_tv, "x" + item.getCount());
        }
    }
}

