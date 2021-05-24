package com.month.markethelper.activity.management;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.month.markethelper.R;
import com.month.markethelper.adapter.GoodsInRepertoryAdapter;
import com.month.markethelper.base.BaseActivityWithViewModel;
import com.month.markethelper.databinding.ActivityInventoryBinding;
import com.month.markethelper.db.entity.Goods;
import com.month.markethelper.utils.DialogUtils;
import com.month.markethelper.utils.ToastUtils;

import java.util.List;

public class InventoryActivity extends BaseActivityWithViewModel<ActivityInventoryBinding> {

    private InventoryViewModel viewModel;

    //库存对话框
    private Dialog dialog;
    private EditText setEt;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_inventory;
    }

    @Override
    protected void initViewModel() {
        viewModel = new ViewModelProvider(this).get(InventoryViewModel.class);
        binding.setVm(viewModel);
        binding.setLifecycleOwner(this);
        viewModel.setStoreId(sharedPreferences.getLong("storeId", 0));
    }

    @Override
    protected void initView() {
        //初始化列表
        RecyclerView recyclerView = binding.inventoryRv;
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        GoodsInRepertoryAdapter adapter = new GoodsInRepertoryAdapter(this);
        recyclerView.setAdapter(adapter);
        viewModel.getGoods().observe(this, new Observer<List<Goods>>() {
            @Override
            public void onChanged(List<Goods> goods) {
                adapter.setList(goods);
            }
        });
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                Goods goods = (Goods) adapter.getData().get(position);
                //ToastUtils.showToast("选择的商品是：" + goods.getName());
                showDialog(goods);
            }
        });
    }

    //---------------------------Click Event Method--------------------------------
    private void showDialog(Goods goods) {
        if (dialog == null) {
            dialog = DialogUtils.createInventoryDialog(this);
            setEt = dialog.findViewById(R.id.set_et);
        }
        dialog.findViewById(R.id.yes_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String num = setEt.getText().toString();
                if (TextUtils.isEmpty(num)) {
                    ToastUtils.showToast("请输入正确的数字！");
                }
                else {
                    //ToastUtils.showToast("更新的商品是 -->" + goods.getName());
                    goods.setInventory(Integer.parseInt(num));
                    viewModel.update(goods);
                    dialog.dismiss();
                }
            }
        });
        setEt.setText(String.valueOf(goods.getInventory()));
        dialog.show();
    }
}