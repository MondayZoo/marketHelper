package com.month.markethelper.fragment.store;

import com.month.markethelper.db.MarketDatabase;
import com.month.markethelper.db.dao.StoreDAO;
import com.month.markethelper.db.dao.UserDAO;
import com.month.markethelper.db.entity.Store;
import com.month.markethelper.db.entity.User;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DetailsViewModel extends ViewModel {

    private StoreDAO storeDAO;

    //当前店铺ID，用于数据库查询
    private long storeId;

    private MutableLiveData<Store> store;

    public DetailsViewModel() {
        MarketDatabase marketDatabase = MarketDatabase.getInstance();
        storeDAO = marketDatabase.getStoreDao();

        initLiveData();

    }

    private void initLiveData() {
        store = new MutableLiveData<>();
    }

    //----------------------------Getter Method---------------------------------
    public void setStoreId(long storeId) {
        this.storeId = storeId;
    }

    public void getStoreDetails() {
        store.setValue(storeDAO.findStoreById(storeId));
    }

    public MutableLiveData<Store> getStore() {
        return store;
    }
}