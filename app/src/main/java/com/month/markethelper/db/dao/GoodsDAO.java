package com.month.markethelper.db.dao;

import com.month.markethelper.db.entity.Goods;

import java.util.List;
import java.util.Set;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
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

    @Query("DELETE FROM GOODS WHERE id = :goodsId")
    void delete(long goodsId);

    /**
     * 查找相似的商品
     * @param pattern
     * @return 相似的商品列表
     */
    @Query("SELECT * FROM Goods WHERE name LIKE :pattern1 OR :pattern2")
    List<Goods> findSimilarGoods(String pattern1, String pattern2);


    @Query("DELETE FROM Goods WHERE store_id = :storeId")
    void deleteByStoreId(long storeId);
}
