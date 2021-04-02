package com.month.markethelper.activity.vm;

import com.month.markethelper.db.MarketDatabase;
import com.month.markethelper.db.dao.AddressInfoDAO;
import com.month.markethelper.db.entity.AddressInfo;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AddressInfoViewModel extends ViewModel {

    private AddressInfoDAO addressInfoDAO;

    private MutableLiveData<String> actionbarTitle;
    private MutableLiveData<String> actionbarRightText;

    //表单信息
    private long addressId;
    private String userPhoneNum;
    private MutableLiveData<String> address;
    private MutableLiveData<String> houseNum;
    private MutableLiveData<String> contacts;
    private MutableLiveData<Boolean> gender;
    private MutableLiveData<String> contactNumber;

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
        address = new MutableLiveData<>();
        address.setValue("");
        houseNum = new MutableLiveData<>();
        houseNum.setValue("");
        contacts = new MutableLiveData<>();
        contacts.setValue("");
        gender = new MutableLiveData<>();
        gender.setValue(true);
        contactNumber = new MutableLiveData<>();
        contactNumber.setValue("");
    }

    //----------------------------Database Operation---------------------------------
    public void addNewAddress() {
        AddressInfo addressInfo = new AddressInfo();
        addressInfo.setPhoneNum(userPhoneNum);
        addressInfo.setAddress(address.getValue());
        addressInfo.setHouseNumber(houseNum.getValue());
        addressInfo.setContacts(contacts.getValue());
        addressInfo.setGender(gender.getValue());
        addressInfo.setContactNumber(contactNumber.getValue());
        addressInfoDAO.add(addressInfo);
    }

    public void updateAddress() {
        AddressInfo addressInfo = new AddressInfo();
        addressInfo.setId(addressId);
        addressInfo.setPhoneNum(userPhoneNum);
        addressInfo.setAddress(address.getValue());
        addressInfo.setHouseNumber(houseNum.getValue());
        addressInfo.setContacts(contacts.getValue());
        addressInfo.setGender(gender.getValue());
        addressInfo.setContactNumber(contactNumber.getValue());
        addressInfoDAO.update(addressInfo);
    }

    public void deleteAddress() {
        AddressInfo addressInfo = new AddressInfo();
        addressInfo.setId(addressId);
        addressInfoDAO.delete(addressInfo);
    }
    //---------------------------- <> ---------------------------------

    /**
     * 清除表单数据
     */
    public void clearForm() {
        address.setValue("");
        houseNum.setValue("");
        contacts.setValue("");
        contactNumber.setValue("");
    }

    //----------------------------Getter Method---------------------------------

    public MutableLiveData<String> getActionbarTitle() {
        return actionbarTitle;
    }

    public MutableLiveData<String> getActionbarRightText() {
        return actionbarRightText;
    }

    public LiveData<List<AddressInfo>> getAddressInfoList() {
        return addressInfoDAO.findAllByPhoneNum(userPhoneNum);
    }

    public MutableLiveData<String> getAddress() {
        return address;
    }

    public MutableLiveData<String> getHouseNum() {
        return houseNum;
    }

    public MutableLiveData<String> getContacts() {
        return contacts;
    }

    public MutableLiveData<Boolean> getGender() {
        return gender;
    }

    public MutableLiveData<String> getContactNumber() {
        return contactNumber;
    }

    public void setAddressId(long addressId) {
        this.addressId = addressId;
    }

    public void setUserPhoneNum(String userPhoneNum) {
        this.userPhoneNum = userPhoneNum;
    }
}
