package com.example.riss.AppUtils;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.example.riss.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class CustomLoadImage {

    @BindingAdapter("android:loadHomeRecImage")
    public static void loadHomeRecImage(ImageView imageView, int imageUrl) {
        imageView.setImageResource(imageUrl);
    }

    @BindingAdapter("android:loadHomeProfileImage")
    public static void loadHomeProfileImage(final ImageView imageView, String imageUrl) {
        if (null != imageUrl && !imageUrl.trim().isEmpty()) {
            Picasso.get()
                    .load(imageUrl)
                    .into(imageView, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {
                            imageView.setImageResource(R.drawable.profile_user_def);
                        }
                    });
        } else imageView.setImageResource(R.drawable.profile_user_def);
    }

    @BindingAdapter("android:loadFundImage")
    public static void loadFundImage(final ImageView imageView, String imageUrl) {
        if (null != imageUrl && !imageUrl.trim().isEmpty()) {
            Picasso.get()
                    .load(imageUrl)
                    .into(imageView, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {
                            imageView.setImageResource(R.drawable.fund_def);
                        }
                    });
        } else imageView.setImageResource(R.drawable.fund_def);
    }
}
