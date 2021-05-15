package com.month.markethelper.fragment.store;

import com.month.markethelper.bean.GoodsAndCountBean;
import com.month.markethelper.db.MarketDatabase;
import com.month.markethelper.db.dao.GoodsDAO;
import com.month.markethelper.db.dao.StoreDAO;
import com.month.markethelper.db.entity.Goods;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MenuViewModel extends ViewModel {

    private StoreDAO storeDAO;
    private GoodsDAO goodsDAO;

    //商户的id
    private long storeId;

    //购物数
    private MutableLiveData<Integer> shoppingCount;

    public MenuViewModel() {
        MarketDatabase marketDatabase = MarketDatabase.getInstance();
        storeDAO = marketDatabase.getStoreDao();
        goodsDAO = marketDatabase.getGoodsDao();
        initLiveData();

    }

    private void initLiveData() {
        shoppingCount = new MutableLiveData<>();
        shoppingCount.setValue(0);
    }


    //--------------------------DataBase Operation----------------
    public List<String> getCategoryList() {
        String categories = storeDAO.getCategory(storeId);
        return Arrays.asList(categories.split(";"));
    }

    public LiveData<List<Goods>> getGoods() {
        return goodsDAO.findAllGoodsByStoreId(storeId);
    }

    public List<GoodsAndCountBean> getGoodsInBag(Map<Long, Integer> orderMap) {
        List<GoodsAndCountBean> result = new ArrayList<>();
        for (Map.Entry<Long, Integer> entry : orderMap.entrySet()) {
            Goods goods = goodsDAO.findGoodsById(entry.getKey());
            GoodsAndCountBean goodsAndCountBean = new GoodsAndCountBean(goods, entry.getValue());
            result.add(goodsAndCountBean);
        }
        return result;
    }
    //--------------------------Getter Method---------------------

    public void setStoreId(long storeId) {
        this.storeId = storeId;
    }

    public MutableLiveData<Integer> getShoppingCount() {
        return shoppingCount;
    }

    public void setShoppingCount(int count) {
        shoppingCount.setValue(count);
    }
}