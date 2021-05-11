package com.month.markethelper.db.dao;

import com.month.markethelper.db.entity.Goods;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface GoodsDAO {

    @Insert
    void addGoods(Goods goods);

    /**
     * 根据商户id获取其全部商品
     * @param storeId   商户id
     * @return  商品数据
     */
    @Query("SELECT * FROM GOODS WHERE store_id = :storeId")
    LiveData<List<Goods>> findAllGoodsByStoreId(long storeId);

}
