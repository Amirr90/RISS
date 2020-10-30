package com.example.riss;

import android.app.Activity;
import android.content.Intent;

import com.google.firebase.firestore.DocumentSnapshot;

import static com.example.riss.AppUtils.Utils.CREATION_TIME;
import static com.example.riss.AppUtils.Utils.IMAGE;
import static com.example.riss.AppUtils.Utils.IS_ACTIVE;
import static com.example.riss.AppUtils.Utils.LAST_SIGN_IN_TIME;
import static com.example.riss.AppUtils.Utils.MOBILE;
import static com.example.riss.AppUtils.Utils.TOKEN;
import static com.example.riss.AppUtils.Utils.UID;
import static com.example.riss.AppUtils.Utils.UPDATE_PROFILE_REQ_CODE;
import static com.example.riss.AppUtils.Utils.USER_NAME;
import static com.example.riss.AppUtils.Utils.getLastSeen;

public class User {

    public User() {
    }

    public static DocumentSnapshot snapshot;
    private String userName;
    private String token;
    private String uid;
    private String mobile;
    private String image;
    private boolean isActive;
    private Activity activity;
    private String creationTime;
    private String lastSignInTime;

    public String getCreationTime() {
        return getLastSeen(snapshot.getString(CREATION_TIME));
    }

    public String getLastSignInTime() {
        return getLastSeen(snapshot.getString(LAST_SIGN_IN_TIME));
    }

    public String getUserName() {
        return snapshot.getString(USER_NAME);
    }

    public String getToken() {
        return snapshot.getString(TOKEN);

    }

    public String getUid() {
        return snapshot.getString(UID);
    }

    public String getMobile() {
        return snapshot.getString(MOBILE);
    }

    public String getImage() {
        if (null != snapshot.getString(IMAGE))
            return snapshot.getString(IMAGE);
        else return "";

    }

    public boolean isActive() {
        return snapshot.getBoolean(IS_ACTIVE);
    }

    public User(DocumentSnapshot snapshot, Activity activity) {
        this.snapshot = snapshot;
        this.activity = activity;
        if (getUserName().isEmpty())
            showUpdateProfileActivity();
    }

    public void showUpdateProfileActivity() {

        if (null != activity)
            activity.startActivityForResult(new Intent(activity, ProfileActivity.class), UPDATE_PROFILE_REQ_CODE);
    }

}
