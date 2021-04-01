package com.month.markethelper.fragment.home;

import android.app.Application;
import android.content.Context;

import com.month.markethelper.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private final List<Integer> bannerData = new ArrayList<>();

    private final List<String> categoryData = new ArrayList<>();

    public HomeViewModel() {
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

    public List<Integer> getBannerData() {
        return bannerData;
    }

    public List<String> getCategoryData() {
        return categoryData;
    }

}