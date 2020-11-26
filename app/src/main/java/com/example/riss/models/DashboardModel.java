package com.example.riss.models;

import java.util.List;
import java.util.Objects;

public class DashboardModel {

    private int responseCode;
    private int totalMedicineDistributed;
    private int expiredFunds;
    private int activeFunds;
    private int deactiveFunds;
    private int totalEarningFromFunds;
    private int totalCreatedFund;
    private int totalInvestedAmount;
    private double earnedAmount;
    private String result;
    private String username;
    private List<MedicineDistributerModel>MedicineDistributeList;
    private List<FundsModel>fundList;
    User user;

    public List<MedicineDistributerModel> getMedicineDistributeList() {
        return MedicineDistributeList;
    }


    public int getTotalInvestedAmount() {
        return totalInvestedAmount;
    }

    public double getEarnedAmount() {
        return earnedAmount;
    }

    public List<FundsModel> getFundList() {
        return fundList;
    }

    public User getUser() {
        return user;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return "DashboardModel{" +
                "responseCode=" + responseCode +
                ", totalMedicineDistributed=" + totalMedicineDistributed +
                ", expiredFunds=" + expiredFunds +
                ", activeFunds=" + activeFunds +
                ", deactiveFunds=" + deactiveFunds +
                ", totalEarningFromFunds=" + totalEarningFromFunds +
                ", totalCreatedFund=" + totalCreatedFund +
                ", result='" + result + '\'' +
                ", username='" + username + '\'' +
                '}';
    }

    public int getResponseCode() {
        return responseCode;
    }

    public String getTotalMedicineDistributed() {
        return String.valueOf(totalMedicineDistributed);
    }

    public String getExpiredFunds() {
        return String.valueOf(expiredFunds);
    }

    public String getActiveFunds() {
        return String.valueOf(activeFunds);
    }

    public String getDeactiveFunds() {
        return String.valueOf(deactiveFunds);
    }

    public String getTotalEarningFromFunds() {
        return String.valueOf(totalEarningFromFunds);
    }

    public String getTotalCreatedFund() {
        return String.valueOf(totalCreatedFund);
    }

    public String getResult() {
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DashboardModel that = (DashboardModel) o;
        return responseCode == that.responseCode &&
                totalMedicineDistributed == that.totalMedicineDistributed &&
                expiredFunds == that.expiredFunds &&
                activeFunds == that.activeFunds &&
                deactiveFunds == that.deactiveFunds &&
                totalEarningFromFunds == that.totalEarningFromFunds &&
                totalCreatedFund == that.totalCreatedFund &&
                result.equals(that.result) &&
                username.equals(that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(responseCode, totalMedicineDistributed, expiredFunds, activeFunds, deactiveFunds, totalEarningFromFunds, totalCreatedFund, result, username);
    }

}
