package com.example.riss.interfaces;

import com.google.firebase.firestore.DocumentSnapshot;

public interface IUserProfileInterface {
    boolean isProfileVerified();

    void profileData(DocumentSnapshot snapshot);

    void isProfileCompleted(boolean isProfileCom);

    void onError(String msg);
}
