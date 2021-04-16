package com.month.markethelper.activity.vm;

import com.month.markethelper.db.MarketDatabase;
import com.month.markethelper.db.dao.StoreDAO;
import com.month.markethelper.db.entity.Store;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class StoreInfoViewModel extends ViewModel {

    private StoreDAO storeDAO;

    private MutableLiveData<String> actionbarTitle;
    private MutableLiveData<String> actionbarRightText;

    //表单信息
    private String userPhoneNum;
    private MutableLiveData<String> storeName;
    private MutableLiveData<String> address;
    private MutableLiveData<String> type;
    private MutableLiveData<String> contactNumber;
    private MutableLiveData<String> intro;

    //详细地址（经纬度）
    private double latitude;
    private double longitude;

    public StoreInfoViewModel() {

        MarketDatabase marketDatabase = MarketDatabase.getInstance();
        storeDAO = marketDatabase.getStoreDao();
        initLiveData();
    }

    private void initLiveData() {
        actionbarTitle = new MutableLiveData<>();
        actionbarTitle.setValue("我的店铺");
        actionbarRightText = new MutableLiveData<>();
        actionbarRightText.setValue("新增");
        storeName = new MutableLiveData<>();
        storeName.setValue("");
        address = new MutableLiveData<>();
        address.setValue("");
        type = new MutableLiveData<>();
        type.setValue("");
        contactNumber = new MutableLiveData<>();
        contactNumber.setValue("");
        intro = new MutableLiveData<>();
        intro.setValue("");
    }

    //----------------------------Database Operation---------------------------------
    public void addNewStore() {
        Store store = new Store();
        //基础信息
        store.setPhoneNum(userPhoneNum);
        store.setStoreName(storeName.getValue());
        store.setAddress(address.getValue());
        store.setType(type.getValue());
        store.setContactNumber(contactNumber.getValue());
        store.setIntro(intro.getValue());
        //详细地址信息
        store.setLatitude(latitude);
        store.setLongitude(longitude);
        //附加信息
        store.setHot(0);
        store.setScore(3.0f);
        store.setEvaluation("新店开张，抢先体验");
        storeDAO.add(store);
    }

    public void updateStore(Store store) {
        store.setStoreName(storeName.getValue());
        store.setAddress(address.getValue());
        store.setType(type.getValue());
        store.setContactNumber(contactNumber.getValue());
        store.setIntro(intro.getValue());
        storeDAO.update(store);
    }

    public void deleteStore(Store store) {
        storeDAO.delete(store);
    }


    //---------------------------- <> ---------------------------------

    /**
     * 清除表单数据
     */
    public void clearForm() {
        storeName.setValue("");
        address.setValue("");
        type.setValue("");
        contactNumber.setValue("");
        intro.setValue("");
    }

    //----------------------------Getter Method---------------------------------

    public MutableLiveData<String> getActionbarTitle() {
        return actionbarTitle;
    }

    public MutableLiveData<String> getActionbarRightText() {
        return actionbarRightText;
    }

    public MutableLiveData<String> getStoreName() {
        return storeName;
    }

    public MutableLiveData<String> getAddress() {
        return address;
    }

    public MutableLiveData<String> getType() {
        return type;
    }

    public MutableLiveData<String> getContactNumber() {
        return contactNumber;
    }

    public MutableLiveData<String> getIntro() {
        return intro;
    }

    public LiveData<List<Store>> getStoreInfoList() {
        return storeDAO.findAllByPhoneNum(userPhoneNum);
    }

    public void setUserPhoneNum(String userPhoneNum) {
        this.userPhoneNum = userPhoneNum;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
