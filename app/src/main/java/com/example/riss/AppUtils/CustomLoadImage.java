package com.example.riss.AppUtils;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

public class CustomLoadImage {

    @BindingAdapter("android:loadHomeRecImage")
    public static void loadHomeRecImage(ImageView imageView, int imageUrl) {
        imageView.setImageResource(imageUrl);

    }
}
