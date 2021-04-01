package com.month.markethelper.base;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

public abstract class BaseFragment<B extends ViewDataBinding> extends Fragment {

    private static final String TAG = "BaseFragment";

    protected String userPhoneNum;
    protected B binding;

    protected SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e(TAG,"onCreateView");
        binding = DataBindingUtil.inflate(inflater, getLayoutResId(), container, false);
        View rootView = binding.getRoot();
//        View rootView = inflater.inflate(getLayoutResId(), container, false);
        sharedPreferences = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        userPhoneNum = sharedPreferences.getString("user", null);
        initViewModel();
        initView(rootView);
        initEvent();
        return rootView;
    }

    //初始化ViewModel
    protected abstract void initViewModel();

    //获取Fragment的layoutId
    protected abstract int getLayoutResId();

    //初始化View
    protected abstract void initView(View rootView);

    //初始化Event
    protected abstract void initEvent();


}
