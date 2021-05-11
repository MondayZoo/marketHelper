package com.month.markethelper.activity.management;

import com.month.markethelper.db.MarketDatabase;
import com.month.markethelper.db.dao.GoodsDAO;
import com.month.markethelper.db.dao.StoreDAO;
import com.month.markethelper.db.entity.Goods;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class GoodsInfoViewModel extends ViewModel {

    private GoodsDAO goodsDAO;
    private StoreDAO storeDAO;

    private long storeId;

    private MutableLiveData<String> goodsName;
    private MutableLiveData<String> goodsIntro;
    private MutableLiveData<String> goodsPrice;
    private MutableLiveData<String> goodsUnit;
    private MutableLiveData<String> goodsType;
    private String goodsPicUrl;

    public GoodsInfoViewModel() {
        MarketDatabase marketDatabase = MarketDatabase.getInstance();
        goodsDAO = marketDatabase.getGoodsDao();
        storeDAO = marketDatabase.getStoreDao();
        initLiveData();
    }

    private void initLiveData() {
        goodsName = new MutableLiveData<>();
        goodsName.setValue("");
        goodsIntro = new MutableLiveData<>();
        goodsIntro.setValue("");
        goodsUnit = new MutableLiveData<>();
        goodsUnit.setValue("");
        goodsPrice = new MutableLiveData<>();
        goodsPrice.setValue("");
        goodsType = new MutableLiveData<>();
        goodsType.setValue("");
    }

    //----------------------------Database Operation---------------------------------

    /**
     * 添加一个商品
     */
    public void addGoods() {
        Goods goods = new Goods();
        goods.setStoreId(storeId);
        goods.setStoreName(storeDAO.getStoreName(storeId));
        goods.setName(goodsName.getValue());
        goods.setIntro(goodsIntro.getValue());
        goods.setPrice(Double.parseDouble(goodsPrice.getValue()));
        goods.setUnit(goodsUnit.getValue());
        goods.setType(goodsType.getValue());
        goods.setUrl(goodsPicUrl);
        goodsDAO.addGoods(goods);
    }

    /**
     * 获取店铺分类
     * @return
     */
    public LiveData<String> getCategory() {
        return storeDAO.getCategoryLived(storeId);
    }

    //----------------------------Getter Method---------------------------------

    public void setStoreId(long storeId) {
        this.storeId = storeId;
    }

    public MutableLiveData<String> getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(MutableLiveData<String> goodsName) {
        this.goodsName = goodsName;
    }

    public MutableLiveData<String> getGoodsIntro() {
        return goodsIntro;
    }

    public void setGoodsIntro(MutableLiveData<String> goodsIntro) {
        this.goodsIntro = goodsIntro;
    }

    public MutableLiveData<String> getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(MutableLiveData<String> goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public MutableLiveData<String> getGoodsUnit() {
        return goodsUnit;
    }

    public void setGoodsUnit(MutableLiveData<String> goodsUnit) {
        this.goodsUnit = goodsUnit;
    }

    public String getGoodsPicUrl() {
        return goodsPicUrl;
    }

    public void setGoodsPicUrl(String goodsPicUrl) {
        this.goodsPicUrl = goodsPicUrl;
    }

    public MutableLiveData<String> getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(MutableLiveData<String> goodsType) {
        this.goodsType = goodsType;
    }
}
