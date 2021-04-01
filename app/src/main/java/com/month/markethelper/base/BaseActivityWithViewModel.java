package com.month.markethelper.base;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.gyf.immersionbar.ImmersionBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

public abstract class BaseActivityWithViewModel<B extends ViewDataBinding> extends AppCompatActivity {

    protected B binding;
    protected SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, getLayoutResId());
        sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        initViewModel();
        initView();
        initEvent();
        initObserver();
        ImmersionBar.with(this).init();
    }

    //获取布局id
    protected abstract int getLayoutResId();

    //初始化ViewModel
    protected abstract void initViewModel();

    //初始化View
    protected abstract void initView();

    //释放资源
    protected void release() {};

    //初始化事件
    protected void initEvent() {}

    //初始化观察者
    protected void initObserver() {}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        release();
    }
}
