package com.month.markethelper.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.month.markethelper.R;
import com.month.markethelper.db.entity.AddressInfo;
import com.month.markethelper.db.entity.Store;

import org.jetbrains.annotations.NotNull;

public class StoreInfoListAdapter extends BaseQuickAdapter<Store, BaseViewHolder> {

    public StoreInfoListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, Store data) {
        baseViewHolder.setText(R.id.store_name_tv, data.getStoreName());
        baseViewHolder.setText(R.id.store_type_tv, data.getType());
        baseViewHolder.setText(R.id.address_tv, data.getAddress());
    }


}
