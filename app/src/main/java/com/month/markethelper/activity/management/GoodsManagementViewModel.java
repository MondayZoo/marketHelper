package com.month.markethelper.activity.management;

import com.month.markethelper.db.MarketDatabase;
import com.month.markethelper.db.dao.GoodsDAO;
import com.month.markethelper.db.dao.StoreDAO;
import com.month.markethelper.db.entity.Goods;
import com.month.markethelper.db.entity.Store;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class GoodsManagementViewModel extends ViewModel {

    private StoreDAO storeDAO;
    private GoodsDAO goodsDAO;

    public GoodsManagementViewModel() {
        MarketDatabase marketDatabase = MarketDatabase.getInstance();
        storeDAO = marketDatabase.getStoreDao();
        goodsDAO = marketDatabase.getGoodsDao();
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

    public LiveData<List<Goods>> getGoods(long storeId) {
        return goodsDAO.findAllGoodsByStoreId(storeId);
    }
    //----------------------------Getter Method---------------------------------
    public LiveData<String> getCategory(long storeId) {
        return storeDAO.getCategoryLived(storeId);
    }
}
