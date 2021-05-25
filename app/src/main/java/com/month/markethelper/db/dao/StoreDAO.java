package com.month.markethelper.db.dao;

import com.month.markethelper.db.entity.AddressInfo;
import com.month.markethelper.db.entity.Store;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface StoreDAO {

    /**
     * 新增店铺
     *
     * @param store 店铺信息
     */
    @Insert
    void add(Store store);

    /**
     * 删除店铺
     *
     * @param store 店铺信息
     */
    @Delete
    void delete(Store store);

    /**
     * 更新店铺
     *
     * @param store 新店铺信息
     */
    @Update
    void update(Store store);

    /**
     * 查询某用户的所有店铺
     */
    @Query("SELECT * FROM store WHERE phone_num = :phoneNum")
    LiveData<List<Store>> findAllByPhoneNum(String phoneNum);

    /**
     * 根据ID查询店铺
     */
    @Query("SELECT * FROM store WHERE id = :storeId")
    Store findStoreById(long storeId);

    /**
     * 查询所有店铺
     */
    @Query("SELECT * FROM store")
    LiveData<List<Store>> findAll();

    /**
     * 查询店铺商品分类
     */
    @Query("SELECT category FROM store WHERE id = :storeId")
    LiveData<String> getCategoryLived(long storeId);

    @Query("SELECT category FROM store WHERE id = :storeId")
    String getCategory(long storeId);

    /**
     * 查询店铺名
     */
    @Query("SELECT store_name FROM Store WHERE id = :storeId")
    String getStoreName(long storeId);

    /**
     * 判断某店铺是否属于某用户
     * @param phoneNum 用户手机号
     * @param storeId  店铺ID
     * @return 店铺名
     */
    @Query("SELECT COUNT(*) FROM STORE WHERE phone_num = :phoneNum AND id = :storeId" )
    int findStoreBelong(String phoneNum, long storeId);
}
