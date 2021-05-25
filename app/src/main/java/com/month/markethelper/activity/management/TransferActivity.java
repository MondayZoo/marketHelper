package com.month.markethelper.activity.management;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.graphics.Bitmap;

import com.bumptech.glide.Glide;
import com.month.markethelper.R;
import com.month.markethelper.adapter.SimilarStoreAdapter;
import com.month.markethelper.base.BaseActivityWithViewModel;
import com.month.markethelper.bean.SimilarDataBean;
import com.month.markethelper.databinding.ActivityTransferBinding;
import com.month.markethelper.db.entity.Goods;
import com.month.markethelper.utils.BitmapUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransferActivity extends BaseActivityWithViewModel<ActivityTransferBinding> {

    private TransferViewModel viewModel;

    //选中的商品
    private Goods mGoods;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_transfer;
    }

    @Override
    protected void initViewModel() {
        viewModel = new ViewModelProvider(this).get(TransferViewModel.class);
        binding.setVm(viewModel);
        binding.setLifecycleOwner(this);
        viewModel.setStoreId(sharedPreferences.getLong("storeId", 0));
        viewModel.setPhoneNum(sharedPreferences.getString("user", null));
    }

    @Override
    protected void initView() {
        //获取上个页面传递的信息
        mGoods = viewModel.getGoodsById(getIntent().getLongExtra("goodsId", 0));
        binding.goodsNameTv.setText(mGoods.getName());
        binding.goodsInventoryTv.setText("库存：" + mGoods.getInventory());
        Bitmap bitmap = BitmapUtils.stringToBitmap(mGoods.getUrl());
        Glide.with(this).load(bitmap).into(binding.goodsPicIv);
        //初始化类似商品列表
        binding.similarRv.setLayoutManager(new LinearLayoutManager(this));
        SimilarStoreAdapter adapter = new SimilarStoreAdapter(this);
        binding.similarRv.setAdapter(adapter);
        List<SimilarDataBean> similarData = viewModel.getSimilarData(mGoods.getName());
        adapter.setList(similarData);
    }


}