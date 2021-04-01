package com.month.markethelper.activity.vm;

import android.app.Application;
import android.app.Dialog;
import android.text.TextUtils;

import com.month.markethelper.base.BaseApplication;
import com.month.markethelper.db.MarketDatabase;
import com.month.markethelper.db.dao.UserDAO;
import com.month.markethelper.db.entity.User;
import com.month.markethelper.db.repository.UserRepository;
import com.month.markethelper.utils.DialogUtils;
import com.month.markethelper.utils.ToastUtils;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class RegisterViewModel extends AndroidViewModel {

    //数据仓库类
    private UserDAO userDao;

    //注册表单信息
    private MutableLiveData<String> phoneNum;
    private MutableLiveData<String> realName;
    private MutableLiveData<String> nickName;

    public RegisterViewModel(Application application) {
        super(application);
        MarketDatabase marketDatabase = MarketDatabase.getInstance();
        userDao = marketDatabase.getUserDao();
        initLiveData();
    }

    private void initLiveData() {
        phoneNum = new MutableLiveData<>();
        phoneNum.setValue("");
        realName = new MutableLiveData<>();
        realName.setValue("");
        nickName = new MutableLiveData<>();
        nickName.setValue("");
    }

    /*-----------------------------------------
     * 有可能改变数据，影响UI的操作
     * 与控件绑定，由控件事件调用
     */


    /**
     * --------------------------------------
     * Getter And Setter Method
     */

    public MutableLiveData<String> getPhoneNum() {
        return phoneNum;
    }

    public MutableLiveData<String> getRealName() {
        return realName;
    }

    public MutableLiveData<String> getNickName() {
        return nickName;
    }

    /**
     * --------------------------------------
     * Database Operation
     */
    public void register() {
        User user = new User();
        user.setPhoneNum(phoneNum.getValue());
        user.setRealName(realName.getValue());
        user.setNickName(nickName.getValue());
        userDao.register(user);
    }

    public User search(String phoneNum) {
        return userDao.findUser(phoneNum);
    }

    //此手机号是否已注册
    public boolean isRegistered() {
        return userDao.findUser(phoneNum.getValue()) != null;
    }
}
