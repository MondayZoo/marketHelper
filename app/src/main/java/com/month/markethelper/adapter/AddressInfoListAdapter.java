package com.month.markethelper.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.month.markethelper.R;
import com.month.markethelper.db.entity.AddressInfo;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AddressInfoListAdapter extends BaseQuickAdapter<AddressInfo, BaseViewHolder> {

    public AddressInfoListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, AddressInfo data) {
        baseViewHolder.setText(R.id.contacts_tv, data.getContacts() + (data.isGender() ? " 先生" : " 女士"));
        baseViewHolder.setText(R.id.contact_number_tv, data.getContactNumber());
        baseViewHolder.setText(R.id.address_tv, data.getAddress() + data.getHouseNumber());
    }


}
