package com.month.markethelper.activity.vm;

import com.month.markethelper.bean.GoodsAndCountBean;
import com.month.markethelper.db.MarketDatabase;
import com.month.markethelper.db.dao.AddressInfoDAO;
import com.month.markethelper.db.dao.GoodsDAO;
import com.month.markethelper.db.entity.AddressInfo;
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

    private String userPhoneNum;

    //保存用户设置的地址信息
    private AddressInfo addressInfo;

    //订单商品总价
    private MutableLiveData<Double> totalPrice;

    public OrderViewModel() {
        MarketDatabase marketDatabase = MarketDatabase.getInstance();
        addressInfoDAO = marketDatabase.getAddressInfoDao();
        goodsDAO = marketDatabase.getGoodsDao();
        initLiveData();
    }

    private void initLiveData() {
        totalPrice = new MutableLiveData<>();
    }

    //----------------------------Database Operation---------------------------------

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
}
