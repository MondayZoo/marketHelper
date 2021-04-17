package com.month.markethelper.activity;

import android.util.Log;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.month.markethelper.R;
import com.month.markethelper.adapter.StoreAdapter;
import com.month.markethelper.base.BaseActivity;
import com.month.markethelper.utils.EmptyMessage;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import androidx.annotation.NonNull;
import androidx.viewpager2.widget.ViewPager2;

public class StoreActivity extends BaseActivity implements View.OnClickListener {

    public static final String TAG = "StoreActivity";

    //----------------------------Basal Method---------------------------------

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_store;
    }


    @Override
    protected void initView() {
        //初始化顶部导航栏
        long storeId = getIntent().getLongExtra("storeId", 0);
        TabLayout tabLayout = findViewById(R.id.store_indicator);
        ViewPager2 viewPager2 = findViewById(R.id.store_pager);
        StoreAdapter storeAdapter = new StoreAdapter(this, storeId);
        viewPager2.setAdapter(storeAdapter);
        String[] title = new String[]{"菜单", "评论", "商家"};
        new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(title[position]);
            }
        }).attach();


        Log.e(TAG, "store id is --> " + storeId);
    }

    @Override
    protected void initEvent() {
        findViewById(R.id.back_tv).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.back_tv) {
            finish();
        }
    }

    //----------------------------Click Event Method---------------------------------


    //---------------------------- EventBus ------------------------------------
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveMessage(EmptyMessage message) {

    }
}