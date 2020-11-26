package com.example.riss.models;

import java.util.Objects;

public class MedicineDistributerModel {
    private String id;
    private String name;
    private String mobile;
    private String address;
    private String otp;
    private String uid;
    private long timestamp;
    private int qty;

    public long getTimestamp() {
        return timestamp;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getMobile() {
        return mobile;
    }

    public String getAddress() {
        return address;
    }

    public String getOtp() {
        return otp;
    }

    public String getUid() {
        return uid;
    }



    public int getQty() {
        return qty;
    }

    @Override
    public String toString() {
        return "MedicineDistributerModel{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", mobile='" + mobile + '\'' +
                ", address='" + address + '\'' +
                ", otp='" + otp + '\'' +
                ", uid='" + uid + '\'' +
                ", timestamp=" + timestamp +
                ", qty=" + qty +
                '}';
    }
}
