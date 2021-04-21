package com.month.markethelper.fragment.store;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.month.markethelper.R;
import com.month.markethelper.adapter.CommentAdapter;
import com.month.markethelper.base.BaseFragment;
import com.month.markethelper.databinding.FragmentStoreCommentBinding;
import com.month.markethelper.db.entity.Comment;
import com.month.markethelper.utils.FormatUtils;
import com.month.markethelper.utils.DialogUtils;
import com.month.markethelper.utils.ToastUtils;

import java.util.Date;
import java.util.List;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CommentFragment extends BaseFragment<FragmentStoreCommentBinding> implements View.OnClickListener{

    private CommentViewModel viewModel;

    private long storeId;

    //评论框相关
    private FloatingActionButton mOpenDialogBtn;
    private Dialog mCommentDialog;
    private TextView mCloseDialogTv;
    private EditText mCommentContentEt;
    private RatingBar mRatingBar;
    private TextView mPublishCommentTv;

    //---------------------------- Constructor --------------------------------
    private CommentFragment() {};

    public static CommentFragment newInstance(long storeId) {
        CommentFragment fragment = new CommentFragment();
        Bundle args = new Bundle();
        args.putLong("storeId", storeId);
        fragment.setArguments(args);
        return fragment;
    }

    //----------------------------basal method---------------------------------
    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_store_comment;
    }

    @Override
    protected void initViewModel() {
        viewModel = new ViewModelProvider(this).get(CommentViewModel.class);
        storeId = getArguments().getLong("storeId");
        viewModel.setStoreId(storeId);
        viewModel.initLiveData();
        binding.setVm(viewModel);
    }

    @Override
    protected void initView(View rootView) {
        //RecyclerView - 评论列表
        RecyclerView commentRv = binding.commentRv;
        commentRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        CommentAdapter commentAdapter = new CommentAdapter(R.layout.item_comment);
        commentRv.setAdapter(commentAdapter);
        viewModel.getCommentList().observe(this, new Observer<List<Comment>>() {
            @Override
            public void onChanged(List<Comment> comments) {
                commentAdapter.setList(comments);
            }
        });
        //Dialog -- 评论框
        mOpenDialogBtn = binding.commentPublishFab;
        mCommentDialog = DialogUtils.createCommentDialog(getActivity());
        mCloseDialogTv = mCommentDialog.findViewById(R.id.close_tv);
        mCommentContentEt = mCommentDialog.findViewById(R.id.comment_et);
        mPublishCommentTv = mCommentDialog.findViewById(R.id.comment_publish_tv);
        mRatingBar = mCommentDialog.findViewById(R.id.comment_rating_bar);
    }

    @Override
    protected void initEvent() {
        //FloatingActionBar
        mOpenDialogBtn.setOnClickListener(this);
        mCloseDialogTv.setOnClickListener(this);
        mPublishCommentTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        //发布评论 -- 弹出评论框，隐藏悬浮按钮
        if (id == R.id.comment_publish_fab) {
            openDialog();
        }
        //关闭评论框
        else if (id == R.id.close_tv) {
            closeDialog();
        }
        //发布评论
        else if (id == R.id.comment_publish_tv) {
            publish();
        }
    }

    //----------------------------Click Event Method---------------------------------
    private void openDialog() {
        mCommentDialog.show();
        mOpenDialogBtn.setVisibility(View.GONE);
    }

    private void closeDialog() {
        mCommentDialog.dismiss();
        mCommentContentEt.setText("");
        mRatingBar.setRating(3);
        mOpenDialogBtn.setVisibility(View.VISIBLE);
    }

    private void publish() {
        String nickName = viewModel.getUserNickName(userPhoneNum);
        String content = mCommentContentEt.getText().toString();
        float rating = mRatingBar.getRating();
        String date = FormatUtils.dateFormat(new Date());
        System.out.println("---------->" + nickName + "发布了评论：" + content + " 于 " + date + " 评分 "  + rating);
        viewModel.addComment(nickName, content, rating, date, storeId);
        ToastUtils.showToast("评论成功！");
        closeDialog();
    }
}