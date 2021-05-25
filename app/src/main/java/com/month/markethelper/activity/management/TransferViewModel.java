package com.month.markethelper.activity.management;

import android.text.TextUtils;

import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.model.LatLng;
import com.month.markethelper.bean.SimilarDataBean;
import com.month.markethelper.db.MarketDatabase;
import com.month.markethelper.db.dao.GoodsDAO;
import com.month.markethelper.db.dao.StoreDAO;
import com.month.markethelper.db.entity.Goods;
import com.month.markethelper.db.entity.Store;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class TransferViewModel extends ViewModel {

    private StoreDAO storeDAO;
    private GoodsDAO goodsDAO;

    private Long storeId;
    private Store store;
    private String phoneNum;

    public TransferViewModel() {
        MarketDatabase marketDatabase = MarketDatabase.getInstance();
        storeDAO = marketDatabase.getStoreDao();
        goodsDAO = marketDatabase.getGoodsDao();
        initLiveData();
    }

    private void initLiveData() {

    }

    /**
     * 获取相似商品的适配数据
     * @param name  商品名
     */
    public List<SimilarDataBean> getSimilarData(String name) {
        int length = name.length();
        String pattern1 = name.substring(length - 2, length);   //最后两个字
        String pattern2 = name.substring(0, 2);   //开头两个字
        //查找相似商品
        List<Goods> list = getSimilarGoods("%" + pattern1 + "%", "%" + pattern2 + "%");
        //将来自同一个店铺的商品集合
        HashMap<Long, List<Goods>> map = new HashMap<>();
        for (Goods goods : list) {
            //获取商品对应的店铺id
            long storeId = goods.getStoreId();
            if (!map.containsKey(storeId)) {
                List<Goods> goodsList = new ArrayList<>();
                goodsList.add(goods);
                map.put(storeId, goodsList);
            } else {
                List<Goods> goodsList = map.get(storeId);
                goodsList.add(goods);
            }
        }
        //获取自身店铺，用于计算距离等
        store = storeDAO.findStoreById(storeId);
        //整合成列表适配所需的Bean类
        List<SimilarDataBean> result = new ArrayList<>();
        for (Map.Entry<Long, List<Goods>> entry : map.entrySet()) {
            //排除自己
            if (!entry.getKey().equals(storeId)) {
                SimilarDataBean bean = new SimilarDataBean();
                Store target = storeDAO.findStoreById(entry.getKey());
                //店名
                bean.setStoreName(target.getStoreName());
                //联系方式
                bean.setContactNum(target.getContactNumber());
                //计算距离
                LatLng latLng1 = new LatLng(store.getLatitude(), store.getLongitude());
                LatLng latLng2 = new LatLng(target.getLatitude(), target.getLongitude());
                bean.setDistance(AMapUtils.calculateLineDistance(latLng1, latLng2));
                //是否是自家店铺
                bean.setBelong(storeDAO.findStoreBelong(phoneNum, target.getId()) > 0);
                //相似商品
                bean.setList(entry.getValue());
                result.add(bean);
            }
        }
        return result;
    }

    //----------------------------Database Operation---------------------------------
    public Goods getGoodsById(long goodsId) {
        return goodsDAO.findGoodsById(goodsId);
    }

    public List<Goods> getSimilarGoods(String pattern1, String pattern2) {
        return goodsDAO.findSimilarGoods(pattern1, pattern2);
    }

    //----------------------------Getter Method---------------------------------
    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }
}
