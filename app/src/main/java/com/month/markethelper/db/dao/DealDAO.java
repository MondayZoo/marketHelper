package com.month.markethelper.db.dao;

import com.month.markethelper.db.entity.Deal;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface DealDAO {

    @Insert
    void insert(Deal deal);

    @Update
    void update(Deal deal);

    /**
     * 根据用户id获取订单
     * @param userId    用户id
     * @return  订单列表
     */
    @Query("SELECT * FROM Deal WHERE customer_id = :userId")
    LiveData<List<Deal>> getDealByUserId(long userId);

}
