package com.month.markethelper.fragment.store;

import android.view.View;

import com.month.markethelper.R;
import com.month.markethelper.base.BaseFragment;
import com.month.markethelper.databinding.FragmentStoreCommentBinding;
import com.month.markethelper.databinding.FragmentStoreMenuBinding;

import androidx.lifecycle.ViewModelProvider;

public class CommentFragment extends BaseFragment<FragmentStoreCommentBinding> implements View.OnClickListener{

    private CommentViewModel viewModel;

    //----------------------------basal method---------------------------------

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_store_comment;
    }

    @Override
    protected void initViewModel() {
        viewModel = new ViewModelProvider(this).get(CommentViewModel.class);
        binding.setVm(viewModel);
    }

    @Override
    protected void initView(View rootView) {

    }

    @Override
    protected void initEvent() {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

    }

    //---------------------------- < > ---------------------------------

}