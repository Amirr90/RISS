package com.example.riss.models;

public class StatsModel {

    int colorCode;
    String title;
    int percentage;

    public StatsModel(int colorCode, String title, int percentage) {
        this.colorCode = colorCode;
        this.title = title;
        this.percentage = percentage;
    }

    public int getColorCode() {
        return colorCode;
    }

    public void setColorCode(int colorCode) {
        this.colorCode = colorCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }
}
