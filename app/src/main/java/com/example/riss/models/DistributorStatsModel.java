package com.example.riss.models;

import com.example.riss.AppUtils.Utils;

public class DistributorStatsModel {
    private String itemName;
    private String itemValue;


    public DistributorStatsModel(String itemName, String itemValue) {
        this.itemName = itemName;
        this.itemValue = itemValue;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemValue() {
        return Utils.getCountInRomanFormat(Integer.valueOf(itemValue));
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setItemValue(String itemValue) {
        this.itemValue = itemValue;
    }
}
