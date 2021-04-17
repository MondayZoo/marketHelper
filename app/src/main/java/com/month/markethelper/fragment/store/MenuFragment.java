package com.month.markethelper.fragment.store;

import android.os.Bundle;
import android.view.View;

import com.month.markethelper.R;
import com.month.markethelper.base.BaseFragment;
import com.month.markethelper.databinding.FragmentStoreDetialsBinding;
import com.month.markethelper.databinding.FragmentStoreMenuBinding;

import androidx.lifecycle.ViewModelProvider;

public class MenuFragment extends BaseFragment<FragmentStoreMenuBinding> implements View.OnClickListener{

    private MenuViewModel viewModel;

    //---------------------------- Constructor --------------------------------
    private MenuFragment() {};

    public static MenuFragment newInstance(long storeId) {
        MenuFragment fragment = new MenuFragment();
        Bundle args = new Bundle();
        args.putLong("storeId", storeId);
        fragment.setArguments(args);
        return fragment;
    }

    //---------------------------- Basal Method ---------------------------------

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_store_menu;
    }

    @Override
    protected void initViewModel() {
        viewModel = new ViewModelProvider(this).get(MenuViewModel.class);
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