package com.month.markethelper.activity.vm;

import android.service.autofill.UserData;

import com.month.markethelper.bean.GoodsAndCountBean;
import com.month.markethelper.db.MarketDatabase;
import com.month.markethelper.db.dao.AddressInfoDAO;
import com.month.markethelper.db.dao.DealDAO;
import com.month.markethelper.db.dao.GoodsDAO;
import com.month.markethelper.db.dao.StoreDAO;
import com.month.markethelper.db.dao.UserDAO;
import com.month.markethelper.db.entity.AddressInfo;
import com.month.markethelper.db.entity.Deal;
import com.month.markethelper.db.entity.Goods;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class OrderViewModel extends ViewModel {

    private AddressInfoDAO addressInfoDAO;
    private GoodsDAO goodsDAO;
    private UserDAO userDAO;
    private DealDAO dealDAO;
    private StoreDAO storeDAO;

    private String userPhoneNum;
    private long storeId;

    //保存用户设置的地址信息
    private AddressInfo addressInfo;

    //订单商品总价
    private MutableLiveData<Double> totalPrice;

    public OrderViewModel() {
        MarketDatabase marketDatabase = MarketDatabase.getInstance();
        addressInfoDAO = marketDatabase.getAddressInfoDao();
        goodsDAO = marketDatabase.getGoodsDao();
        dealDAO = marketDatabase.getDealDao();
        userDAO = marketDatabase.getUserDao();
        storeDAO = marketDatabase.getStoreDao();
        initLiveData();
    }

    private void initLiveData() {
        totalPrice = new MutableLiveData<>();
    }

    //----------------------------Database Operation---------------------------------
    public String getStoreName() {
        return storeDAO.getStoreName(storeId);
    }


    public List<GoodsAndCountBean> getGoodsInOrder(Map<Long, Integer> orderMap) {
        List<GoodsAndCountBean> result = new ArrayList<>();
        totalPrice.setValue(0d);
        for (Map.Entry<Long, Integer> entry : orderMap.entrySet()) {
            Goods goods = goodsDAO.findGoodsById(entry.getKey());
            int count = entry.getValue();
            GoodsAndCountBean goodsAndCountBean = new GoodsAndCountBean(goods, count);
            totalPrice.setValue(totalPrice.getValue() + goods.getPrice() * count);
            result.add(goodsAndCountBean);
        }
        return result;
    }

    /**
     * 新交易
     */
    public void newDeal(Map<Long, Integer> orderMap) {
        //处理商品信息
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Long, Integer> entry : orderMap.entrySet()) {
            long goodsId = entry.getKey();
            int amount = entry.getValue();
            //订单中的商品信息 格式：商品id,购买数量;
            sb.append(goodsId);
            sb.append(",");
            sb.append(amount);
            sb.append(";");
            //更新库存
            Goods goods = goodsDAO.findGoodsById(goodsId);
            goods.setInventory(goods.getInventory() - amount);
            goodsDAO.update(goods);
        }
        //待存储的交易类
        Deal deal = new Deal();
        //店铺信息
        deal.setStoreId(storeId);
        //商品信息
        deal.setGoods(sb.toString());
        deal.setTotalPrice(totalPrice.getValue());
        //用户信息
        deal.setAddressId(addressInfo.getId());                         //地址信息
        deal.setCustomerId(userDAO.findUser(userPhoneNum).getId());     //用户id
        //交易信息
        deal.setStatus("交易中");                                       //交易状态
        deal.setType("普通");                                           //交易类型
        //保存
        dealDAO.insert(deal);
    }
    //---------------------------- <> ---------------------------------


    //----------------------------Getter Method---------------------------------
    public void setUserPhoneNum(String userPhoneNum) {
        this.userPhoneNum = userPhoneNum;
    }

    public LiveData<List<AddressInfo>> getAddressInfoList() {
        return addressInfoDAO.findAllByPhoneNum(userPhoneNum);
    }

    public void setAddressInfo(AddressInfo addressInfo) {
        this.addressInfo = addressInfo;
    }

    public MutableLiveData<Double> getTotalPrice() {
        return totalPrice;
    }

    public long getStoreId() {
        return storeId;
    }

    public void setStoreId(long storeId) {
        this.storeId = storeId;
    }
}
