package com.month.markethelper.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * 评论表
 */
@Entity
public class Comment {

    //评论ID
    @PrimaryKey(autoGenerate = true)
    private long id;

    //评论的店铺ID
    @ColumnInfo(name = "store_id")
    private long storeId;

    //评论的用户名
    @ColumnInfo(name = "nick_name")
    private String nickName;

    //评论的内容
    private String content;

    //评论的日期
    private String date;

    //评分
    private float rating;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getStoreId() {
        return storeId;
    }

    public void setStoreId(long storeId) {
        this.storeId = storeId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
