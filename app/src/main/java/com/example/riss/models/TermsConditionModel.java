package com.example.riss.models;

public class TermsConditionModel {
    String serial;
    String withdrawalPercentage;
    String handlingCharges;


    public TermsConditionModel(String serial, String withdrawalPercentage, String handlingCharges) {
        this.serial = serial;
        this.withdrawalPercentage = withdrawalPercentage;
        this.handlingCharges = handlingCharges;
    }

    public String getSerial() {
        return serial;
    }

    public String getWithdrawalPercentage() {
        return withdrawalPercentage;
    }

    public String getHandlingCharges() {
        return handlingCharges;
    }
}
