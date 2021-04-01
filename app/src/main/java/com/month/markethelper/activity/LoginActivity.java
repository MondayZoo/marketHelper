package com.month.markethelper.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;

import com.month.markethelper.MainActivity;
import com.month.markethelper.R;
import com.month.markethelper.activity.vm.LoginViewModel;
import com.month.markethelper.base.BaseActivityWithViewModel;
import com.month.markethelper.databinding.ActivityLoginBinding;
import com.month.markethelper.utils.EmptyMessage;
import com.month.markethelper.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import androidx.lifecycle.ViewModelProvider;

public class LoginActivity extends BaseActivityWithViewModel<ActivityLoginBinding> implements View.OnClickListener {

    public static final String TAG = "LoginActivity";

    private LoginViewModel viewModel;

    //----------------------------basal method---------------------------------

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initViewModel() {
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        binding.setVm(viewModel);
        binding.setLifecycleOwner(this);
    }

    protected void initView() {
        // include包含的布局内组件binding获取不到
        findViewById(R.id.actionbar_back_tv).setOnClickListener(this);
        //
        binding.loginRegisterTv.setOnClickListener(this);
        binding.getSmsCodeTv.setOnClickListener(this);
        binding.loginTv.setOnClickListener(this);
    }

    /**
     * 点击事件
     * Activity(View层) 仅负责与数据无关的UI
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.actionbar_back_tv) {
            finish();
        }
        //注册
        else if (v.getId() == R.id.login_register_tv)
        {
            Intent intent = new Intent(this, RegisterChoiceActivity.class);
            startActivity(intent);
        }
        //获取验证码
        else if (v.getId() == R.id.get_sms_code_tv)
        {
            //检查手机号是否注册
            if (viewModel.isRegistered()) {
                //获取验证码
                ToastUtils.showLongToast("您的验证码是：" + viewModel.generateSmsCode());
                viewModel.getTimer().start();   //开启倒计时
            }
            //手机号未注册
            else {
                binding.loginPhoneNumEt.requestFocus();
                ToastUtils.showToast("该手机号尚未注册！");
            }
        }
        //登录
        else if (v.getId() == R.id.login_tv)
        {
            if (viewModel.login()) {
                //fixme
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("user", viewModel.getPhoneNum().getValue());
                editor.apply();
                ToastUtils.showToast("登录成功！");
                //通知Fragment进行相应的UI调整
                EventBus.getDefault().post(EmptyMessage.getInstance(EmptyMessage.STATE_LOGIN));
                //返回MainActivity
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            } else {
                ToastUtils.showToast("验证码错误！");
            }
        }
    }

    @Override
    protected void onDestroy() {
        Log.e(TAG, "Activity --> onDestroy");
        super.onDestroy();
    }
}