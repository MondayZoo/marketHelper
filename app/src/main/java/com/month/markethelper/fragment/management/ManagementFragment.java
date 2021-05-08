package com.month.markethelper.fragment.management;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.month.markethelper.R;
import com.month.markethelper.activity.management.GoodsManagementActivity;
import com.month.markethelper.adapter.ManagementToolsAdapter;
import com.month.markethelper.bean.ToolBean;
import com.month.markethelper.fragment.tools.ToolsViewModel;
import com.month.markethelper.utils.EmptyMessage;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ManagementFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_management, container, false);
        initView(root);
        return root;
    }

    private void initView(View root) {
        RecyclerView recyclerView = root.findViewById(R.id.management_rv);

        List<ToolBean> data = new ArrayList<>();
        data.add(new ToolBean(R.drawable.ic_management_shangjia, "商品管理"));
        data.add(new ToolBean(R.drawable.ic_management_yulan, "店铺预览"));
        data.add(new ToolBean(R.drawable.ic_management_zhuxiao, "注销"));

        ManagementToolsAdapter adapter = new ManagementToolsAdapter(R.layout.item_management, data);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                switch (position) {
                    //商品管理
                    case 0:
                        Intent intent = new Intent(getActivity(), GoodsManagementActivity.class);
                        startActivity(intent);
                        break;
                    //店铺预览
                    case 1:

                        break;
                    //注销
                    case 2:
                        break;
                }
            }
        });
    }

}