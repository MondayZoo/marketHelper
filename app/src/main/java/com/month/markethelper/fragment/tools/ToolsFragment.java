package com.month.markethelper.fragment.tools;

import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.month.markethelper.R;
import com.month.markethelper.adapter.OrderAdapter;
import com.month.markethelper.base.BaseFragment;
import com.month.markethelper.databinding.FragmentToolsBinding;
import com.month.markethelper.db.entity.Deal;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ToolsFragment extends BaseFragment<FragmentToolsBinding> {

    private static final String TAG = "ToolsFragment";

    private ToolsViewModel toolsViewModel;

    @Override
    protected void initViewModel() {
        toolsViewModel = new ViewModelProvider(this).get(ToolsViewModel.class);
        binding.setVm(toolsViewModel);
        toolsViewModel.setUserId(userPhoneNum);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_tools;
    }

    @Override
    protected void initView(View rootView) {
        RecyclerView recyclerView = binding.orderRv;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        OrderAdapter adapter;
        //用户状态，显示用户订单
        long storeId = sharedPreferences.getLong("storeId", 0);
        Log.e(TAG, "storeId --> " + storeId);
        if (storeId == 0) {
            adapter = new OrderAdapter(getActivity(), 0);
            recyclerView.setAdapter(adapter);
            toolsViewModel.getUserDeal().observe(getActivity(), new Observer<List<Deal>>() {
                @Override
                public void onChanged(List<Deal> deals) {
                    adapter.setList(deals);
                }
            });
            adapter.addChildClickViewIds(R.id.order_confirm_tv, R.id.order_refuse_tv);
            adapter.setOnItemChildClickListener(new OnItemChildClickListener() {
                @Override
                public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                    Deal deal = (Deal) adapter.getData().get(position);
                    //确认订单
                    if (view.getId() == R.id.order_confirm_tv) {
                        deal.setStatus("已完成");
                        toolsViewModel.updateDeal(deal);
                    }
                    //取消订单
                    else if (view.getId() == R.id.order_refuse_tv) {
                        deal.setStatus("已取消");
                        toolsViewModel.updateDeal(deal);
                        //回调商品库存
                        toolsViewModel.callbackGoods(deal);
                    }
                }
            });
        }
        //店铺状态，显示商家订单
        else {
            adapter = new OrderAdapter(getActivity(), 1);
            recyclerView.setAdapter(adapter);
            toolsViewModel.getStoreDeal(storeId).observe(getActivity(), new Observer<List<Deal>>() {
                @Override
                public void onChanged(List<Deal> deals) {
                    adapter.setList(deals);
                }
            });
        }
    }

    @Override
    protected void initEvent() {

    }
}