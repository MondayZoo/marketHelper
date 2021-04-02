package com.month.markethelper.activity;

import android.app.Dialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.amap.api.location.AMapLocationClient;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.gyf.immersionbar.ImmersionBar;
import com.month.markethelper.R;
import com.month.markethelper.activity.vm.AddressInfoViewModel;
import com.month.markethelper.adapter.AddressInfoListAdapter;
import com.month.markethelper.base.BaseActivityWithViewModel;
import com.month.markethelper.databinding.ActivityAddressInfoBinding;
import com.month.markethelper.db.entity.AddressInfo;
import com.month.markethelper.utils.DialogUtils;
import com.month.markethelper.utils.MapUtils;
import com.month.markethelper.utils.ToastUtils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AddressInfoActivity extends BaseActivityWithViewModel<ActivityAddressInfoBinding> implements View.OnClickListener {

    private static final String TAG = "AddressInfoActivity";
    private AddressInfoViewModel viewModel;

    //用户手机号
    private String userPhoneNum;

    /**
     * 页面状态
     * 0：列表状态
     * 1：新增状态
     * 2: 更新状态
     */
    private int state;

    private AppCompatTextView backTv, titleTv, rightTv;
    private LinearLayout addressFormLayout;
    private RecyclerView addressInfoList;
    private AddressInfoListAdapter adapter;

    /**
     * 定位获取地址
     */
    private Dialog mLoadingDialog;
    private AMapLocationClient mLocationClient;

    //----------------------------Basal Method---------------------------------

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_address_info;
    }

    @Override
    protected void initViewModel() {
        viewModel = new ViewModelProvider(this).get(AddressInfoViewModel.class);
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
        addressFormLayout = binding.addressFormLl;
        ImmersionBar.with(this).statusBarColor(R.color.color_D10D0B).init();
        //定位相关
        mLoadingDialog = DialogUtils.createLoadingDialog(this, "定位中...");
        mLocationClient = MapUtils.getLocationClient(this);
        //RecyclerView
        addressInfoList = binding.addressInfoRv;
        addressInfoList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AddressInfoListAdapter(R.layout.item_address_info);
        addressInfoList.setAdapter(adapter);
    }

    @Override
    protected void initEvent() {
        backTv.setOnClickListener(this);
        rightTv.setOnClickListener(this);
        binding.addressTv.setOnClickListener(this);
        binding.addressSaveBtn.setOnClickListener(this);
        binding.addressDeleteBtn.setOnClickListener(this);
        binding.genderSwitch.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                viewModel.getGender().setValue(checkedId == R.id.male_rb);
            }
        });
        //更新地址信息
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                state = 2;
                AddressInfo addressInfo = ((List<AddressInfo>) adapter.getData()).get(position);
                viewModel.getAddress().setValue(addressInfo.getAddress());
                viewModel.getHouseNum().setValue(addressInfo.getHouseNumber());
                viewModel.getContacts().setValue(addressInfo.getContacts());
                viewModel.getContactNumber().setValue(addressInfo.getContactNumber());
                viewModel.getGender().setValue(addressInfo.isGender());
                viewModel.setAddressId(addressInfo.getId());
                showAddressInfoForm();
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
        viewModel.getAddressInfoList().observe(this, new Observer<List<AddressInfo>>() {
            @Override
            public void onChanged(List<AddressInfo> addressInfos) {
                adapter.setList(addressInfos);
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
        //自动定位获取收获地址
        else if (id == R.id.address_tv) {
            getAddress();
        }
        //保存新地址
        else if (id == R.id.address_save_btn) {
            if (state == 1) {
                viewModel.addNewAddress();
            }
            else if (state == 2) {
                viewModel.updateAddress();
            }
            showAddressInfoList();
        }
        //删除地址
        else if (id == R.id.address_delete_btn) {
            viewModel.deleteAddress();
            showAddressInfoList();
        }
    }

    @Override
    protected void release() {
        mLocationClient.stopLocation();
        mLocationClient.onDestroy();
        MapUtils.destroyLocationClient();
    }
    //----------------------------Click Event Method---------------------------------
    private void back() {
        switch (state) {
            case 0:
                finish();
                break;
            case 1:
            case 2:
                showAddressInfoList();
                break;
        }
    }

    /**
     * 打开编辑表单
     */
    private void openForm() {
        if (state == 0) {
            state = 1;
            showAddressInfoForm();
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
                        ToastUtils.showToast("定位成功！");
                    } else {
                        Log.e(TAG, "定位失败，错误码：" + aMapLocation.getErrorCode());
                        ToastUtils.showToast("定位失败，请手动输入地址。");
                    }
                }
                mLoadingDialog.dismiss();
                //mLocationClient.stopLocation();
            });
        }
    }

    /**
     * 切换
     */
    private void showAddressInfoList() {
        state = 0;
        addressFormLayout.setVisibility(View.GONE);
        addressInfoList.setVisibility(View.VISIBLE);
        binding.addressDeleteBtn.setVisibility(View.GONE);
        viewModel.getActionbarRightText().setValue("新增");
        viewModel.getActionbarTitle().setValue("我的收货地址");
        viewModel.clearForm();
    }

    private void showAddressInfoForm() {
        if (state == 2) {
            binding.addressDeleteBtn.setVisibility(View.VISIBLE);
        }
        addressFormLayout.setVisibility(View.VISIBLE);
        addressInfoList.setVisibility(View.GONE);
        viewModel.getActionbarRightText().setValue("");
        viewModel.getActionbarTitle().setValue("编辑收货地址");
    }
}