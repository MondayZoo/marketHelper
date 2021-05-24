    package com.month.markethelper.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * 交易表
 */
@Entity
public class Deal {

    //交易ID
    @PrimaryKey(autoGenerate = true)
    private long id;

    //商品id及购买量
    private String goods;

    //店铺ID
    @ColumnInfo(name = "store_id")
    private long storeId;

    //购买者
    @ColumnInfo(name = "customer_id")
    private long customerId;

    //收获地址信息
    @ColumnInfo(name = "address_id")
    private long addressId;

    //总价
    @ColumnInfo(name = "total_price")
    private double totalPrice;

    //交易状态
    private String status;

    //交易类型
    private String type;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getGoods() {
        return goods;
    }

    public void setGoods(String goods) {
        this.goods = goods;
    }

    public long getStoreId() {
        return storeId;
    }

    public void setStoreId(long storeId) {
        this.storeId = storeId;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public long getAddressId() {
        return addressId;
    }

    public void setAddressId(long addressId) {
        this.addressId = addressId;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
