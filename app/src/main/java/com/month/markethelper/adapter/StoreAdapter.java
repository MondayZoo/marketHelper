package com.month.markethelper.adapter;

import com.month.markethelper.fragment.store.CommentFragment;
import com.month.markethelper.fragment.store.DetailsFragment;
import com.month.markethelper.fragment.store.MenuFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class StoreAdapter extends FragmentStateAdapter {

    private Fragment[] fragments;

    public StoreAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        MenuFragment menuFragment = new MenuFragment();
        CommentFragment commentFragment = new CommentFragment();
        DetailsFragment detailsFragment = new DetailsFragment();
        fragments = new Fragment[] {menuFragment, commentFragment, detailsFragment};
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragments[position];
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
