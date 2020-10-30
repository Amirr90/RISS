package com.example.riss;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.example.riss.AppUtils.Utils.getCountInRomanFormat;
import static com.example.riss.AppUtils.Utils.getCurrencyFormat;

public class FundsModel {

    private String fundName;
    private String fundSupport;
    private String fundLike;
    private String fundFollower;
    private String fundImage;
    private boolean like;
    private String id;
    private ArrayList<String> likedIdsList;

    public FundsModel(String fundName, String fundSupport, String fundLike, String fundFollower, String fundImage, boolean like, String id, ArrayList<String> likedIdsList) {
        this.fundName = fundName;
        this.fundSupport = fundSupport;
        this.fundLike = fundLike;
        this.fundFollower = fundFollower;
        this.fundImage = fundImage;
        this.like = like;
        this.id = id;
        this.likedIdsList = likedIdsList;
    }

    public ArrayList<String> getLikedIdsList() {
        return likedIdsList;
    }

    public String getId() {
        return id;
    }

    public boolean isLike() {
        return like;
    }

    public void setFundName(String fundName) {
        this.fundName = fundName;
    }

    public void setFundSupport(String fundSupport) {
        this.fundSupport = fundSupport;
    }

    public void setFundLike(String fundLike) {

        this.fundLike = "" + Integer.parseInt(fundLike) + Integer.parseInt(this.fundLike);
    }

    public void setFundFollower(String fundFollower) {
        this.fundFollower = fundFollower;
    }

    public void setFundImage(String fundImage) {
        this.fundImage = fundImage;
    }

    public void setLike(boolean like) {
        this.like = like;
    }

    public String getFundName() {
        return fundName;
    }

    public String getFundSupport() {

        if (fundSupport.isEmpty()) {
            return "Amount Supported " + 0;
        } else {
            return "Amount Supported " + getCurrencyFormat(fundSupport);
        }

    }

    public String getFundLike() {
        if (fundLike.isEmpty()) {
            return "" + 0;
        } else {
            return getCountInRomanFormat(Integer.parseInt(fundLike));
        }
    }

    public String getFundFollower() {
        if (fundFollower.isEmpty()) {
            return "Follower" + 0;
        } else {
            return "Follower " + getCountInRomanFormat(Integer.parseInt(fundFollower));
        }
    }

    public String getFundImage() {
        return fundImage;
    }

    @BindingAdapter("android:loadImage")
    public void loadImage(ImageView imageView, String imageUrl) {
        if (null != imageUrl && !imageUrl.equalsIgnoreCase(""))
            Picasso.get()
                    .load(imageUrl)
                    .resize(80, 80)
                    .centerCrop()
                    .into(imageView);
        else Picasso.get()
                .load(R.drawable.ic_launcher_foreground)
                .centerCrop()
                .resize(80, 80)
                .into(imageView);
    }
}
