package com.month.markethelper.db;

import android.content.Context;

import com.month.markethelper.base.BaseApplication;
import com.month.markethelper.db.dao.AddressInfoDAO;
import com.month.markethelper.db.dao.CommentDAO;
import com.month.markethelper.db.dao.DealDAO;
import com.month.markethelper.db.dao.FundingDAO;
import com.month.markethelper.db.dao.GoodsDAO;
import com.month.markethelper.db.dao.StoreDAO;
import com.month.markethelper.db.dao.UserDAO;
import com.month.markethelper.db.entity.AddressInfo;
import com.month.markethelper.db.entity.Comment;
import com.month.markethelper.db.entity.Deal;
import com.month.markethelper.db.entity.Funding;
import com.month.markethelper.db.entity.Goods;
import com.month.markethelper.db.entity.Store;
import com.month.markethelper.db.entity.User;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {User.class, Store.class, Goods.class, Deal.class, Funding.class, AddressInfo.class, Comment.class}, version = 2, exportSchema = false)
public abstract class   MarketDatabase extends RoomDatabase {

    private volatile static MarketDatabase INSTANCE;

    public static MarketDatabase getInstance() {
        if (INSTANCE == null) {
            synchronized (MarketDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(BaseApplication.getAppContext(), MarketDatabase.class, "market_database")
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract UserDAO getUserDao();
    public abstract DealDAO getDealDao();
    public abstract FundingDAO getFundingDao();
    public abstract StoreDAO getStoreDao();
    public abstract GoodsDAO getGoodsDao();
    public abstract AddressInfoDAO getAddressInfoDao();
    public abstract CommentDAO getCommentDao();

}
