package com.month.markethelper.activity.management;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.month.markethelper.R;
import com.month.markethelper.base.BaseActivityWithViewModel;
import com.month.markethelper.databinding.ActivityTransferBinding;
import com.month.markethelper.db.entity.Goods;
import com.month.markethelper.utils.BitmapUtils;

public class TransferActivity extends BaseActivityWithViewModel<ActivityTransferBinding> {

    private TransferViewModel viewModel;

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
    }

    @Override
    protected void initView() {
        //获取上个页面传递的信息
        Goods goods = (Goods) getIntent().getSerializableExtra("goods");
        binding.goodsNameTv.setText(goods.getName());
        binding.goodsInventoryTv.setText("库存：" + goods.getInventory());
        Bitmap bitmap = BitmapUtils.stringToBitmap(goods.getUrl());
        Glide.with(this).load(bitmap).into(binding.goodsPicIv);
    }
}