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
    String nomineeName;
    String nomineeDetail;
    long timestamp;
    double totalInvested;
    double currentValue;
    double initialValue;
    int duration;
    String refId;
    String startDate;
    String expiryDate;
    String fundId;
    boolean isActive;
    List<String> likedIds;
    long like;
    String fundImage;
    int support;
    int fundAmount;
    int medicineDistributeTarget;

    public String getNomineeName() {
        return nomineeName;
    }

    public void setNomineeName(String nomineeName) {
        this.nomineeName = nomineeName;
    }

    public String getNomineeDetail() {
        return nomineeDetail;
    }

    public void setNomineeDetail(String nomineeDetail) {
        this.nomineeDetail = nomineeDetail;
    }

    public int getMedicineDistributeTarget() {
        return medicineDistributeTarget;
    }

    public void setMedicineDistributeTarget(int medicineDistributeTarget) {
        this.medicineDistributeTarget = medicineDistributeTarget;
    }

    public int getFundAmount() {
        return fundAmount;
    }

    public void setFundAmount(int fundAmount) {
        this.fundAmount = fundAmount;
    }

    public int getSupport() {
        return support;
    }

    public void setSupport(int support) {
        this.support = support;
    }

    public String getFundImage() {
        return fundImage;
    }

    public void setFundImage(String fundImage) {
        this.fundImage = fundImage;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public double getTotalInvested() {

        return totalInvested;
    }

    public void setTotalInvested(double totalInvested) {
        this.totalInvested = totalInvested;
    }

    public double getInitialValue() {
        return initialValue;
    }

    public void setInitialValue(double initialValue) {
        this.initialValue = initialValue;
    }

    public double getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(double currentValue) {
        this.currentValue = currentValue;
    }


    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }


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
