package com.month.markethelper.fragment.tools;

import com.month.markethelper.db.MarketDatabase;
import com.month.markethelper.db.dao.DealDAO;
import com.month.markethelper.db.dao.GoodsDAO;
import com.month.markethelper.db.dao.UserDAO;
import com.month.markethelper.db.entity.Deal;
import com.month.markethelper.db.entity.Goods;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ToolsViewModel extends ViewModel {

    private DealDAO dealDAO;
    private UserDAO userDAO;
    private GoodsDAO goodsDAO;

    private long userId;

    public ToolsViewModel() {
        MarketDatabase marketDatabase = MarketDatabase.getInstance();
        dealDAO = marketDatabase.getDealDao();
        userDAO = marketDatabase.getUserDao();
        goodsDAO = marketDatabase.getGoodsDao();
    }

    //------------------------Database Operation----------------------------
    public LiveData<List<Deal>> getUserDeal() {
        return dealDAO.getDealByUserId(userId);
    }

    public LiveData<List<Deal>> getStoreDeal(long storeId) {
        return dealDAO.getDealByStoreId(storeId);
    }

    public void updateDeal(Deal deal) {
        dealDAO.update(deal);
    }

    public void callbackGoods(Deal deal) {
        String[] str1 = deal.getGoods().split(";");
        for (String s : str1) {
            String[] str2 = s.split(",");
            Goods goods = goodsDAO.findGoodsById(Long.parseLong(str2[0]));
            goods.setInventory(goods.getInventory() + Integer.parseInt(str2[1]));
            goodsDAO.update(goods);
        }
    }
    //-------------------------Get/Set Method--------------------------------
    public void setUserId(String userPhoneNum) {
        userId = userDAO.findUser(userPhoneNum).getId();
    }

}