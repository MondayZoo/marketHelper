package com.month.markethelper.adapter;

import android.view.View;
import android.widget.RatingBar;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.month.markethelper.R;
import com.month.markethelper.db.entity.Comment;

import org.jetbrains.annotations.NotNull;

public class CommentAdapter extends BaseQuickAdapter<Comment, BaseViewHolder> {

    public CommentAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, Comment comment) {
        baseViewHolder.setText(R.id.user_nick_name_tv, comment.getNickName());
        RatingBar ratingBar = baseViewHolder.findView(R.id.rating_bar);
        ratingBar.setRating(comment.getRating());
        baseViewHolder.setText(R.id.comment_content_tv, comment.getContent());
        baseViewHolder.setText(R.id.comment_date_tv, comment.getDate());
    }
}
