package com.month.markethelper.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.month.markethelper.R;
import com.month.markethelper.bean.ToolBean;
import com.month.markethelper.db.entity.Store;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ManagementToolsAdapter extends BaseQuickAdapter<ToolBean, BaseViewHolder> {

    public ManagementToolsAdapter(int layoutResId, @Nullable List<ToolBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, ToolBean bean) {
        baseViewHolder.setBackgroundResource(R.id.icon_tv, bean.getIconResId());
        baseViewHolder.setText(R.id.func_tv, bean.getText());
    }
}
