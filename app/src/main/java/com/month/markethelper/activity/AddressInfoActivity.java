package com.month.markethelper.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;

import com.gyf.immersionbar.ImmersionBar;
import com.month.markethelper.R;
import com.month.markethelper.activity.vm.AddressInfoViewModel;
import com.month.markethelper.base.BaseActivityWithViewModel;
import com.month.markethelper.databinding.ActivityAddressInfoBinding;

public class AddressInfoActivity extends BaseActivityWithViewModel<ActivityAddressInfoBinding> implements View.OnClickListener{

    AddressInfoViewModel viewModel;

    private AppCompatTextView backTv, titleTv, rightTv;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_address_info;
    }

    @Override
    protected void initViewModel() {
        viewModel = new ViewModelProvider(this).get(AddressInfoViewModel.class);
        binding.setVm(viewModel);
        binding.setLifecycleOwner(this);
    }

    @Override
    protected void initView() {
        backTv = findViewById(R.id.actionbar_back_tv);
        titleTv = findViewById(R.id.actionbar_title_tv);
        rightTv = findViewById(R.id.actionbar_right_tv);
        ImmersionBar.with(this).statusBarColor(R.color.color_D10D0B).init();
        //RecyclerView

    }

    @Override
    protected void initEvent() {
        backTv.setOnClickListener(this);
        rightTv.setOnClickListener(this);
    }

    @Override
    protected void initObserver() {
        //Actionbar标题
        viewModel.getActionbarTitle().observe(this, s -> titleTv.setText(s));
        //Actionbar右文本
        viewModel.getActionbarRightText().observe(this, s -> rightTv.setText(s));
    }

    @Override
    public void onClick(View v) {

    }
}