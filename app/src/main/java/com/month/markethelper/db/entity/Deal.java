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
    @PrimaryKey
    private long id;

    //商品ID
    @ColumnInfo(name = "goods_id")
    private long goodsId;

    //商品名
    private String name;

    //店铺所有人的ID
    @ColumnInfo(name = "owner_id")
    private long ownerId;

    //店铺ID
    @ColumnInfo(name = "store_id")
    private long storeId;

    //店铺名
    @ColumnInfo(name = "store_name")
    private String storeName;

    //购买者的手机号
    private String customer;

    //收获地址信息
    @ColumnInfo(name = "address_id")
    private long addressId;

    //购买的数量
    private int amount;

    //总价
    @ColumnInfo(name = "total_price")
    private double totalPrice;

    //交易状态
    private String status;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(long goodsId) {
        this.goodsId = goodsId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(long ownerId) {
        this.ownerId = ownerId;
    }

    public long getStoreId() {
        return storeId;
    }

    public void setStoreId(long storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public long getAddressId() {
        return addressId;
    }

    public void setAddressId(long addressId) {
        this.addressId = addressId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
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
}
