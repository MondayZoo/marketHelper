package com.month.markethelper.fragment.home;

import com.month.markethelper.R;
import com.month.markethelper.db.MarketDatabase;
import com.month.markethelper.db.dao.StoreDAO;
import com.month.markethelper.db.entity.Store;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    //轮播图
    private final List<Integer> bannerData = new ArrayList<>();
    //左侧分类
    private final List<String> categoryData = new ArrayList<>();

    private StoreDAO storeDAO;

    public HomeViewModel() {

        MarketDatabase marketDatabase = MarketDatabase.getInstance();
        storeDAO = marketDatabase.getStoreDao();

        initData();
    }

    private void initData() {
        initBannerData();
        initCategoryData();
    }

    private void initBannerData() {
        bannerData.add(R.drawable.banner1);
        bannerData.add(R.drawable.banner2);
        bannerData.add(R.drawable.banner3);
        bannerData.add(R.drawable.banner4);
    }

    private void initCategoryData() {
        categoryData.add("平台推荐");
        categoryData.add("附近热销");
        categoryData.add("饱腹正餐");
        categoryData.add("汉堡炸鸡");
        categoryData.add("奶茶饮品");
        categoryData.add("零食小吃");
    }

    //----------------------------Getter Method---------------------------------

    public List<Integer> getBannerData() {
        return bannerData;
    }

    public List<String> getCategoryData() {
        return categoryData;
    }

    public LiveData<List<Store>> getContentData() {
        return storeDAO.findAll();
    }
}