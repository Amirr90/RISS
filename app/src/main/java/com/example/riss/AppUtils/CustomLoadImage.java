package com.example.riss.AppUtils;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.squareup.picasso.Picasso;

public class CustomLoadImage {

    @BindingAdapter("android:loadHomeRecImage")
    public static void loadHomeRecImage(ImageView imageView, int imageUrl) {
        imageView.setImageResource(imageUrl);
    }

    @BindingAdapter("android:loadHomeProfileImage")
    public static void loadHomeProfileImage(ImageView imageView, String imageUrl) {
        if (null != imageUrl && !imageUrl.trim().isEmpty())
            Picasso.get()
                    .load(imageUrl)
                    .into(imageView);
    }
}
