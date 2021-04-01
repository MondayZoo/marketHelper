package com.month.markethelper.activity.vm;

import com.month.markethelper.db.MarketDatabase;
import com.month.markethelper.db.dao.AddressInfoDAO;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AddressInfoViewModel extends ViewModel {

    private AddressInfoDAO addressInfoDAO;

    private MutableLiveData<String> actionbarTitle;
    private MutableLiveData<String> actionbarRightText;

    public AddressInfoViewModel() {

        MarketDatabase marketDatabase = MarketDatabase.getInstance();
        addressInfoDAO = marketDatabase.getAddressInfoDao();

        initLiveData();
    }

    private void initLiveData() {
        actionbarTitle = new MutableLiveData<>();
        actionbarTitle.setValue("我的收货地址");
        actionbarRightText = new MutableLiveData<>();
        actionbarRightText.setValue("新增");
    }

    //----------------------------Getter Method---------------------------------

    public MutableLiveData<String> getActionbarTitle() {
        return actionbarTitle;
    }

    public MutableLiveData<String> getActionbarRightText() {
        return actionbarRightText;
    }
}
