package com.month.markethelper.activity;

import android.content.Intent;
import android.view.View;

import com.month.markethelper.R;
import com.month.markethelper.base.BaseActivity;
import com.month.markethelper.utils.EmptyMessage;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class RegisterChoiceActivity extends BaseActivity implements View.OnClickListener{

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_register_choice;
    }

    @Override
    protected void initView() {
        findViewById(R.id.actionbar_back_tv).setOnClickListener(this);
        findViewById(R.id.customer_register_cv).setOnClickListener(this);
        findViewById(R.id.store_register_cv).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.customer_register_cv) {
            //跳转至普通用户注册页面
            Intent intent = new Intent(this, RegisterActivity.class);
            intent.putExtra("tag", 0);
            startActivity(intent);
            finish();
        } else if (id == R.id.store_register_cv) {
            //跳转至商铺注册页面
            Intent intent = new Intent(this, RegisterActivity.class);
            intent.putExtra("tag", 1);
            startActivity(intent);
            finish();
        } else if (id == R.id.actionbar_back_tv) {
            finish();
        }
    }

    //---------------------------- EventBus ------------------------------------
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveMessage(EmptyMessage message) {

    }
}