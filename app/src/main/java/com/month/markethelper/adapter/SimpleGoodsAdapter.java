package com.month.markethelper.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.month.markethelper.R;
import com.month.markethelper.bean.SimpleGoodsBean;
import com.month.markethelper.utils.BitmapUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SimpleGoodsAdapter extends BaseMultiItemQuickAdapter<SimpleGoodsBean, BaseViewHolder> {

    private Context context;

    public SimpleGoodsAdapter(Context context) {
        this.context = context;
        //绑定布局对应的类型
        addItemType(SimpleGoodsBean.TYPE_TITLE, R.layout.item_category_text);
        addItemType(SimpleGoodsBean.TYPE_CONTENT, R.layout.item_goods_simple);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, SimpleGoodsBean item) {
        switch (helper.getItemViewType()) {
            //根据类型设置数据
            case SimpleGoodsBean.TYPE_TITLE:
                helper.setText(R.id.category_tv, item.getTitle());
                break;
            case SimpleGoodsBean.TYPE_CONTENT:
                Bitmap bitmap = BitmapUtils.stringToBitmap(item.getGoods().getUrl());
                ImageView imageView = helper.getView(R.id.goods_pic_iv);
                Glide.with(context).load(bitmap).into(imageView);
                helper.setText(R.id.goods_name_tv, item.getGoods().getName());
                break;
            default:
                break;
            }
        }
}

