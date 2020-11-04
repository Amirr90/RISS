package com.example.riss.models;

public class HomeModel {
    private String title;
    String text;
    int image;


    public HomeModel(String title, String text, int image) {
        this.title = title;
        this.text = text;
        this.image = image;
    }

    public int getImage() {
        return image;
    }

    public String getText() {
        return text;
    }


    public String getTitle() {
        return title;
    }
}
