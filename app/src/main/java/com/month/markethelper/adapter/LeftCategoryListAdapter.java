package com.month.markethelper.adapter;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.month.markethelper.R;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LeftCategoryListAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    //用户选中的分类位置
    private int mCurrentSelectedPosition = 0;

    public LeftCategoryListAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, String s) {

        TextView itemView = baseViewHolder.getView(R.id.left_category_text);
        itemView.setText(s);

        if (baseViewHolder.getLayoutPosition() == mCurrentSelectedPosition) {
            itemView.setBackgroundColor(itemView.getResources().getColor(R.color.white, null));
        } else {
            itemView.setBackgroundColor(itemView.getResources().getColor(R.color.color_F8F8F8, null));
        }
    }

    public void setSelectedPosition(int position) {
        mCurrentSelectedPosition = position;
    }

}
