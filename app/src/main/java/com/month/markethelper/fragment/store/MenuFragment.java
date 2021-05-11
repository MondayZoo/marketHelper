package com.month.markethelper.fragment.store;

import android.os.Bundle;
import android.view.View;

import com.month.markethelper.R;
import com.month.markethelper.adapter.GoodsInMenuAdapter;
import com.month.markethelper.adapter.LeftCategoryListAdapter;
import com.month.markethelper.base.BaseFragment;
import com.month.markethelper.bean.SimpleGoodsBean;
import com.month.markethelper.databinding.FragmentStoreDetialsBinding;
import com.month.markethelper.databinding.FragmentStoreMenuBinding;
import com.month.markethelper.db.entity.Goods;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MenuFragment extends BaseFragment<FragmentStoreMenuBinding> implements View.OnClickListener{

    private MenuViewModel viewModel;

    private List<SimpleGoodsBean> multipleData = new ArrayList<>();

    //---------------------------- Constructor --------------------------------
    private MenuFragment() {};

    public static MenuFragment newInstance(long storeId) {
        MenuFragment fragment = new MenuFragment();
        Bundle args = new Bundle();
        args.putLong("storeId", storeId);
        fragment.setArguments(args);
        return fragment;
    }

    //---------------------------- Basal Method ---------------------------------

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_store_menu;
    }

    @Override
    protected void initViewModel() {
        viewModel = new ViewModelProvider(this).get(MenuViewModel.class);
        binding.setVm(viewModel);
        if (getArguments() != null) {
            viewModel.setStoreId(getArguments().getLong("storeId"));
        }
    }

    @Override
    protected void initView(View rootView) {
        //获取分类列表
        List<String> categoryList = viewModel.getCategoryList();
        if (categoryList != null) {
            //左侧分类列表
            RecyclerView leftCategoryList = binding.leftCategoryList;
            leftCategoryList.setLayoutManager(new LinearLayoutManager(getActivity()));
            LeftCategoryListAdapter leftCategoryListAdapter = new LeftCategoryListAdapter(R.layout.item_left_category, categoryList);
            leftCategoryList.setAdapter(leftCategoryListAdapter);
            leftCategoryListAdapter.setOnItemClickListener((adapter, view, position) -> {
                leftCategoryListAdapter.setSelectedPosition(position);
                leftCategoryListAdapter.notifyDataSetChanged();
            });
            //右侧商品列表
            RecyclerView rightContentList = binding.rightContentList;
            rightContentList.setLayoutManager(new LinearLayoutManager(getActivity()));
            GoodsInMenuAdapter goodsInMenuAdapter = new GoodsInMenuAdapter(getActivity());
            rightContentList.setAdapter(goodsInMenuAdapter);
            //设置数据
            viewModel.getGoods().observe(this, goodsList -> {
                multipleData.clear();
                //对商品进行分类
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    SimpleGoodsBean bean;
                    Map<String, List<Goods>> collect = goodsList.stream().collect(Collectors.groupingBy(Goods::getType));
                    for (String type : categoryList) {
                        bean = new SimpleGoodsBean(type);
                        multipleData.add(bean);
                        if (collect.get(type) != null) {
                            for (Goods goods : collect.get(type)) {
                                //fixme
                                bean = new SimpleGoodsBean(goods);
                                multipleData.add(bean);
                            }
                        }
                    }
                    //这种分类方式，会导致顺序不一致
//                for (Map.Entry<String, List<Goods>> entry : collect.entrySet()) {
//                    bean = new SimpleGoodsBean(entry.getKey());
//                    multipleData.add(bean);
//                    for (Goods goods : entry.getValue()) {
//                        bean = new SimpleGoodsBean(goods);
//                        multipleData.add(bean);
//                    }
//                }
                }
                goodsInMenuAdapter.setList(multipleData);
            });
        }
    }

    @Override
    protected void initEvent() {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

    }

    //---------------------------- < > ---------------------------------

}