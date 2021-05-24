package com.month.markethelper.activity.management;

import com.month.markethelper.db.MarketDatabase;
import com.month.markethelper.db.dao.GoodsDAO;
import com.month.markethelper.db.dao.StoreDAO;
import com.month.markethelper.db.entity.Goods;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class TransferViewModel extends ViewModel {

    private StoreDAO storeDAO;
    private GoodsDAO goodsDAO;

    private Long storeId;

    public TransferViewModel() {
        MarketDatabase marketDatabase = MarketDatabase.getInstance();
        storeDAO = marketDatabase.getStoreDao();
        goodsDAO = marketDatabase.getGoodsDao();
        initLiveData();
    }

    private void initLiveData() {

    }

    //----------------------------Database Operation---------------------------------


    //----------------------------Getter Method---------------------------------
    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }
}
