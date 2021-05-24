package com.month.markethelper.db.dao;

import com.month.markethelper.db.entity.Goods;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

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

    /**
     * 根据商品id获取商品信息
     * @param goodsId   商品id
     * @return  商品信息
     */
    @Query("SELECT * FROM GOODS WHERE id = :goodsId")
    Goods findGoodsById(long goodsId);

    /**
     * 根据库存数量获取商品列表
     * @param storeId
     * @return
     */
    @Query("SELECT * FROM GOODS WHERE store_id = :storeId ORDER BY inventory")
    LiveData<List<Goods>> findGoodsByInventory(long storeId);

    /**
     * 获取短缺商品列表
     * @param storeId
     * @return
     */
    @Query("SELECT * FROM GOODS WHERE store_id = :storeId AND inventory <= 50 ORDER BY inventory")
    LiveData<List<Goods>> findShortGoods(long storeId);

    @Update
    void update(Goods goods);
}
