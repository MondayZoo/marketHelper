package com.month.markethelper.db.dao;

import com.month.markethelper.db.entity.AddressInfo;
import com.month.markethelper.db.entity.User;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface AddressInfoDAO {

    /**
     * 新增地址
     *
     * @param info 地址信息
     */
    @Insert
    void add(AddressInfo info);

    /**
     * 删除地址
     *
     * @param info 地址信息
     */
    @Delete
    void delete(AddressInfo info);

    /**
     * 更新地址
     *
     * @param info 新地址信息
     */
    @Update
    void update(AddressInfo info);

    /**
     * 查询某用户的所有收获地址
     */
    @Query("SELECT * FROM AddressInfo WHERE phoneNum = :phoneNum")
    LiveData<List<AddressInfo>> findAllByPhoneNum(String phoneNum);
}
