package com.month.markethelper.activity.management;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.month.markethelper.R;
import com.month.markethelper.adapter.GoodsCategoryAdapter;
import com.month.markethelper.base.BaseActivityWithViewModel;
import com.month.markethelper.custom.TextFlowLayout;
import com.month.markethelper.databinding.ActivityGoodsManagementBinding;
import com.month.markethelper.db.entity.Store;
import com.month.markethelper.utils.DialogUtils;
import com.month.markethelper.utils.ToastUtils;

import java.util.Arrays;

public class GoodsManagementActivity extends BaseActivityWithViewModel<ActivityGoodsManagementBinding> implements View.OnClickListener, TextFlowLayout.OnFlowTextItemClickListener {

    private static final String TAG = "GoodsManagementActivity";

    private GoodsManagementViewModel viewModel;

    private long storeId;

    //新增分类对话框
    private Dialog dialog;
    private int categoryIndex = -1;

    private TextFlowLayout categoryList;

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
        categoryList = binding.categoryList;
        categoryList.setItemVerticalSpace(20);
        categoryList.setItemHorizontalSpace(20);
        categoryList.setOnFlowTextItemClickListener(this);
        //设置数据
        viewModel.getCategory(storeId).observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (!TextUtils.isEmpty(s)) {
                    String[] category = s.split(";");
                    categoryList.setTextList(Arrays.asList(category));
                }
            }
        });

        //☆ 商品管理
        //初始化RecyclerView
        RecyclerView contentRv = binding.contentRv;
        contentRv.setLayoutManager(new GridLayoutManager(this, 2));

    }

    @Override
    protected void initEvent() {
        binding.categoryAddTv.setOnClickListener(this);
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
            String[] category = store.getCategory().split(";");
            String newCategory = editText.getText().toString();
            category[categoryIndex] = TextUtils.isEmpty(newCategory) ? category[categoryIndex] : newCategory;
            StringBuilder sb = new StringBuilder();
            for (String s : category) {
                sb.append(s);
                sb.append(";");
            }
            store.setCategory(sb.toString());
        }
        viewModel.updateCategory(store);
        categoryIndex = -1;
        dialog.dismiss();
    }

}