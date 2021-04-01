package com.month.markethelper.activity.vm;

import android.app.Application;
import android.content.Context;
import android.os.CountDownTimer;
import android.text.TextWatcher;
import android.util.Log;

import com.month.markethelper.R;
import com.month.markethelper.base.BaseApplication;
import com.month.markethelper.db.MarketDatabase;
import com.month.markethelper.db.dao.UserDAO;
import com.month.markethelper.utils.SimpleTextWatcher;

import java.util.Random;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.room.util.StringUtil;

public class LoginViewModel extends AndroidViewModel {

    public static final String TAG = "LoginViewModel";

    private Context context;

    private final UserDAO userDAO;

    //登录前置条件
    private MutableLiveData<Boolean> isCheckAgreement;
    private boolean isPhoneNumCorrect = false;
    private boolean isSmsCodeCorrect = false;
    private MutableLiveData<Boolean> canLogin;

    //清空手机号输入
    private MutableLiveData<Integer> showClearBtn;
    private MutableLiveData<String> phoneNum;

    //EditText监听器
    private TextWatcher phoneNumWatcher;
    private TextWatcher smsCodeWatcher;

    //验证码
    private MutableLiveData<String> smsCode;
    private MutableLiveData<String> smsCodeText;    //获取验证码按钮的文本
    private MutableLiveData<Boolean> canGetSmsCode;
    private String theSmsCode;
    private CountDownTimer timer;
    private boolean isTiming = false;

    public LoginViewModel(Application application) {
        super(application);
        context = application;
        MarketDatabase marketDatabase = MarketDatabase.getInstance();
        userDAO = marketDatabase.getUserDao();
        initLiveData();
        initListener();
    }

    private void initLiveData() {
        //初始化LiveData
        isCheckAgreement = new MutableLiveData<>();
        isCheckAgreement.setValue(false);
        canLogin = new MutableLiveData<>();
        canLogin.setValue(false);
        showClearBtn = new MutableLiveData<>();
        showClearBtn.setValue(8);
        phoneNum = new MutableLiveData<>();
        phoneNum.setValue("");
        smsCode = new MutableLiveData<>();
        smsCode.setValue("");
        smsCodeText = new MutableLiveData<>();
        smsCodeText.setValue("获取验证码");
        canGetSmsCode = new MutableLiveData<>();
        canGetSmsCode.setValue(false);
    }

    private void initListener() {
        //手机号文本监听
        phoneNumWatcher = new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                showClearBtn.setValue(s.length() > 0 ? 0 : 8);
                isPhoneNumCorrect = (s.length() == 11);
                canGetSmsCode.setValue(!isTiming && s.length() == 11);
                canLogin.setValue(checkLogin());
            }
        };
        //验证码文本监听
        smsCodeWatcher = new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isSmsCodeCorrect = (s.length() == 6);
                canLogin.setValue(checkLogin());
            }
        };
        //倒计时
        timer = new CountDownTimer(60 * 1000, 1000) {
            @Override
            public void onTick(long l) {
                smsCodeText.setValue(String.format(context.getString(R.string.string_regain_verification_code), String.valueOf(l / 1000)));
                isTiming = true;
                canGetSmsCode.setValue(false);
            }

            @Override
            public void onFinish() {
                smsCodeText.setValue("获取验证码");
                isTiming = false;
                canGetSmsCode.setValue(isPhoneNumCorrect);
            }
        };
    }

    /**
     * 是否可以登录
     * @return 如果满足前置条件，返回true，否则返回false
     */
    private boolean checkLogin() {
        return isCheckAgreement.getValue() && isPhoneNumCorrect && isSmsCodeCorrect;
    }

    /**
     * 手机号是否注册
     * @return 是否注册
     */
    public boolean isRegistered() {
        return userDAO.findUser(phoneNum.getValue()) != null;
    }

    /**
     * 随机生成验证码
     */
    public String generateSmsCode() {
        int max = 999999;
        int min = 100000;
        Random random = new Random();
        int res = random.nextInt(max) % (max - min + 1) + min;
        theSmsCode = String.valueOf(res);
        return String.valueOf(res);
    }

    /**
     * 登录
     */
    public boolean login() {
        return smsCode.getValue().equals(theSmsCode);
    }

    /*-----------------------------------------
     * 有可能改变数据，影响UI的操作
     * 与控件绑定，由控件事件调用
     */

    /**
     * 接受协议
     */
    public void checkAgreement() {
        isCheckAgreement.setValue(!isCheckAgreement.getValue());
        canLogin.setValue(checkLogin());
    }

    /**
     * 清除文本输入框内容
     */
    public void clearInput() {
        phoneNum.setValue("");
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.e(TAG, "ViewModel --> onCleared");
        timer.onFinish();
        timer.cancel();
        timer = null;
        context = null;
    }

    /*-----------------------------------------
     * Getter
     * 与控件属性绑定，数据改变时自动更新UI
     */
    public TextWatcher getSmsCodeWatcher() {
        return smsCodeWatcher;
    }

    public TextWatcher getPhoneNumWatcher() {
        return phoneNumWatcher;
    }

    public MutableLiveData<Boolean> getCanLogin() {
        return canLogin;
    }

    public MutableLiveData<Boolean> getIsCheckAgreement() {
        return isCheckAgreement;
    }

    public MutableLiveData<Integer> getShowClearBtn() {
        return showClearBtn;
    }

    public MutableLiveData<String> getPhoneNum() {
        return phoneNum;
    }

    public MutableLiveData<String> getSmsCode() {
        return smsCode;
    }

    public MutableLiveData<String> getSmsCodeText() {
        return smsCodeText;
    }

    public CountDownTimer getTimer() {
        return timer;
    }

    public MutableLiveData<Boolean> getCanGetSmsCode() {
        return canGetSmsCode;
    }
}
