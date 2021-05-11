package com.month.markethelper.fragment.store;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.month.markethelper.R;
import com.month.markethelper.activity.AddressInfoActivity;
import com.month.markethelper.activity.MapActivity;
import com.month.markethelper.activity.StoreInfoActivity;
import com.month.markethelper.base.BaseFragment;
import com.month.markethelper.databinding.FragmentStoreDetialsBinding;
import com.month.markethelper.databinding.FragmentUserBinding;
import com.month.markethelper.fragment.user.UserViewModel;
import com.month.markethelper.utils.EmptyMessage;

import org.greenrobot.eventbus.EventBus;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class DetailsFragment extends BaseFragment<FragmentStoreDetialsBinding> implements View.OnClickListener{

    public static final String TAG = "DetailsFragment";

    private DetailsViewModel viewModel;

    //---------------------------- Constructor --------------------------------
    private DetailsFragment() {};

    public static DetailsFragment newInstance(long storeId) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putLong("storeId", storeId);
        fragment.setArguments(args);
        return fragment;
    }

    //----------------------------Basal Method---------------------------------

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_store_detials;
    }

    @Override
    protected void initViewModel() {
        viewModel = new ViewModelProvider(this).get(DetailsViewModel.class);
        binding.setVm(viewModel);
    }

    @Override
    protected void initView(View rootView) {
        //获取相关数据
        viewModel.setStoreId(getArguments().getLong("storeId"));
        viewModel.getStoreDetails();
    }

    @Override
    protected void initEvent() {
        binding.storeContactsLl.setOnClickListener(this);
        binding.storeAddressLl.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        //拨打电话
        if (id == R.id.store_contacts_ll) {
            callPhone(viewModel.getStore().getValue().getContactNumber());
        }
        //显示地图
        else if (id == R.id.store_address_ll) {
            Intent intent = new Intent(getActivity(), MapActivity.class);
            intent.putExtra("latitude", viewModel.getStore().getValue().getLatitude());
            intent.putExtra("longitude", viewModel.getStore().getValue().getLongitude());
            intent.putExtra("storeName", viewModel.getStore().getValue().getStoreName());
            startActivity(intent);
        }
    }

    //---------------------------- Click Event Method ---------------------------------
    private void callPhone(String phoneNum) {
        //Android 6 版本动态获取权限
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            int REQUEST_CODE_CONTACT = 101;
            String[] permissions = {Manifest.permission.CALL_PHONE};
            //验证是否许可权限
            for (String permission : permissions) {
                if (getActivity().checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(permissions, REQUEST_CODE_CONTACT);
                    return;
                }
            }
        }

        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        startActivity(intent);
    }
}