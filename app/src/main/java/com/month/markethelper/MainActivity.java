package com.month.markethelper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.month.markethelper.activity.LoginActivity;
import com.month.markethelper.base.BaseActivity;
import com.month.markethelper.utils.EmptyMessage;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends BaseActivity {

    public static final String TAG = "MainActivity";

    private SharedPreferences sp;
    private String userPhoneNum;
    private int state;

    private BottomNavigationView navView;
    private NavController navController;

    //---------------------------- Basal Method ------------------------------------
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {

        navView = findViewById(R.id.nav_view);
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        //默认用户状态，隐藏商家的导航栏
        navView.getMenu().findItem(R.id.navigation_management).setVisible(false);

        sp = getSharedPreferences("data", MODE_PRIVATE);

//        //测试
//        SharedPreferences.Editor editor = sp.edit();
//        editor.putString("user", null);
//        editor.apply();

        //登录状态检查
        checkLoginState();
    }

    //------------------------------------------------------------------------
    private void checkLoginState() {
        userPhoneNum = sp.getString("user", null);
        //登录状态
        if (null != userPhoneNum) {
            NavigationUI.setupWithNavController(navView, navController);
            //用户登录状态
            if (state == 1) {
                navView.getMenu().findItem(R.id.navigation_management).setVisible(false);
                navView.getMenu().findItem(R.id.navigation_user).setVisible(true);
                navController.navigate(R.id.navigation_home);
            }
            //商家登录状态
            else if (state == 2) {
                navView.getMenu().findItem(R.id.navigation_management).setVisible(true);
                navView.getMenu().findItem(R.id.navigation_user).setVisible(false);
                navController.navigate(R.id.navigation_management);
            }
            //商家注销状态 --> 特殊的用户登录状态
            else if (state == 3) {
                navView.getMenu().findItem(R.id.navigation_management).setVisible(false);
                navView.getMenu().findItem(R.id.navigation_user).setVisible(true);
                navController.navigate(R.id.navigation_user);
                //清空storeId
                SharedPreferences.Editor editor = sp.edit();
                editor.putLong("storeId", 0);
                editor.apply();
            }
        }
        //用户注销状态
        else {
            navController.navigate(R.id.navigation_home);
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
            case EmptyMessage.STATE_USER_LOGOUT:
                state = 0;
                checkLoginState();
                break;
            case EmptyMessage.STATE_USER_LOGIN:
                state = 1;
                break;
            case EmptyMessage.STATE_STORE_LOGIN:
                state = 2;
                break;
            case EmptyMessage.STATE_STORE_LOGOUT:
                state = 3;
                checkLoginState();
                break;
        }
    }

    //---------------------------- LifeCycle ------------------------------------
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.e(TAG, "Activity --> onNewIntent");
        //提交订单回到主页
        if (intent.getIntExtra("order", 0) == 1) {
            navController.navigate(R.id.navigation_tools);
        }
        //其他情况下跳转主页
        else {
            checkLoginState();
        }
    }
}