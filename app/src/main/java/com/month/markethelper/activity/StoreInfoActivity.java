package com.month.markethelper.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.amap.api.location.AMapLocationClient;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnItemLongClickListener;
import com.gyf.immersionbar.ImmersionBar;
import com.month.markethelper.MainActivity;
import com.month.markethelper.R;
import com.month.markethelper.activity.vm.StoreInfoViewModel;
import com.month.markethelper.adapter.StoreInfoListAdapter;
import com.month.markethelper.base.BaseActivityWithViewModel;
import com.month.markethelper.databinding.ActivityStoreInfoBinding;
import com.month.markethelper.db.entity.Store;
import com.month.markethelper.utils.DialogUtils;
import com.month.markethelper.utils.EmptyMessage;
import com.month.markethelper.utils.MapUtils;
import com.month.markethelper.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class StoreInfoActivity extends BaseActivityWithViewModel<ActivityStoreInfoBinding> implements View.OnClickListener {

    private static final String TAG = "StoreInfoActivity";
    private StoreInfoViewModel viewModel;

    //用户手机号
    private String userPhoneNum;

    //待更新的store
    private Store store;

    /**
     * 页面状态
     * 0：列表状态
     * 1：新增状态
     * 2: 更新状态
     */
    private int state;

    private AppCompatTextView backTv, titleTv, rightTv;
    private LinearLayout storeFormLayout;
    private RecyclerView storeInfoList;
    private StoreInfoListAdapter adapter;

    /**
     * 定位获取地址
     */
    private Dialog mLoadingDialog;
    private AMapLocationClient mLocationClient;

    //----------------------------Basal Method---------------------------------

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_store_info;
    }

    @Override
    protected void initViewModel() {
        viewModel = new ViewModelProvider(this).get(StoreInfoViewModel.class);
        userPhoneNum = sharedPreferences.getString("user", null);
        viewModel.setUserPhoneNum(userPhoneNum);
        binding.setVm(viewModel);
        binding.setLifecycleOwner(this);
    }

    @Override
    protected void initView() {
        backTv = findViewById(R.id.actionbar_back_tv);
        titleTv = findViewById(R.id.actionbar_title_tv);
        rightTv = findViewById(R.id.actionbar_right_tv);
        ImmersionBar.with(this).statusBarColor(R.color.color_D10D0B).init();
        //表单布局
        storeFormLayout = binding.storeFormLl;
        //定位相关
        mLoadingDialog = DialogUtils.createLoadingDialog(this, "定位中...");
        mLocationClient = MapUtils.getLocationClient(this);
        //RecyclerView
        storeInfoList = binding.storeInfoRv;
        storeInfoList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new StoreInfoListAdapter(R.layout.item_store_info);
        storeInfoList.setAdapter(adapter);
    }

    @Override
    protected void initEvent() {
        backTv.setOnClickListener(this);
        rightTv.setOnClickListener(this);
        binding.addressTv.setOnClickListener(this);
        binding.storeSaveBtn.setOnClickListener(this);
        binding.storeDeleteBtn.setOnClickListener(this);
        //更新店铺信息
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                state = 2;
                store = (Store) adapter.getData().get(position);
                viewModel.getStoreName().setValue(store.getStoreName());
                viewModel.getAddress().setValue(store.getAddress());
                viewModel.getType().setValue(store.getType());
                viewModel.getContactNumber().setValue(store.getContactNumber());
                viewModel.getIntro().setValue(store.getIntro());
                showStoreInfoForm();
            }
        });
        //长按进入店铺
        adapter.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                //通知主页更新
                EventBus.getDefault().post(EmptyMessage.getInstance(EmptyMessage.STATE_STORE_LOGIN));
                //更新主页状态为商家状态
                store = (Store) adapter.getData().get(position);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putLong("storeId", store.getId());
                editor.apply();
                //跳转到主页
                Intent intent = new Intent(StoreInfoActivity.this, MainActivity.class);
                startActivity(intent);
                return true;
            }
        });
    }

    @Override
    protected void initObserver() {
        //Actionbar标题
        viewModel.getActionbarTitle().observe(this, s -> titleTv.setText(s));
        //Actionbar右文本
        viewModel.getActionbarRightText().observe(this, s -> rightTv.setText(s));
        //列表数据
        viewModel.getStoreInfoList().observe(this, new Observer<List<Store>>() {
            @Override
            public void onChanged(List<Store> stores) {
                adapter.setList(stores);
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        //返回键
        if (id == R.id.actionbar_back_tv) {
            back();
        }
        //新增
        else if (id == R.id.actionbar_right_tv) {
            openForm();
        }
        //自动定位获取店铺地址
        else if (id == R.id.address_tv) {
            getAddress();
        }
        //保存新店铺
        else if (id == R.id.store_save_btn) {
            if (state == 1) {
                viewModel.addNewStore();
            }
            else if (state == 2) {
                viewModel.updateStore(store);
            }
            showStoreInfoList();
        }
        //删除店铺
        else if (id == R.id.store_delete_btn) {
            viewModel.deleteStore(store);
            showStoreInfoList();
        }
    }

    @Override
    protected void release() {
        mLocationClient.stopLocation();
        mLocationClient.onDestroy();
        mLocationClient = null;
    }
    //----------------------------Click Event Method---------------------------------
    private void back() {
        switch (state) {
            case 0:
                finish();
                break;
            case 1:
            case 2:
                showStoreInfoList();
                break;
        }
    }

    /**
     * 打开编辑表单
     */
    private void openForm() {
        if (state == 0) {
            state = 1;
            showStoreInfoForm();
        }
    }

    /**
     * 定位获取地址
     */
    private void getAddress() {
        if (TextUtils.isEmpty(viewModel.getAddress().getValue())) {
            mLoadingDialog.show();
            mLocationClient.startLocation();
            mLocationClient.setLocationListener(aMapLocation -> {
                if (aMapLocation != null) {
                    if (aMapLocation.getErrorCode() == 0) {
                        viewModel.getAddress().setValue(aMapLocation.getAddress());
                        viewModel.setLatitude(aMapLocation.getLatitude());
                        viewModel.setLongitude(aMapLocation.getLongitude());
                        ToastUtils.showToast("定位成功！");
                    } else {
                        Log.e(TAG, "定位失败，错误码：" + aMapLocation.getErrorCode());
                        ToastUtils.showToast("定位失败，请手动输入地址。");
                    }
                }
                mLoadingDialog.dismiss();
            });
        }
    }

    /**
     * 切换
     */
    private void showStoreInfoList() {
        state = 0;
        storeFormLayout.setVisibility(View.GONE);
        storeInfoList.setVisibility(View.VISIBLE);
        binding.storeDeleteBtn.setVisibility(View.GONE);
        viewModel.getActionbarRightText().setValue("新增");
        viewModel.getActionbarTitle().setValue("我的店铺");
        viewModel.clearForm();
        store = null;
    }

    private void showStoreInfoForm() {
        if (state == 2) {
            binding.storeDeleteBtn.setVisibility(View.VISIBLE);
        }
        storeFormLayout.setVisibility(View.VISIBLE);
        storeInfoList.setVisibility(View.GONE);
        viewModel.getActionbarRightText().setValue("");
        viewModel.getActionbarTitle().setValue("编辑店铺");
    }
}