package com.example.riss.models;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.example.riss.AppUtils.Utils.getCountInRomanFormat;

public class Fund {

    String createdBy;
    String fundName;
    String mobileNo;
    String email;
    String address;
    String description;
    String uid;
    long timestamp;
    double totalInvested;
    double currentValue;
    double initialValue;
    String refId;

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public double getInitialValue() {
        return initialValue;
    }

    public void setInitialValue(int initialValue) {
        this.initialValue = initialValue;
    }

    String fundId;
    boolean isActive;
    List<String> likedIds;

    long like;

    public String getLike() {

        return getCountInRomanFormat(like);
    }

    public void setLike(long like) {
        this.like = like;
    }

    public List<String> getLikedIds() {
        return likedIds;
    }

    public void setLikedIds(List<String> likedIds) {
        this.likedIds = likedIds;
    }

    public boolean isActive() {
        return true;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getFundId() {
        return fundId;
    }

    public void setFundId(String fundId) {
        this.fundId = fundId;
    }

    public double getTotalInvested() {
        return totalInvested;
    }

    public void setTotalInvested(int totalInvested) {
        this.totalInvested = totalInvested;
    }

    public double getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(int currentValue) {
        this.currentValue = currentValue;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getFundName() {
        return fundName;
    }

    public void setFundName(String fundName) {
        this.fundName = fundName;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fund fund = (Fund) o;
        return createdBy.equals(fund.createdBy) &&
                fundName.equals(fund.fundName) &&
                mobileNo.equals(fund.mobileNo) &&
                email.equals(fund.email) &&
                address.equals(fund.address) &&
                description.equals(fund.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(createdBy, fundName, mobileNo, email, address, description);
    }

    public static DiffUtil.ItemCallback<Fund> itemCallback = new DiffUtil.ItemCallback<Fund>() {
        @Override
        public boolean areItemsTheSame(@NonNull Fund oldItem, @NonNull Fund newItem) {
            return oldItem.getCreatedBy().equalsIgnoreCase(newItem.getCreatedBy());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Fund oldItem, @NonNull Fund newItem) {
            return oldItem.equals(newItem);
        }
    };
}
