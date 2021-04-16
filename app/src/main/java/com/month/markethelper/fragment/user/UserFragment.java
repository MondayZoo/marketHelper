package com.month.markethelper.fragment.user;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;

import com.month.markethelper.R;
import com.month.markethelper.activity.AddressInfoActivity;
import com.month.markethelper.activity.StoreInfoActivity;
import com.month.markethelper.base.BaseFragment;
import com.month.markethelper.databinding.FragmentUserBinding;
import com.month.markethelper.utils.EmptyMessage;

import org.greenrobot.eventbus.EventBus;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class UserFragment extends BaseFragment<FragmentUserBinding> implements View.OnClickListener{

    private UserViewModel userViewModel;

    //----------------------------basal method---------------------------------

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_user;
    }

    @Override
    protected void initViewModel() {
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        binding.setVm(userViewModel);
    }

    @Override
    protected void initView(View rootView) {
        userViewModel.getUserInfo(userPhoneNum);
    }

    @Override
    protected void initEvent() {
        binding.userExitLl.setOnClickListener(this);
        binding.userAddressLl.setOnClickListener(this);
        binding.userStoreLl.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        //注销
        if (id == R.id.user_exit_ll) {
            logOut(v);
        }
        //我的收货地址
        else if (id == R.id.user_address_ll) {
            Intent intent = new Intent(getActivity(), AddressInfoActivity.class);
            startActivity(intent);
        }
        //我的店铺
        else if (id == R.id.user_store_ll) {
            Intent intent = new Intent(getActivity(), StoreInfoActivity.class);
            startActivity(intent);
        }
    }

    //---------------------------- < > ---------------------------------

    private void logOut(View v) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString("user", null);
        edit.apply();
        EventBus.getDefault().post(EmptyMessage.getInstance(EmptyMessage.STATE_LOGOUT));
        NavController navController = Navigation.findNavController(v);
        navController.navigate(R.id.action_navigation_user_to_navigation_home);
    }
}