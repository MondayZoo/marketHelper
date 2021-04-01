package com.month.markethelper.fragment.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.gyf.immersionbar.ImmersionBar;
import com.month.markethelper.MainActivity;
import com.month.markethelper.R;
import com.month.markethelper.activity.LoginActivity;
import com.month.markethelper.adapter.LeftCategoryListAdapter;
import com.month.markethelper.base.BaseFragment;
import com.month.markethelper.databinding.FragmentHomeBinding;
import com.month.markethelper.utils.EmptyMessage;
import com.youth.banner.Banner;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.holder.BannerImageHolder;
import com.youth.banner.indicator.CircleIndicator;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HomeFragment extends BaseFragment<FragmentHomeBinding> {

    private static final String TAG = "HomeFragment";

    private HomeViewModel homeViewModel;

    //----------------------------basal method---------------------------------

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initViewModel() {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding.setVm(homeViewModel);
    }

    @Override
    protected void initView(View rootView) {
        if (null != userPhoneNum) {
            binding.loginTipsLl.setVisibility(View.GONE);
        }
        initBanner();
        initList();
    }

    @Override
    protected void initEvent() {
        //跳转至登录页面
        binding.loginTv.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        });
    }

    //---------------------------- EventBus ------------------------------------
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveMessage(EmptyMessage message) {
        if (message.code == EmptyMessage.STATE_LOGIN) {
            binding.loginTipsLl.setVisibility(View.GONE);
        }
    }

    //------------------------- initialize method ------------------------------

    private void initBanner() {
        binding.banner.setAdapter(new BannerImageAdapter<Integer>(homeViewModel.getBannerData()) {
            @Override
            public void onBindView(BannerImageHolder holder, Integer url, int position, int size) {
                Glide.with(holder.itemView)
                        .load(url)
                        .into(holder.imageView);
            }
        })
                .addBannerLifecycleObserver(this)
                .setIndicator(new CircleIndicator(getContext()));
    }

    private void initList() {
        //左侧分类列表
        RecyclerView leftCategoryList = binding.leftCategoryList;
        leftCategoryList.setLayoutManager(new LinearLayoutManager(getActivity()));
        LeftCategoryListAdapter leftCategoryListAdapter = new LeftCategoryListAdapter(R.layout.item_left_category, homeViewModel.getCategoryData());
        leftCategoryList.setAdapter(leftCategoryListAdapter);
        leftCategoryListAdapter.setOnItemClickListener((adapter, view, position) -> {
            leftCategoryListAdapter.setSelectedPosition(position);
            leftCategoryListAdapter.notifyDataSetChanged();
        });
        //右侧内容列表
    }

    //----------------------- lifecycle -----------------------------

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.e(TAG, "onAttach");
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.e(TAG, "onDetach");
        EventBus.getDefault().unregister(this);
    }
}