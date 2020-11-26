package com.example.riss.models;

public class StatsModel {

    int icon;
    int colorCode;
    String title;
    String percentage;
    String type;

    public StatsModel(int icon, int colorCode, String title, String percentage, String type) {
        this.icon = icon;
        this.colorCode = colorCode;
        this.title = title;
        this.percentage = percentage;
        this.type = type;
    }


    public int getIcon() {
        return icon;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }
}
