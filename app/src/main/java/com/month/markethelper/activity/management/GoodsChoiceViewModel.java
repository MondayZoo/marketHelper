package com.month.markethelper.activity.management;

import com.month.markethelper.db.MarketDatabase;
import com.month.markethelper.db.dao.GoodsDAO;
import com.month.markethelper.db.dao.StoreDAO;
import com.month.markethelper.db.entity.Goods;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class GoodsChoiceViewModel extends ViewModel {

    private GoodsDAO goodsDAO;

    private Long storeId;

    public GoodsChoiceViewModel() {
        MarketDatabase marketDatabase = MarketDatabase.getInstance();
        goodsDAO = marketDatabase.getGoodsDao();
        initLiveData();
    }

    private void initLiveData() {

    }

    //----------------------------Database Operation---------------------------------
    public LiveData<List<Goods>> getShortGoods() {
        return goodsDAO.findShortGoods(storeId);
    }

    //----------------------------Getter Method---------------------------------
    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }
}
