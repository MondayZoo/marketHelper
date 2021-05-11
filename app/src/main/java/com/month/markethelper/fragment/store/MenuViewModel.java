package com.month.markethelper.fragment.store;

import com.month.markethelper.db.MarketDatabase;
import com.month.markethelper.db.dao.GoodsDAO;
import com.month.markethelper.db.dao.StoreDAO;
import com.month.markethelper.db.entity.Goods;

import java.util.Arrays;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class MenuViewModel extends ViewModel {

    private StoreDAO storeDAO;
    private GoodsDAO goodsDAO;

    //商户的id
    private long storeId;

    public MenuViewModel() {
        MarketDatabase marketDatabase = MarketDatabase.getInstance();
        storeDAO = marketDatabase.getStoreDao();
        goodsDAO = marketDatabase.getGoodsDao();
        initLiveData();

    }

    private void initLiveData() {

    }


    //--------------------------DataBase Operation----------------
    public List<String> getCategoryList() {
        String categories = storeDAO.getCategory(storeId);
        return Arrays.asList(categories.split(";"));
    }

    public LiveData<List<Goods>> getGoods() {
        return goodsDAO.findAllGoodsByStoreId(storeId);
    }
    //--------------------------Getter Method---------------------

    public void setStoreId(long storeId) {
        this.storeId = storeId;
    }
}