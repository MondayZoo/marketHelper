package com.month.markethelper.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;

import com.amap.api.location.AMapLocationClient;
import com.gyf.immersionbar.ImmersionBar;
import com.month.markethelper.R;
import com.month.markethelper.activity.vm.RegisterViewModel;
import com.month.markethelper.base.BaseActivityWithViewModel;
import com.month.markethelper.databinding.ActivityRegisterBinding;
import com.month.markethelper.utils.DialogUtils;
import com.month.markethelper.utils.MapUtils;
import com.month.markethelper.utils.ToastUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

public class RegisterActivity extends BaseActivityWithViewModel<ActivityRegisterBinding> implements View.OnClickListener {

    private RegisterViewModel viewModel;

    //----------------------------basal method---------------------------------
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_register;
    }

    @Override
    protected void initViewModel() {
        viewModel = new ViewModelProvider(this).get(RegisterViewModel.class);
        binding.setVm(viewModel);
        binding.setLifecycleOwner(this);
    }

    @Override
    protected void initView() {
        findViewById(R.id.actionbar_back_tv).setOnClickListener(this);
        binding.registerTv.setOnClickListener(this);
    }

    @Override
    protected void initEvent() {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.actionbar_back_tv) {
            finish();
        }

        /*
         * 注册
         * 检查表单
         */
        else if (id == R.id.register_tv)
        {
            if (TextUtils.isEmpty(viewModel.getPhoneNum().getValue())
                    || TextUtils.isEmpty(viewModel.getRealName().getValue())
                    || TextUtils.isEmpty(viewModel.getNickName().getValue())) {
                ToastUtils.showToast("请将信息填写完整！");
            }
            else if (viewModel.getPhoneNum().getValue().length() != 11) {
                binding.registerPhoneNumEt.requestFocus();
                ToastUtils.showToast("手机号不符合要求！");
            }
            else if (viewModel.isRegistered()){
                binding.registerPhoneNumEt.requestFocus();
                ToastUtils.showToast("该手机号已被注册！");
            } else {
                viewModel.register();
                ToastUtils.showToast("注册成功！");
                Handler handler = new Handler();
                handler.postDelayed(this::finish, 1000);
            }
        }
    }
}