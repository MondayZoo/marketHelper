package com.month.markethelper.fragment.user;

import com.month.markethelper.db.MarketDatabase;
import com.month.markethelper.db.dao.UserDAO;
import com.month.markethelper.db.entity.User;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class UserViewModel extends ViewModel {

    private UserDAO userDAO;

    private MutableLiveData<User> userInfo;

    public UserViewModel() {
        MarketDatabase marketDatabase = MarketDatabase.getInstance();
        userDAO = marketDatabase.getUserDao();
        initLiveData();
    }

    private void initLiveData() {
        userInfo = new MutableLiveData<>();
    }

    public MutableLiveData<User> getUserInfo() {
        return userInfo;
    }

    public void getUserInfo(String phoneNum) {
        userInfo.setValue(userDAO.findUser(phoneNum));
    }
}