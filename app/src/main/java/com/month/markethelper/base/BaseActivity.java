package com.month.markethelper.base;

import android.os.Bundle;

import com.gyf.immersionbar.ImmersionBar;

import org.greenrobot.eventbus.EventBus;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        ImmersionBar.with(this).init();
        EventBus.getDefault().register(this);
        initView();
        initEvent();
    }

    protected void initEvent() { }

    protected abstract void initView();

    protected abstract int getLayoutResId();

    //释放资源
    protected void release() { }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        release();
    }
}
