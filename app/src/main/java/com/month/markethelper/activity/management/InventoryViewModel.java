package com.month.markethelper.activity.management;

import com.month.markethelper.db.MarketDatabase;
import com.month.markethelper.db.dao.GoodsDAO;
import com.month.markethelper.db.dao.StoreDAO;
import com.month.markethelper.db.entity.Goods;
import com.month.markethelper.db.entity.Store;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class InventoryViewModel extends ViewModel {

    private StoreDAO storeDAO;
    private GoodsDAO goodsDAO;

    private Long storeId;

    public InventoryViewModel() {
        MarketDatabase marketDatabase = MarketDatabase.getInstance();
        storeDAO = marketDatabase.getStoreDao();
        goodsDAO = marketDatabase.getGoodsDao();
        initLiveData();
    }

    private void initLiveData() {

    }

    //----------------------------Database Operation---------------------------------
    public LiveData<List<Goods>> getGoods() {
        return goodsDAO.findGoodsByInventory(storeId);
    }

    public void update(Goods goods) {
        goodsDAO.update(goods);
    }



    //----------------------------Getter Method---------------------------------

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }
}
