package com.month.markethelper.adapter;

import android.widget.RatingBar;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.month.markethelper.R;
import com.month.markethelper.db.entity.Store;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RightContentListAdapter extends BaseQuickAdapter<Store, BaseViewHolder> {

    public RightContentListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, Store store) {
        baseViewHolder.setText(R.id.store_name_tv, store.getStoreName());
        baseViewHolder.setText(R.id.store_tips_tv, "新店");
        baseViewHolder.setText(R.id.store_intro_tv, store.getIntro());
        baseViewHolder.setText(R.id.store_evaluation_tv, store.getEvaluation());
    }
}
