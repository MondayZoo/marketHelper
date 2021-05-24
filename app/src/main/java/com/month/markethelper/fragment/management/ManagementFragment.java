package com.month.markethelper.fragment.management;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.month.markethelper.R;
import com.month.markethelper.activity.StoreInfoActivity;
import com.month.markethelper.activity.management.GoodsChoiceActivity;
import com.month.markethelper.activity.management.GoodsManagementActivity;
import com.month.markethelper.activity.management.InventoryActivity;
import com.month.markethelper.adapter.ManagementToolsAdapter;
import com.month.markethelper.bean.ToolBean;
import com.month.markethelper.utils.EmptyMessage;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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
        data.add(new ToolBean(R.drawable.ic_management_kucun, "库存管理"));
        data.add(new ToolBean(R.drawable.ic_management_diaohuo, "调货"));
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
                        Intent intent1 = new Intent(getActivity(), GoodsManagementActivity.class);
                        startActivity(intent1);
                        break;
                    //库存预览
                    case 1:
                        Intent intent2 = new Intent(getActivity(), InventoryActivity.class);
                        startActivity(intent2);
                        break;
                    //调货
                    case 2:
                        Intent intent3 = new Intent(getActivity(), GoodsChoiceActivity.class);
                        startActivity(intent3);
                        break;
                    //注销
                    case 4:
                        EventBus.getDefault().post(EmptyMessage.getInstance(EmptyMessage.STATE_STORE_LOGOUT));
                        Intent intent4 = new Intent(getActivity(), StoreInfoActivity.class);
                        startActivity(intent4);
                        break;
                }
            }
        });
    }

}