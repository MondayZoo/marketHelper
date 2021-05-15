package com.month.markethelper.activity.management;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.chad.library.adapter.base.listener.GridSpanSizeLookup;
import com.month.markethelper.R;
import com.month.markethelper.adapter.SimpleGoodsAdapter;
import com.month.markethelper.base.BaseActivityWithViewModel;
import com.month.markethelper.bean.GoodsAndTypeBean;
import com.month.markethelper.custom.TextFlowLayout;
import com.month.markethelper.databinding.ActivityGoodsManagementBinding;
import com.month.markethelper.db.entity.Goods;
import com.month.markethelper.db.entity.Store;
import com.month.markethelper.utils.DialogUtils;
import com.month.markethelper.utils.ToastUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GoodsManagementActivity extends BaseActivityWithViewModel<ActivityGoodsManagementBinding> implements View.OnClickListener, TextFlowLayout.OnFlowTextItemClickListener {

    private static final String TAG = "GoodsManagementActivity";

    private GoodsManagementViewModel viewModel;

    private long storeId;

    //新增分类对话框
    private Dialog dialog;
    private int categoryIndex = -1;

    //流式布局展示分类
    private TextFlowLayout textFlowLayout;
    private List<Integer> mark = new ArrayList<>();

    //商品列表
    private List<GoodsAndTypeBean> multipleData = new ArrayList<>();

    //分类操作模式。
    //默认为展示模式，为true时为删除模式
    private boolean opMode;

    //-------------------------------------Basal Method----------------------------------

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_goods_management;
    }

    @Override
    protected void initViewModel() {
        viewModel = new ViewModelProvider(this).get(GoodsManagementViewModel.class);
        binding.setVm(viewModel);
        binding.setLifecycleOwner(this);
    }

    @Override
    protected void initView() {
        //店铺id
        storeId = sharedPreferences.getLong("storeId", 0);
        Log.e(TAG, "storeId --> " + storeId);

        //☆ 分类管理
        //初始化流式布局
        textFlowLayout = binding.categoryList;
        textFlowLayout.setItemVerticalSpace(20);
        textFlowLayout.setItemHorizontalSpace(20);
        textFlowLayout.setOnFlowTextItemClickListener(this);
        //设置数据
        viewModel.getCategory(storeId).observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (!TextUtils.isEmpty(s)) {
                    String[] categories = s.split(";");
                    textFlowLayout.setTextList(Arrays.asList(categories));
                }
            }
        });

        //☆ 商品管理
        //初始化RecyclerView
        RecyclerView contentRv = binding.contentRv;
        contentRv.setLayoutManager(new GridLayoutManager(this, 2));
        //初始化适配器
        SimpleGoodsAdapter simpleGoodsAdapter = new SimpleGoodsAdapter(this);
        simpleGoodsAdapter.setGridSpanSizeLookup(new GridSpanSizeLookup() {
            @Override
            public int getSpanSize(@NonNull GridLayoutManager gridLayoutManager, int viewType, int position) {
                return multipleData.get(position).getSpanSize();
            }
        });
        //设置数据
        viewModel.getGoods(storeId).observe(this, new Observer<List<Goods>>() {
            @Override
            public void onChanged(List<Goods> goodsList) {
                multipleData.clear();
                //对商品进行分类
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    GoodsAndTypeBean bean;
                    Map<String, List<Goods>> collect = goodsList.stream().collect(Collectors.groupingBy(Goods::getType));
                    for (Map.Entry<String, List<Goods>> entry : collect.entrySet()) {
                        bean = new GoodsAndTypeBean(entry.getKey());
                        multipleData.add(bean);
                        for (Goods goods : entry.getValue()) {
                            bean = new GoodsAndTypeBean(goods, entry.getKey());
                            multipleData.add(bean);
                        }
                    }
                }
                simpleGoodsAdapter.setList(multipleData);
            }
        });
        //设置适配器
        contentRv.setAdapter(simpleGoodsAdapter);
    }

    @Override
    protected void initEvent() {
        binding.categoryAddTv.setOnClickListener(this);
        binding.categoryDeleteTv.setOnClickListener(this);
        binding.categoryConfirmTv.setOnClickListener(this);
        binding.categoryCancelTv.setOnClickListener(this);
        binding.contentIconTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        //弹出添加分类对话框
        if (id == R.id.category_add_tv) {
            showDialog("");
        }
        //确认更新分类
        else if (id == R.id.yes_tv) {
            updateCategory();
        }
        //取消更新分类
        else if (id == R.id.no_tv) {
            categoryIndex = -1;
            dialog.dismiss();
        }
        //删除分类
        else if (id == R.id.category_delete_tv) {
            switchOpMode();
            mark.clear();
        }
        //确认删除
        else if (id == R.id.category_confirm_tv) {
            switchOpMode();
            deleteCategory();
        }
        //取消删除
        else if (id == R.id.category_cancel_tv) {
            switchOpMode();
            textFlowLayout.showAllItem();
        }
        //新增商品
        else if (id == R.id.content_icon_tv) {
            Intent intent = new Intent(this, GoodsInfoActivity.class);
            intent.putExtra("state", 1);
            startActivity(intent);
        }
    }

    @Override
    public void onFlowTextItemClick(int index, String text) {
        ToastUtils.showToast(index + " " + text);
    }

    @Override
    public void onFlowTextItemLongChick(int index, String text) {
        categoryIndex = index;
        showDialog(text);
    }

    @Override
    public void onFlowTextItemDelete(int index) {
        textFlowLayout.hideItem(index);
        mark.add(index);
    }

    //-------------------------------------Click Event Method----------------------------------
    private void showDialog(String text) {
        if (null == dialog) {
            dialog = DialogUtils.createCategoryDialog(this);
        }
        dialog.findViewById(R.id.yes_tv).setOnClickListener(this);
        dialog.findViewById(R.id.no_tv).setOnClickListener(this);
        EditText editText = dialog.findViewById(R.id.category_et);
        editText.setText(text);
        dialog.show();
    }

    /**
     * 更新分类
     * 两种情况：
     *      1、新增一个分类
     *      2、修改原有的某个分类
     */
    private void updateCategory() {
        Store store = viewModel.getStoreInfo(storeId);
        EditText editText = dialog.findViewById(R.id.category_et);
        //新增分类
        if (categoryIndex == -1) {
            String category = store.getCategory() == null ? "" : store.getCategory();
            StringBuilder sb = new StringBuilder(category);
            sb.append(editText.getText().toString());
            sb.append(";");
            store.setCategory(sb.toString());
        }
        //更新原有分类
        else {
            String[] categories = store.getCategory().split(";");
            String newCategory = editText.getText().toString();
            categories[categoryIndex] = TextUtils.isEmpty(newCategory) ? categories[categoryIndex] : newCategory;
            StringBuilder sb = new StringBuilder();
            for (String s : categories) {
                sb.append(s);
                sb.append(";");
            }
            store.setCategory(sb.toString());
        }
        viewModel.updateCategory(store);
        categoryIndex = -1;
        dialog.dismiss();
    }

    private void deleteCategory() {
        Store store = viewModel.getStoreInfo(storeId);
        String[] categories = store.getCategory().split(";");
        for (Integer index : mark) {
            categories[index] = "";
        }
        StringBuilder sb = new StringBuilder();
        for (String category : categories) {
            if (!TextUtils.isEmpty(category)) {
                sb.append(category);
                sb.append(";");
            }
        }
        store.setCategory(sb.toString());
        viewModel.updateCategory(store);
    }

    /**
     *  切换操作模式 -- 展示模式 / 删除模式
     */
    private void switchOpMode() {
        opMode = !opMode;
        //删除模式
        if (opMode) {
            binding.categoryCancelTv.setVisibility(View.VISIBLE);
            binding.categoryConfirmTv.setVisibility(View.VISIBLE);
            binding.categoryAddTv.setVisibility(View.GONE);
            binding.categoryDeleteTv.setVisibility(View.GONE);
            textFlowLayout.showItemDeleteBtn();
        } else {
            binding.categoryCancelTv.setVisibility(View.GONE);
            binding.categoryConfirmTv.setVisibility(View.GONE);
            binding.categoryAddTv.setVisibility(View.VISIBLE);
            binding.categoryDeleteTv.setVisibility(View.VISIBLE);
            textFlowLayout.hideItemDeleteBtn();
        }
    }
}