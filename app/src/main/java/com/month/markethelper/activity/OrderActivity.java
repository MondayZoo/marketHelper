package com.month.markethelper.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.gyf.immersionbar.ImmersionBar;
import com.month.markethelper.R;
import com.month.markethelper.activity.vm.OrderViewModel;
import com.month.markethelper.adapter.AddressInfoListAdapter;
import com.month.markethelper.adapter.GoodsInBagAdapter;
import com.month.markethelper.base.BaseActivityWithViewModel;
import com.month.markethelper.databinding.ActivityOrderBinding;
import com.month.markethelper.db.entity.AddressInfo;
import com.month.markethelper.utils.DialogUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderActivity extends BaseActivityWithViewModel<ActivityOrderBinding> implements View.OnClickListener {

    public static final String TAG = "OrderActivity";

    private OrderViewModel viewModel;

    //选择地址对话框相关
    private Dialog dialog;
    private String userPhoneNum;
    private AddressInfoListAdapter adapter;

    //商品列表
    private Map<Long, Integer> orderMap = new HashMap<>();

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_order;
    }

    @Override
    protected void initViewModel() {
        viewModel = new ViewModelProvider(this).get(OrderViewModel.class);
        binding.setVm(viewModel);
        binding.setLifecycleOwner(this);
        userPhoneNum = sharedPreferences.getString("user", null);
        viewModel.setUserPhoneNum(userPhoneNum);
    }

    @Override
    protected void initView() {
        //顶部栏
        ImmersionBar.with(this).statusBarColor(R.color.color_D10D0B).init();
        AppCompatTextView titleTv = findViewById(R.id.actionbar_title_tv);
        AppCompatTextView backTv = findViewById(R.id.actionbar_back_tv);
        titleTv.setText("提交订单");
        backTv.setOnClickListener(this);
        //选择地址列表的适配器
        adapter = new AddressInfoListAdapter(R.layout.item_address_info, 1);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                //选择的地址信息
                AddressInfo addressInfo = (AddressInfo) adapter.getData().get(position);
                //保存至ViewModel
                viewModel.setAddressInfo(addressInfo);
                //更新页面
                String str1 = addressInfo.getAddress() + addressInfo.getHouseNumber();
                binding.addressTv.setText(str1);
                binding.addressTv.setTextColor(getColor(R.color.black));

                String str2 = addressInfo.getContacts() +
                        (addressInfo.isGender() ? " 先生  " : " 女士  ") +
                        addressInfo.getContactNumber();
                binding.contactsTv.setText(str2);

                binding.contactsTv.setVisibility(View.VISIBLE);
                binding.addTv.setVisibility(View.GONE);
                //关闭dialog
                dialog.dismiss();
            }
        });
        //商品列表
        orderMap = (Map<Long, Integer>) getIntent().getSerializableExtra("orderMap");
        binding.storeNameTv.setText(getIntent().getStringExtra("storeName"));
        RecyclerView goodsRv = binding.goodsRv;
        goodsRv.setLayoutManager(new LinearLayoutManager(this));
        for (Map.Entry<Long, Integer> entry : orderMap.entrySet()) {
            Log.e(TAG, "购买了" + entry.getKey() + "号商品 数量为" + entry.getValue());
        }
        GoodsInBagAdapter goodsInBagAdapter = new GoodsInBagAdapter(this, R.layout.item_goods_in_order);
        goodsRv.setAdapter(goodsInBagAdapter);
        goodsInBagAdapter.setList(viewModel.getGoodsInOrder(orderMap));
    }

    @Override
    protected void initEvent() {
        binding.addressTv.setOnClickListener(this);
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
        //返回
        if (id == R.id.actionbar_back_tv) {
            finish();
        }
        //确认收货地址
        else if (id == R.id.address_tv) {
            showDialog();
        }
    }

    //--------------------------------Click Event Method-----------------------------

    /**
     * 展示地址选择对话框
     */
    private void showDialog() {
        //初始化Dialog
        if (dialog == null) {
            dialog = DialogUtils.createChooseAddressDialog(this);
            RecyclerView recyclerView = dialog.findViewById(R.id.address_rv);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);
        }
        dialog.show();
    }
}