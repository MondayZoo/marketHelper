package com.month.markethelper.fragment.store;

import com.month.markethelper.db.MarketDatabase;
import com.month.markethelper.db.dao.StoreDAO;

import androidx.lifecycle.ViewModel;

public class CommentViewModel extends ViewModel {

    private StoreDAO storeDAO;

    public CommentViewModel() {
        MarketDatabase marketDatabase = MarketDatabase.getInstance();
        storeDAO = marketDatabase.getStoreDao();

        initLiveData();

    }

    private void initLiveData() {

    }



}