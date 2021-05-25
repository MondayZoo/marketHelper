package com.month.markethelper.db.dao;

import com.month.markethelper.db.entity.User;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface UserDAO {

    /**
     * 注册
     *
     * @param user 新用户
     */
    @Insert
    void register(User user);


    /**
     * 通过手机号查找用户
     *
     * @param phoneNum 手机号
     * @return 用户
     */
    @Query("SELECT * FROM User WHERE phone_num = :phoneNum")
    User findUser(String phoneNum);

    @Query("SELECT * FROM User WHERE id = :userId")
    User findUserById(long userId);
}