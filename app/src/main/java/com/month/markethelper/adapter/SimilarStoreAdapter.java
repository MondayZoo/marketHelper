package com.month.markethelper.adapter;

import android.content.Context;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.month.markethelper.R;
import com.month.markethelper.bean.SimilarDataBean;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SimilarStoreAdapter extends BaseQuickAdapter<SimilarDataBean, BaseViewHolder> {

    private Context context;

    public SimilarStoreAdapter(Context context) {
        super(R.layout.item_similar_store);
        this.context = context;
    }


    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, SimilarDataBean bean) {
        //店铺名
        baseViewHolder.setText(R.id.store_name_tv, bean.getStoreName());
        //距离
        DecimalFormat df = new DecimalFormat("0");
        baseViewHolder.setText(R.id.distance_tv, "距您" + df.format(bean.getDistance()) + "米");
        //是否是自家的其他店铺
        //  若是，则显示提示文本
        //  若不是，显示联系方式
        if (bean.isBelong()) {
            baseViewHolder.setVisible(R.id.phone_ll, false);
            baseViewHolder.setVisible(R.id.tips_tv, true);
        }
        //表中表
        RecyclerView recyclerView = baseViewHolder.findView(R.id.similar_goods_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        SimilarGoodsAdapter adapter = new SimilarGoodsAdapter(bean.getList(), context);
        recyclerView.setAdapter(adapter);
    }
}
