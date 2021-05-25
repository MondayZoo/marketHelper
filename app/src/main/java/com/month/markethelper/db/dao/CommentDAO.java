package com.month.markethelper.db.dao;

import com.month.markethelper.db.entity.Comment;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface CommentDAO {

    /**
     * 新增评论
     *
     * @param comment 评论信息
     */
    @Insert
    void add(Comment comment);

    /**
     * 根据店铺ID查询某店铺的所有评论
     * @param storeId 店铺id
     * @return 评论列表
     */
    @Query("SELECT * FROM comment WHERE store_id = :storeId")
    LiveData<List<Comment>> findAllByStoreId(long storeId);

    /**
     * 根据店铺ID查询某店铺的总评分
     * @param storeID
     * @return
     */
    @Query("SELECT SUM(rating) FROM comment WHERE store_id = :storeID")
    float getSumOfRatingByStoreId(long storeID);

    /**
     * 根据店铺ID查询某店铺的评论数量
     * @param storeId 店铺id
     * @return 评论数
     */
    @Query("SELECT COUNT(*) FROM comment WHERE store_id = :storeId")
    int getCountByStoreId(long storeId);

    /**
     * 根据店铺ID查询某店铺的好评数量
     * @param storeId 店铺id
     * @return 好评数
     */
    @Query("SELECT COUNT(*) FROM comment WHERE store_id = :storeId AND rating > 3")
    int getFavoriteCountByStoreId(long storeId);

    @Query("DELETE FROM Comment WHERE store_id = :storeId")
    void deleteByStoreId(long storeId);
}
