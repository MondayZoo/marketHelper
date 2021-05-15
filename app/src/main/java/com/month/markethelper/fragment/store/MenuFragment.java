package com.month.markethelper.fragment.store;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.month.markethelper.R;
import com.month.markethelper.adapter.GoodsInBagAdapter;
import com.month.markethelper.adapter.GoodsInMenuAdapter;
import com.month.markethelper.adapter.LeftCategoryListAdapter;
import com.month.markethelper.base.BaseFragment;
import com.month.markethelper.bean.GoodsAndCountBean;
import com.month.markethelper.bean.GoodsAndTypeBean;
import com.month.markethelper.databinding.FragmentStoreMenuBinding;
import com.month.markethelper.db.entity.Goods;
import com.month.markethelper.utils.DialogUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MenuFragment extends BaseFragment<FragmentStoreMenuBinding> implements View.OnClickListener{

    public static final String TAG = "MenuFragment";

    private MenuViewModel viewModel;

    private final List<GoodsAndTypeBean> multipleData = new ArrayList<>();

    //购物袋相关
    private RelativeLayout menuRl;      //菜单总布局，购物袋动画用
    private TextView bagTv;             //购物袋图片容器，购物袋动画用
    private Map<Long, Integer> orderMap = new HashMap<>();    //加入购物袋的商品id
    private double totalPrice;          //购物袋中的商品总价
    private int count;
    //购物详情对话框
    private Dialog dialog;
    //对话框中的相关View
    private TextView dialogCountTv;
    private TextView dialogPriceTv;
    private RecyclerView dialogDetailList;
    private GoodsInBagAdapter goodsInBagAdapter;

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
        //初始化View
        menuRl = binding.menuRl;
        bagTv = binding.shoppingBagTv;
        //初始化菜单商品列表
        initMenuList();
        //初始化购物袋
        initShoppingBag();
    }

    @Override
    protected void initEvent() {
        //购物数量
        viewModel.getShoppingCount().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                count = integer;
                if (integer > 0) {
                    binding.shoppingCountTv.setVisibility(View.VISIBLE);
                    binding.shoppingCountTv.setText(String.valueOf(integer));
                    binding.shoppingSubmitTv.setEnabled(true);
                } else {
                    binding.shoppingCountTv.setVisibility(View.GONE);
                    binding.shoppingSubmitTv.setEnabled(false);
                }
            }
        });
        //购物袋详情
        binding.shoppingBagRl.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        //显示购物袋对话框
        if (id == R.id.shopping_bag_rl) {
            showDetailDialog();
        }
        //清空购物袋
        else if (id == R.id.clear_tv) {
            orderMap.clear();
            viewModel.setShoppingCount(0);
            totalPrice = 0;
            binding.shoppingCountTv.setVisibility(View.GONE);
            binding.shoppingDetailsTv.setText("空空如也~");
            binding.shoppingDetailsTv.setTextSize(13);
            dialog.dismiss();
        }
        //隐藏购物袋
        else if (id == R.id.dismiss_view || id == R.id.shopping_bag_rl2) {
            binding.shoppingDetailsTv.setText(totalPrice == 0 ? "空空如也~" : "￥" + totalPrice);
            dialog.dismiss();
        }
    }

    //--------------------------Click Event Method---------------------

    /**
     * 展示购物袋对话框
     */
    private void showDetailDialog() {
        //初始化购物袋对话框
        if (dialog == null) {
            dialog = DialogUtils.createShoppingBagDialog(getActivity());
            dialogCountTv = dialog.findViewById(R.id.shopping_count_tv);
            dialogPriceTv = dialog.findViewById(R.id.shopping_details_tv);
            dialogDetailList = dialog.findViewById(R.id.shopping_details_rv);
            //购物袋列表
            dialogDetailList.setLayoutManager(new LinearLayoutManager(getActivity()));
            goodsInBagAdapter = new GoodsInBagAdapter(getActivity(), R.layout.item_goods_in_bag);
            dialogDetailList.setAdapter(goodsInBagAdapter);
            //点击事件
            //fixme 图片闪烁问题
            goodsInBagAdapter.addChildClickViewIds(R.id.goods_add_tv, R.id.goods_reduce_tv, R.id.clear_tv);
            goodsInBagAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
                @Override
                public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                    int id = view.getId();
                    GoodsAndCountBean goodsAndCountBean = (GoodsAndCountBean) adapter.getData().get(position);
                    Goods goods = goodsAndCountBean.getGoods();
                    long goodsId = goods.getId();
                    if (id == R.id.goods_add_tv) {
                        orderMap.put(goodsId, orderMap.get(goodsId) + 1);
                        viewModel.setShoppingCount(viewModel.getShoppingCount().getValue() + 1);
                        totalPrice += goods.getPrice();
                    }
                    else if (id == R.id.goods_reduce_tv) {
                        int count = orderMap.get(goodsId);
                        if (count > 1) {
                            orderMap.put(goodsId, orderMap.get(goodsId) - 1);
                        } else {
                            orderMap.remove(goodsId);
                        }
                        viewModel.setShoppingCount(viewModel.getShoppingCount().getValue() - 1);
                        totalPrice -= goods.getPrice();
                        if (orderMap.size() == 0) {
                            binding.shoppingDetailsTv.setTextSize(13);
                            binding.shoppingDetailsTv.setText("空空如也~");
                            dialog.dismiss();
                            return;
                        }
                    }
                    goodsInBagAdapter.setList(viewModel.getGoodsInBag(orderMap));
                    dialogCountTv.setText(String.valueOf(count));
                    dialogPriceTv.setText(getString(R.string.string_goods_total_price, String.valueOf(totalPrice)));
                }
            });
            //清空购物车
            TextView dialogClearTv = dialog.findViewById(R.id.clear_tv);
            dialogClearTv.setOnClickListener(this);
            //隐藏对话框
            View dialogDismissView = dialog.findViewById(R.id.dismiss_view);
            RelativeLayout dialogRl = dialog.findViewById(R.id.shopping_bag_rl2);
            dialogDismissView.setOnClickListener(this);
            dialogRl.setOnClickListener(this);
        }
        //展示购物袋（已购物）
        if (orderMap.size() > 0) {
            dialogCountTv.setText(String.valueOf(count));
            dialogPriceTv.setText(getString(R.string.string_goods_total_price, String.valueOf(totalPrice)));
            goodsInBagAdapter.setList(viewModel.getGoodsInBag(orderMap));
            dialog.show();
        }
    }


    //----------------------------Method---------------------------------

    /**
     * 初始化商品列表
     */
    private void initMenuList() {
        //获取分类列表
        List<String> categoryList = viewModel.getCategoryList();
        if (categoryList != null) {
            //左侧分类列表
            RecyclerView leftCategoryList = binding.leftCategoryList;
            leftCategoryList.setLayoutManager(new LinearLayoutManager(getActivity()));
            LeftCategoryListAdapter leftCategoryListAdapter = new LeftCategoryListAdapter(R.layout.item_left_category, categoryList);
            leftCategoryList.setAdapter(leftCategoryListAdapter);
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
                    GoodsAndTypeBean bean;
                    Map<String, List<Goods>> collect = goodsList.stream().collect(Collectors.groupingBy(Goods::getType));
                    for (String type : categoryList) {
                        bean = new GoodsAndTypeBean(type);
                        multipleData.add(bean);
                        if (collect.get(type) != null) {
                            for (Goods goods : collect.get(type)) {
                                //fixme
                                bean = new GoodsAndTypeBean(goods, type);
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
            //左右侧列表联动
            //左侧点击，右侧滑动
            leftCategoryListAdapter.setOnItemClickListener((adapter, view, position) -> {
                leftCategoryListAdapter.setSelectedPosition(position);
                leftCategoryListAdapter.notifyDataSetChanged();
                for (int i = 0; i < multipleData.size(); i++) {
                    if (categoryList.get(position).equals(multipleData.get(i).getTitle())) {
                        smoothMoveToPosition(rightContentList, i);
                        break;
                    }
                }
            });
            //右侧滑动，左侧选择
            rightContentList.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    //第一个可见的Item位置
                    int firstItemPosition = recyclerView.getChildLayoutPosition(recyclerView.getChildAt(0));
                    //该Item的分类
                    String type = multipleData.get(firstItemPosition).getTitle();
                    if (!TextUtils.isEmpty(type)) {
                        leftCategoryListAdapter.setSelectedPosition(categoryList.indexOf(type));
                        leftCategoryListAdapter.notifyDataSetChanged();
                    }
                }
            });
            //右侧点击事件
            goodsInMenuAdapter.addChildClickViewIds(R.id.goods_buy_tv);
            goodsInMenuAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                    //购物车动画
                    addCart((TextView) view);
                    //暂存商品信息
                    GoodsAndTypeBean goodsBean = (GoodsAndTypeBean) adapter.getData().get(position);
                    Goods goods = goodsBean.getGoods();
                    Log.e(TAG, "选择了商品 [" +  goods.getName() + "] 商品序号 -> " + goods.getId());
                    orderMap.put(goods.getId(), orderMap.getOrDefault(goods.getId(), 0) + 1);
                    totalPrice += goods.getPrice();
                    binding.shoppingDetailsTv.setText(getString(R.string.string_goods_total_price, String.valueOf(totalPrice)));
                    binding.shoppingDetailsTv.setTextSize(18);
                }
            });
        }
    }

    //目标项是否在最后一个可见项之后
    private boolean mShouldScroll;

    //记录目标位置
    private int mPosition;

    /**
     * @param recyclerView  需要滑动的RecyclerView
     * @param position      滑动的目标位置
     */
    private void smoothMoveToPosition(RecyclerView recyclerView, int position) {
        int firstItemPosition = -1;
        int lastItemPosition = -1;

        //获取第一个和最后一个可见Item的位置
        firstItemPosition = recyclerView.getChildLayoutPosition(recyclerView.getChildAt(0));
        lastItemPosition = recyclerView.getChildLayoutPosition(recyclerView.getChildAt(recyclerView.getChildCount() - 1));

        Log.e(TAG, "firstItemPosition --> " + firstItemPosition);
        Log.e(TAG, "lastItemPosition --> " + lastItemPosition);

        //不同情况处理
        //1.跳转位置在第一个可见位置之前
        if (position < firstItemPosition) {
            //直接跳转
            recyclerView.smoothScrollToPosition(position);
        }
        //2.跳转位置在第一个和最后一个可见位置之间
        //由于此Item可见，scrollToPosition方法将不起作用
        else if (position <= lastItemPosition) {
            int movePosition = position - firstItemPosition;
            if (movePosition >= 0 && movePosition < recyclerView.getChildCount()) {
                int top = recyclerView.getChildAt(movePosition).getTop();
                recyclerView.scrollBy(0, top);
            }
        }
        //3.跳转位置在最后一个可见位置之后
        else {
            recyclerView.smoothScrollToPosition(position);
            mPosition = position;
            mShouldScroll = true;

            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (mShouldScroll && newState == RecyclerView.SCROLL_STATE_IDLE) {
                        mShouldScroll = false;
                        smoothMoveToPosition(recyclerView, mPosition);
                    }
                }
            });
        }
    }


    /**
     * 初始化购物袋
     */
    private void initShoppingBag() {
        //基础属性
        binding.shoppingCountTv.setVisibility(View.GONE);
        binding.shoppingDetailsTv.setText("空空如也~");
        binding.shoppingDetailsTv.setTextSize(14);
        binding.shoppingSubmitTv.setEnabled(false);
        //购物袋详情对话框
        binding.shoppingBagRl.setOnClickListener(this);
    }

    private PathMeasure mPathMeasure;
    //贝塞尔曲线中间过程的点坐标
    private final float[] mCurrentPosition = new float[2];

    /**
     * ★★★★★把商品添加到购物袋的动画效果★★★★★
     * @param tv
     */
    private void addCart(TextView tv) {
        //一、创造出执行动画的主题---imageview
        //代码new一个imageview，图片资源是上面的imageview的图片
        // (这个图片就是执行动画的图片，从开始位置出发，经过一个抛物线（贝塞尔曲线），移动到购物袋里)
        final ImageView goods = new ImageView(getActivity());
        goods.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ui_shape_add_cart_anime, null));
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(50, 50);
        menuRl.addView(goods, params);

        //二、计算动画开始/结束点的坐标的准备工作
        //得到父布局的起始点坐标（用于辅助计算动画开始/结束时的点的坐标）
        int[] parentLocation = new int[2];
        menuRl.getLocationInWindow(parentLocation);

        //得到商品图片的坐标（用于计算动画开始的坐标）
        int[] startLoc = new int[2];
        tv.getLocationInWindow(startLoc);

        //得到购物袋图片的坐标(用于计算动画结束后的坐标)
        int[] endLoc = new int[2];
        bagTv.getLocationInWindow(endLoc);

        //三、正式开始计算动画开始/结束的坐标
        //开始掉落的商品的起始点：商品起始点-父布局起始点+该商品图片的一半
        float startX = startLoc[0] - parentLocation[0] + tv.getWidth() / 2;
        float startY = startLoc[1] - parentLocation[1] + tv.getHeight() / 2;

        //商品掉落后的终点坐标：购物袋起始点-父布局起始点+购物袋图片的1/5
        float toX = endLoc[0] - parentLocation[0] + bagTv.getWidth() / 5;
        float toY = endLoc[1] - parentLocation[1];

        //四、计算中间动画的插值坐标（贝塞尔曲线）（其实就是用贝塞尔曲线来完成起终点的过程）
        //开始绘制贝塞尔曲线
        Path path = new Path();
        //移动到起始点（贝塞尔曲线的起点）
        path.moveTo(startX, startY);
        //使用二次萨贝尔曲线：注意第一个起始坐标越大，贝塞尔曲线的横向距离就会越大，一般按照下面的式子取即可
        path.quadTo((startX + toX) / 2, startY, toX, toY);
        //mPathMeasure用来计算贝塞尔曲线的曲线长度和贝塞尔曲线中间插值的坐标，
        // 如果是true，path会形成一个闭环
        mPathMeasure = new PathMeasure(path, false);

        //★★★属性动画实现（从0到贝塞尔曲线的长度之间进行插值计算，获取中间过程的距离值）
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, mPathMeasure.getLength());
        valueAnimator.setDuration(1000);
        // 匀速线性插值器
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // 当插值计算进行时，获取中间的每个值，
                // 这里这个值是中间过程中的曲线长度（下面根据这个值来得出中间点的坐标值）
                float value = (Float) animation.getAnimatedValue();
                // ★★★★★获取当前点坐标封装到mCurrentPosition
                // boolean getPosTan(float distance, float[] pos, float[] tan) ：
                // 传入一个距离distance(0<=distance<=getLength())，然后会计算当前距
                // 离的坐标点和切线，pos会自动填充上坐标，这个方法很重要。
                mPathMeasure.getPosTan(value, mCurrentPosition, null);//mCurrentPosition此时就是中间距离点的坐标值
                // 移动的商品图片（动画图片）的坐标设置为该中间点的坐标
                goods.setTranslationX(mCurrentPosition[0]);
                goods.setTranslationY(mCurrentPosition[1]);
            }
        });
        //五、 开始执行动画
        valueAnimator.start();
        //六、动画结束后的处理
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }
            //当动画结束后：
            @Override
            public void onAnimationEnd(Animator animation) {
                // 购物袋的数量加1
                viewModel.setShoppingCount(viewModel.getShoppingCount().getValue() + 1);
                // 把移动的图片imageview从父布局里移除
                menuRl.removeView(goods);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }
}