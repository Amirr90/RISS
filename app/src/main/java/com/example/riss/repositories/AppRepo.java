package com.example.riss.repositories;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.riss.AppUtils.Utils;
import com.example.riss.interfaces.ApiCallbackInterface;
import com.example.riss.models.Fund;
import com.example.riss.models.FundsModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static com.example.riss.AppUtils.ApiCall.getTopFunds;
import static com.example.riss.AppUtils.Utils.FUNDS;
import static com.example.riss.AppUtils.Utils.fundName;
import static com.example.riss.AppUtils.Utils.getFirestoreReference;
import static com.example.riss.AppUtils.Utils.getUid;
import static com.example.riss.AppUtils.Utils.hideAlertDialog;
import static com.example.riss.AppUtils.Utils.showAlertDialog;
import static com.example.riss.AppUtils.Utils.uid;

public class AppRepo {

    private static final String TAG = "AppRepo";

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    public MutableLiveData<List<Fund>> topFundsMutableLiveData;
    public MutableLiveData<List<Fund>> personalFundsMutableLiveData;
    public MutableLiveData<List<Fund>> topFundByIdMutableLiveData;
    public MutableLiveData<List<FundsModel>> otherFundsMutableLiveData;
    public MutableLiveData<DocumentSnapshot> FundsMutableLiveData;


    public LiveData<List<Fund>> getTopFundById(String text, Activity activity) {
        if (topFundByIdMutableLiveData == null) {
            topFundByIdMutableLiveData = new MutableLiveData<>();
        }
        loadTopFundByIdData(text, activity);
        return topFundByIdMutableLiveData;

    }

    private void loadTopFundByIdData(String text, final Activity activity) {

        getFirestoreReference().collection(FUNDS)
                .orderBy("fundName")
                .startAt(text)
                .endAt(text + "\uf8ff")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (null != queryDocumentSnapshots && !queryDocumentSnapshots.isEmpty()) {
                            List<Fund> fundList = new ArrayList<>();
                            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                Fund fund = documentSnapshot.toObject(Fund.class);
                                fundList.add(fund);
                            }
                            topFundByIdMutableLiveData.setValue(fundList);
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(activity, "try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public LiveData<List<Fund>> getPersonalFundsData(Activity activity) {
        if (personalFundsMutableLiveData == null) {
            personalFundsMutableLiveData = new MutableLiveData<>();
        }
        loadPersonalFundsData(activity);
        return personalFundsMutableLiveData;

    }

    private void loadPersonalFundsData(Activity activity) {

        showAlertDialog(activity);
        getFirestoreReference().collection(FUNDS)
                .whereEqualTo(uid, getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        hideAlertDialog();
                        if (null != queryDocumentSnapshots) {
                            List<Fund> funds = new ArrayList<>();
                            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                Fund fund = documentSnapshot.toObject(Fund.class);
                                funds.add(fund);
                            }
                            personalFundsMutableLiveData.setValue(funds);
                        }
                    }
                });
    }

    public LiveData<List<Fund>> getTopFundsData(Activity activity) {
        if (topFundsMutableLiveData == null) {
            topFundsMutableLiveData = new MutableLiveData<>();
        }
        loadTopFundsData(activity);
        return topFundsMutableLiveData;

    }

    public LiveData<DocumentSnapshot> getFundById(String fundId) {
        if (FundsMutableLiveData == null) {
            FundsMutableLiveData = new MutableLiveData<>();
        }
        loadFundsData(fundId);
        return FundsMutableLiveData;

    }
    

    private void loadFundsData(String fundId) {

        getFirestoreReference().collection(FUNDS).document(fundId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (null != documentSnapshot) {
                    FundsMutableLiveData.setValue(documentSnapshot);
                }
            }
        });
    }

    private void loadTopFundsData(final Activity activity) {

        showAlertDialog(activity);
        getTopFunds(new ApiCallbackInterface() {
            @Override
            public void onSuccess(Object obj) {
                hideAlertDialog();
                List<Fund> fundList = (List<Fund>) obj;
                Log.d(TAG, "onSuccess: " + fundList);
                if (null != fundList) {
                    topFundsMutableLiveData.setValue(fundList);
                } else Toast.makeText(activity, "try again", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailed(String msg) {
                hideAlertDialog();
                Toast.makeText(activity, "try again", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailed: " + msg);
            }
        });

    }

}
