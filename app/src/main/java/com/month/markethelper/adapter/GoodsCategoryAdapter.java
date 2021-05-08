package com.month.markethelper.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.month.markethelper.R;
import com.month.markethelper.db.entity.AddressInfo;

import org.jetbrains.annotations.NotNull;

public class GoodsCategoryAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public GoodsCategoryAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, String s) {
        baseViewHolder.setText(R.id.category_tv, s);
    }

}
