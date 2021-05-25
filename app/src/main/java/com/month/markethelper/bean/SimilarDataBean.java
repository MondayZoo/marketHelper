package com.month.markethelper.bean;

import com.month.markethelper.db.entity.Goods;

import java.util.List;

public class SimilarDataBean {

    private String storeName;
    private float distance;
    private boolean isBelong;
    private String contactNum;
    private List<Goods> list;

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public boolean isBelong() {
        return isBelong;
    }

    public void setBelong(boolean belong) {
        isBelong = belong;
    }

    public String getContactNum() {
        return contactNum;
    }

    public void setContactNum(String contactNum) {
        this.contactNum = contactNum;
    }

    public List<Goods> getList() {
        return list;
    }

    public void setList(List<Goods> list) {
        this.list = list;
    }
}
