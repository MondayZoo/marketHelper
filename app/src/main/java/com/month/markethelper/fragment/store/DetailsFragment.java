package com.month.markethelper.fragment.store;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;

import com.month.markethelper.R;
import com.month.markethelper.activity.AddressInfoActivity;
import com.month.markethelper.activity.StoreInfoActivity;
import com.month.markethelper.base.BaseFragment;
import com.month.markethelper.databinding.FragmentStoreDetialsBinding;
import com.month.markethelper.databinding.FragmentUserBinding;
import com.month.markethelper.fragment.user.UserViewModel;
import com.month.markethelper.utils.EmptyMessage;

import org.greenrobot.eventbus.EventBus;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class DetailsFragment extends BaseFragment<FragmentStoreDetialsBinding> implements View.OnClickListener{

    private DetailsViewModel viewModel;

    //----------------------------basal method---------------------------------

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

    }

    @Override
    protected void initEvent() {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

    }

    //---------------------------- < > ---------------------------------

}