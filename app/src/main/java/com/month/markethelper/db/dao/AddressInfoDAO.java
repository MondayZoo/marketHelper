package com.month.markethelper.db.dao;

import com.month.markethelper.db.entity.AddressInfo;
import com.month.markethelper.db.entity.User;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;

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
}
