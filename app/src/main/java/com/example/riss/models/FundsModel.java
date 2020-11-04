package com.example.riss.models;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import java.util.Objects;

public class FundsModel {
    String fundName;
    String fundImage;
    String createdBy;
    String likes;
    String totalInvested;
    String currentValue;

    public void setFundName(String fundName) {
        this.fundName = fundName;
    }

    public void setFundImage(String fundImage) {
        this.fundImage = fundImage;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public void setTotalInvested(String totalInvested) {
        this.totalInvested = totalInvested;
    }

    public void setCurrentValue(String currentValue) {
        this.currentValue = currentValue;
    }

    public String getFundName() {
        return fundName;
    }

    public String getFundImage() {
        return fundImage;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getLikes() {
        return likes;
    }

    public String getTotalInvested() {
        return totalInvested;
    }

    public String getCurrentValue() {
        return currentValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FundsModel)) return false;
        FundsModel that = (FundsModel) o;
        return getFundName().equals(that.getFundName()) &&
                getFundImage().equals(that.getFundImage()) &&
                getCreatedBy().equals(that.getCreatedBy()) &&
                getLikes().equals(that.getLikes()) &&
                getTotalInvested().equals(that.getTotalInvested()) &&
                getCurrentValue().equals(that.getCurrentValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFundName(), getFundImage(), getCreatedBy(), getLikes(), getTotalInvested(), getCurrentValue());
    }

    public static DiffUtil.ItemCallback<FundsModel> itemCallback = new DiffUtil.ItemCallback<FundsModel>() {
        @Override
        public boolean areItemsTheSame(@NonNull FundsModel oldItem, @NonNull FundsModel newItem) {
            return oldItem.getTotalInvested().equalsIgnoreCase(newItem.getTotalInvested());
        }

        @Override
        public boolean areContentsTheSame(@NonNull FundsModel oldItem, @NonNull FundsModel newItem) {
            return oldItem.equals(newItem);
        }
    };
}
