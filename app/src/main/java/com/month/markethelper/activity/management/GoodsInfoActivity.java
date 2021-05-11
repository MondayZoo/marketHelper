package com.month.markethelper.activity.management;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bumptech.glide.Glide;
import com.month.markethelper.R;
import com.month.markethelper.base.BaseActivityWithViewModel;
import com.month.markethelper.databinding.ActivityGoodsInfoBinding;
import com.month.markethelper.utils.BitmapUtils;
import com.month.markethelper.utils.DialogUtils;

import java.util.Arrays;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

public class GoodsInfoActivity extends BaseActivityWithViewModel<ActivityGoodsInfoBinding> implements View.OnClickListener {

    public static final String TAG = "GoodsInfoActivity";

    private GoodsInfoViewModel viewModel;

    private long storeId;

    //页面状态 1、新增状态 2、更新状态
    private int state;

    private TextView backTv, titleTv;

    //选择图片对话框
    private Dialog dialog;

    //分类选择器
    private OptionsPickerView pvCustomOptions;

    private final int CAMERA_REQUEST = 102;
    private final int ALBUM_REQUEST = 103;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_goods_info;
    }

    @Override
    protected void initViewModel() {
        viewModel = new ViewModelProvider(this).get(GoodsInfoViewModel.class);
        binding.setVm(viewModel);
        binding.setLifecycleOwner(this);
        storeId = sharedPreferences.getLong("storeId", 0);
        Log.e(TAG, "storeId --> " + storeId);
        viewModel.setStoreId(storeId);
        /**
         * fixme 坑
         * LiveData 必须使用观察者获取数据 getValue()获取为null
         * 根据商户设置的分类初始化 分类选择对话框
         */
        viewModel.getCategory().observe(this, s -> {
            List<String> categoryList = Arrays.asList(s.split(";"));
            pvCustomOptions = DialogUtils.initCustomOptionPicker(GoodsInfoActivity.this, categoryList, (options1, option2, options3) -> {
                String tx = categoryList.get(options1);
                binding.goodsTypeTv.setText(tx);
            });
        });
    }

    @Override
    protected void initView() {

        //页面状态
        state = getIntent().getIntExtra("state", 0);

        backTv = findViewById(R.id.actionbar_back_tv);
        titleTv = findViewById(R.id.actionbar_title_tv);
        titleTv.setText("商品管理");

        //新增模式
        if (state == 1) {

            binding.goodsPicIv.setVisibility(View.GONE);
        }
        //更新模式
        else if (state == 2) {

            binding.goodsPicIv.setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected void initEvent() {
        backTv.setOnClickListener(this);
        binding.goodsPicTv.setOnClickListener(this);
        binding.goodsSaveBtn.setOnClickListener(this);
        binding.goodsDeleteBtn.setOnClickListener(this);
        binding.goodsTypeTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        //返回
        if (id == R.id.actionbar_back_tv) {
            finish();
        }
        //设置分类
        if (id == R.id.goods_type_tv) {
            pvCustomOptions.show();
        }
        //选择图片
        else if (id == R.id.goods_pic_tv) {
            showDialog();
        }
        //拍照
        else if (id == R.id.camera_tv) {
            getPictureByCamera();
        }
        //从相册选择
        else if (id == R.id.album_tv) {
            getPictureByAlbum();
        }
        //保存商品
        else if (id == R.id.goods_save_btn) {
            viewModel.addGoods();
            finish();
        }
    }

    //------------------------------------ Click Event Method -------------------------------

    /**
     * 显示图片选择对话框
     */
    private void showDialog() {
        if (dialog == null) {
            dialog = DialogUtils.createPictureChoiceDialog(this);
            TextView cameraTv = dialog.findViewById(R.id.camera_tv);
            TextView albumTv = dialog.findViewById(R.id.album_tv);
            cameraTv.setOnClickListener(this);
            albumTv.setOnClickListener(this);
        }
        dialog.show();
    }

    /**
     * 拍照获取图片
     */
    private void getPictureByCamera() {
        //Android 6 版本动态获取权限
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            int REQUEST_CODE_CONTACT = 101;
            String[] permissions = {Manifest.permission.CAMERA};
            //验证是否许可权限
            for (String permission : permissions) {
                if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(permissions, REQUEST_CODE_CONTACT);
                    return;
                }
            }
        }
        //调用摄像头
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    /**
     * 从相册获取图片
     */
    private void getPictureByAlbum() {
        //Android 6 版本动态获取权限
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            int REQUEST_CODE_CONTACT = 101;
            String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
            //验证是否许可权限
            for (String permission : permissions) {
                if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(permissions, REQUEST_CODE_CONTACT);
                    return;
                }
            }
        }
        //调用相册
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, ALBUM_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            //相册选择图片
            case ALBUM_REQUEST:
                if (resultCode == RESULT_OK) {//resultcode是setResult里面设置的code值
                    try {
                        Uri selectedImage = data.getData(); //获取系统返回的照片的Uri
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        Cursor cursor = getContentResolver().query(selectedImage,
                                filePathColumn, null, null, null);//从系统表中查询指定Uri对应的照片
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String path = cursor.getString(columnIndex);  //获取照片路径
                        cursor.close();
                        //转换成bitmap
                        Bitmap bitmap = BitmapFactory.decodeFile(path);
                        //转换成可保存的String类型数据并保存
                        viewModel.setGoodsPicUrl(BitmapUtils.bitmapToString(bitmap));
                        //预览
                        binding.goodsPicTv.setText("已添加，点击修改");
                        binding.goodsPicIv.setVisibility(View.VISIBLE);
                        Glide.with(this).load(bitmap).into(binding.goodsPicIv);
                        //隐藏dialog
                        dialog.dismiss();
                    } catch (Exception e) {
                        // TODO Auto-generatedcatch block
                        e.printStackTrace();
                    }
                }
                break;
            //拍照
            case CAMERA_REQUEST:
                //获取Bitmap
                if (data.getExtras() != null) {
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    //转换成可保存的String类型数据并保存
                    if (photo != null) {
                        viewModel.setGoodsPicUrl(BitmapUtils.bitmapToString(photo));
                        //显示出来
                        binding.goodsPicTv.setText("已添加，点击修改");
                        binding.goodsPicIv.setVisibility(View.VISIBLE);
                        Glide.with(this).load(photo).into(binding.goodsPicIv);
                    }
                }
                //隐藏dialog
                dialog.dismiss();
                break;
        }
    }

}
