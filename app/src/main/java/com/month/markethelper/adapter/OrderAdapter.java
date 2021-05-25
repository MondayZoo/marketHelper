package com.month.markethelper.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.month.markethelper.R;
import com.month.markethelper.bean.GoodsAndCountBean;
import com.month.markethelper.db.MarketDatabase;
import com.month.markethelper.db.dao.GoodsDAO;
import com.month.markethelper.db.dao.StoreDAO;
import com.month.markethelper.db.dao.UserDAO;
import com.month.markethelper.db.entity.Deal;
import com.month.markethelper.db.entity.Goods;
import com.month.markethelper.utils.BitmapUtils;

import org.jetbrains.annotations.NotNull;

public class OrderAdapter extends BaseQuickAdapter<Deal, BaseViewHolder> {

    private Context context;

    private StoreDAO storeDAO;
    private GoodsDAO goodsDAO;
    private UserDAO userDAO;

    private int mode;

    public OrderAdapter(Context context, int mode) {
        super(R.layout.item_order);
        this.context = context;
        this.mode = mode;

        MarketDatabase marketDatabase = MarketDatabase.getInstance();
        storeDAO = marketDatabase.getStoreDao();
        goodsDAO = marketDatabase.getGoodsDao();
        userDAO = marketDatabase.getUserDao();
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, Deal deal) {
        //*交易情况
        TextView statusTv = baseViewHolder.findView(R.id.deal_status_tv);
        statusTv.setText(deal.getStatus());
        statusTv.setTextColor(deal.getStatus().equals("交易中") ? context.getColor(R.color.green) : context.getColor(R.color.color_D10D0B));
        //用户订单
        if (mode == 0) {
            String storeName = storeDAO.getStoreName(deal.getStoreId());
            baseViewHolder.setText(R.id.store_name_tv, storeName == null ? "店铺已注销" : storeName);
            baseViewHolder.setVisible(R.id.order_confirm_tv, deal.getStatus().equals("交易中"));
            baseViewHolder.setVisible(R.id.order_refuse_tv, deal.getStatus().equals("交易中"));
        }
        //商家订单
        else {
            baseViewHolder.setText(R.id.store_name_tv, userDAO.findUserById(deal.getCustomerId()).getNickName());
            baseViewHolder.setVisible(R.id.order_confirm_tv, false);
            baseViewHolder.setVisible(R.id.order_refuse_tv, false);
        }
        //*商品信息
        baseViewHolder.setText(R.id.goods_price_tv, "￥" + deal.getTotalPrice());
        //*购买的商品id与数理
        String[] str1 = deal.getGoods().split(";");
        boolean flag = false;
        int count = 0;
        for (String s : str1) {
            String[] str2 = s.split(",");
            if (!flag) {
                //获取第一个购买的商品
                Goods goods = goodsDAO.findGoodsById(Long.parseLong(str2[0]));
                Bitmap bitmap = BitmapUtils.stringToBitmap(goods.getUrl());
                ImageView imgView = baseViewHolder.findView(R.id.goods_pic_iv);
                Glide.with(context).load(bitmap).into(imgView);
                //如果购买了多件商品
                if (str1.length > 1) {
                    baseViewHolder.setText(R.id.goods_name_tv, goods.getName() + "等");
                } else {
                    baseViewHolder.setText(R.id.goods_name_tv, goods.getName());
                }
                flag = true;
            }
            count += Integer.parseInt(str2[1]);
        }
        baseViewHolder.setText(R.id.goods_count_tv, "共" + count + "件");
    }
}

