package com.ohad.shoppinglist;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Item {

    @PrimaryKey()
    private int uid;

    @ColumnInfo(name = "isChecked")
    private boolean isChacked;

    @ColumnInfo(name = "item_name")
    private String itemName;

    @ColumnInfo(name = "item_price")
    private float itemPrice;

    @ColumnInfo(name = "item_amount")
    private int itemAmount;

    @ColumnInfo(name = "item_sum")
    private float itemSum;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public boolean isChacked() {
        return isChacked;
    }

    public void setChacked(boolean chacked) {
        isChacked = chacked;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public float getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(float itemPrice) {
        this.itemPrice = itemPrice;
    }

    public int getItemAmount() {
        return itemAmount;
    }

    public void setItemAmount(int itemAmount) {
        this.itemAmount = itemAmount;
    }

    public float getItemSum() {
        return itemSum;
    }

    public void setItemSum(float itemSum) {
        this.itemSum = itemSum;
    }
}
