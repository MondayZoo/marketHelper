package com.month.markethelper.adapter;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.month.markethelper.R;
import com.month.markethelper.db.entity.AddressInfo;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AddressInfoListAdapter extends BaseQuickAdapter<AddressInfo, BaseViewHolder> {

    private int mode;

    public AddressInfoListAdapter(int layoutResId, int mode) {
        super(layoutResId);
        this.mode = mode;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, AddressInfo data) {
        baseViewHolder.setText(R.id.contacts_tv, data.getContacts() + (data.isGender() ? " 先生" : " 女士"));
        baseViewHolder.setText(R.id.contact_number_tv, data.getContactNumber());
        baseViewHolder.setText(R.id.address_tv, data.getAddress() + data.getHouseNumber());
        if (mode == 1) {
            TextView alterTv = baseViewHolder.findView(R.id.alter_tv);
            alterTv.setVisibility(View.GONE);
        }
    }


}
