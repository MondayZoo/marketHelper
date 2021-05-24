package com.month.markethelper.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.month.markethelper.R;
import com.month.markethelper.base.BaseApplication;

import java.util.List;

public class DialogUtils {

    /**
     * 创建加载框
     * @param context   上下文
     * @param msg   显示的信息
     * @return  Dialog
     */
    public static Dialog createLoadingDialog(Context context, String msg) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.dialog_loading, null);// 得到加载view
        LinearLayout layout = (LinearLayout) v
                .findViewById(R.id.dialog_loading_view);// 加载布局
        TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
        tipTextView.setText(msg);// 设置加载信息

        Dialog loadingDialog = new Dialog(context, R.style.MyDialogStyle);// 创建自定义样式dialog
        loadingDialog.setCancelable(true); // 是否可以按“返回键”消失
        loadingDialog.setCanceledOnTouchOutside(false); // 点击加载框以外的区域
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));// 设置布局
        /**
         *将显示Dialog的方法封装在这里面
         */
        Window window = loadingDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setGravity(Gravity.CENTER);
        window.setAttributes(lp);
        //loadingDialog.show();
        return loadingDialog;
    }

    /**
     * 创建评论框
     * @return  dialog
     */
    public static Dialog createCommentDialog(Context context) {
        Dialog dialog = new Dialog(context, R.style.CustomDialogTheme);
        dialog.setContentView(R.layout.dialog_comment);

        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    /**
     * 创建分类对话框
     * @return  dialog
     */
    public static Dialog createCategoryDialog(Context context) {
        Dialog dialog = new Dialog(context, R.style.CustomDialogTheme);
        dialog.setContentView(R.layout.dialog_add_category);

        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    /**
     * 创建库存对话框
     * @return  dialog
     */
    public static Dialog createInventoryDialog(Context context) {
        Dialog dialog = new Dialog(context, R.style.CustomDialogTheme);
        dialog.setContentView(R.layout.dialog_set_inventory);

        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        dialog.setCanceledOnTouchOutside(false);
        dialog.findViewById(R.id.no_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        return dialog;
    }

    /**
     * 创建图片选择对话框
     * @param context
     * @return
     */
    public static Dialog createPictureChoiceDialog(Context context) {
        Dialog dialog = new Dialog(context, R.style.CustomDialogTheme);
        dialog.setContentView(R.layout.dialog_picture_choice);

        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        return dialog;
    }

    /**
     * 条件选择器初始化，自定义布局
     */
    private static OptionsPickerView pvCustomOptions;
    public static <T>OptionsPickerView initCustomOptionPicker(Context mContext, List<T> data, OnOptionsSelectListener onOptionsSelectListener) {
        /*
         * @description
         *
         * 注意事项：
         * 自定义布局中，id为 optionspicker 或者 timepicker 的布局以及其子控件必须要有，否则会报空指针。
         * 具体可参考demo 里面的两个自定义layout布局。
         */
        pvCustomOptions = new OptionsPickerBuilder(mContext, (options1, option2, options3, v) -> {
            //返回的分别是三个级别的选中位置
            onOptionsSelectListener.onOptionsSelectListener(options1,option2,options3);
        })
                .setLayoutRes(R.layout.pickerview_custom_options, v -> {
                    TextView pickerTitle = v.findViewById(R.id.tvTitle);
                    TextView tvSubmit = v.findViewById(R.id.tv_finish);
                    ImageView ivCancel = v.findViewById(R.id.iv_cancel);
                    pickerTitle.setText("选择分类");
                    tvSubmit.setOnClickListener(v1 -> {
                        pvCustomOptions.returnData();
                        pvCustomOptions.dismiss();
                    });
                    ivCancel.setOnClickListener(v12 -> pvCustomOptions.dismiss());
                })
                .setContentTextSize(18)
                .setDividerColor(mContext.getResources().getColor(R.color.white))            //分割线的颜色
                .setTextColorCenter(mContext.getResources().getColor(R.color.color_D0021B))  //选中文本的颜色
                .setLineSpacingMultiplier(2.0f)
                .isAlphaGradient(true)
                .setDecorView((ViewGroup) ((Activity) mContext).getWindow().getDecorView().findViewById(android.R.id.content))
                .build();

        pvCustomOptions.setPicker(data);//添加数据
        return pvCustomOptions;
    }

    public interface OnOptionsSelectListener {
        void onOptionsSelectListener(int options1,int option2,int options3);
    }

    /**
     * 创建购物袋对话框
     * @param context
     * @return
     */
    public static Dialog createShoppingBagDialog(Context context) {
        Dialog dialog = new Dialog(context, R.style.CustomDialogTheme);
        dialog.setContentView(R.layout.dialog_shopping_bag);

        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        return dialog;
    }

    /**
     * 创建地址选择对话框
     * @param context
     * @return
     */
    public static Dialog createChooseAddressDialog(Context context) {
        Dialog dialog = new Dialog(context, R.style.CustomDialogTheme);
        dialog.setContentView(R.layout.dialog_choose_address);

        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        dialog.findViewById(R.id.back_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        return dialog;
    }
}