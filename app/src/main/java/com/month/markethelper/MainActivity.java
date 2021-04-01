package com.month.markethelper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.gyf.immersionbar.ImmersionBar;
import com.month.markethelper.activity.LoginActivity;
import com.month.markethelper.base.BaseActivity;
import com.month.markethelper.utils.EmptyMessage;
import com.month.markethelper.utils.MapUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends BaseActivity {

    public static final String TAG = "MainActivity";

    private SharedPreferences sp;
    private String userPhoneNum;

    private BottomNavigationView navView;

    //---------------------------- Basal Method ------------------------------------
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        navView = findViewById(R.id.nav_view);
//        navView.getMenu().removeItem(R.id.navigation_home);

        sp = getSharedPreferences("data", MODE_PRIVATE);

//        //测试
//        SharedPreferences.Editor editor = sp.edit();
//        editor.putString("user", null);
//        editor.apply();

        //检查用户是否已登录
        checkLoginState();
    }

    //------------------------------------------------------------------------
    private void checkLoginState() {
        userPhoneNum = sp.getString("user", null);
        if (null != userPhoneNum) {
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
            NavigationUI.setupWithNavController(navView, navController);
        } else {
            navView.setOnNavigationItemSelectedListener(item -> {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                return false;
            });
        }
    }

    //---------------------------- EventBus ------------------------------------
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginStateChanged(EmptyMessage message) {
        switch (message.code) {
            case EmptyMessage.STATE_LOGIN:
            case EmptyMessage.STATE_LOGOUT:
                checkLoginState();
                break;
        }
    }

    //---------------------------- LifeCycle ------------------------------------
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.e(TAG, "Activity --> onNewIntent");
        checkLoginState();
    }
}