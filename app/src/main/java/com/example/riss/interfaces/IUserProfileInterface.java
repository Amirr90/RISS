package com.example.riss.interfaces;

import com.google.firebase.firestore.DocumentSnapshot;

public interface IUserProfileInterface {
    void isProfileVerified(boolean status);

    void profileData(DocumentSnapshot snapshot);

    void isProfileCompleted(boolean isProfileCom);

    void onError(String msg);
}
