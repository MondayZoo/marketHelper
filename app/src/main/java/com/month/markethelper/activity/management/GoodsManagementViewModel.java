package com.month.markethelper.activity.management;

import com.month.markethelper.db.MarketDatabase;
import com.month.markethelper.db.dao.StoreDAO;
import com.month.markethelper.db.entity.Store;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class GoodsManagementViewModel extends ViewModel {

    private StoreDAO storeDAO;

    public GoodsManagementViewModel() {
        MarketDatabase marketDatabase = MarketDatabase.getInstance();
        storeDAO = marketDatabase.getStoreDao();
        initLiveData();
    }

    private void initLiveData() {

    }

    //----------------------------Database Operation---------------------------------
    public Store getStoreInfo(long storeId) {
        return storeDAO.findStoreById(storeId);
    }

    public void updateCategory(Store store) {
        storeDAO.update(store);
    }


    //----------------------------Getter Method---------------------------------
    public LiveData<String> getCategory(long storeId) {
        return storeDAO.getCategory(storeId);
    }
}
