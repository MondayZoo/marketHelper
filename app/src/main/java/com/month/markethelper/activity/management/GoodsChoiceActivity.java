package com.month.markethelper.activity.management;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.month.markethelper.R;
import com.month.markethelper.adapter.GoodsInChoiceAdapter;
import com.month.markethelper.base.BaseActivity;
import com.month.markethelper.base.BaseActivityWithViewModel;
import com.month.markethelper.databinding.ActivityChooseGoodsBinding;
import com.month.markethelper.db.entity.Goods;

import java.util.List;

public class GoodsChoiceActivity extends BaseActivityWithViewModel<ActivityChooseGoodsBinding> {

    private GoodsChoiceViewModel viewModel;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_choose_goods;
    }

    @Override
    protected void initViewModel() {
        viewModel = new ViewModelProvider(this).get(GoodsChoiceViewModel.class);
        binding.setVm(viewModel);
        binding.setLifecycleOwner(this);
        viewModel.setStoreId(sharedPreferences.getLong("storeId", 0));
    }

    @Override
    protected void initView() {
        RecyclerView recyclerView = binding.goodsChoiceRv;
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        GoodsInChoiceAdapter goodsInChoiceAdapter = new GoodsInChoiceAdapter(this);
        recyclerView.setAdapter(goodsInChoiceAdapter);
        viewModel.getShortGoods().observe(this, new Observer<List<Goods>>() {
            @Override
            public void onChanged(List<Goods> goods) {
                goodsInChoiceAdapter.setList(goods);
            }
        });
        goodsInChoiceAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                Goods goods = (Goods) adapter.getData().get(position);
                Intent intent = new Intent(GoodsChoiceActivity.this, TransferActivity.class);
                intent.putExtra("goods", goods);
                startActivity(intent);
            }
        });
    }
}