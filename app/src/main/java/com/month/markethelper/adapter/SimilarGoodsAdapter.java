package com.month.markethelper.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.month.markethelper.R;
import com.month.markethelper.bean.SimilarDataBean;
import com.month.markethelper.db.entity.Goods;
import com.month.markethelper.utils.BitmapUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SimilarGoodsAdapter extends BaseQuickAdapter<Goods, BaseViewHolder> {

    private Context context;

    public SimilarGoodsAdapter(@Nullable List<Goods> data, Context context) {
        super(R.layout.item_similar_goods, data);
        this.context = context;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, Goods goods) {
        baseViewHolder.setText(R.id.goods_name_tv, goods.getName());
        Bitmap bitmap = BitmapUtils.stringToBitmap(goods.getUrl());
        ImageView imageView = baseViewHolder.findView(R.id.goods_pic_iv);
        Glide.with(context).load(bitmap).into(imageView);
    }
}
