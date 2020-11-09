package com.example.riss.repositories;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.riss.interfaces.ApiCallbackInterface;
import com.example.riss.models.Fund;
import com.example.riss.models.FundsModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import static com.example.riss.AppUtils.ApiCall.getTopFunds;
import static com.example.riss.AppUtils.Utils.FUNDS;
import static com.example.riss.AppUtils.Utils.getFirestoreReference;
import static com.example.riss.AppUtils.Utils.hideAlertDialog;
import static com.example.riss.AppUtils.Utils.showAlertDialog;

public class AppRepo {

    private static final String TAG = "AppRepo";

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    public MutableLiveData<List<Fund>> topFundsMutableLiveData;
    public MutableLiveData<List<FundsModel>> otherFundsMutableLiveData;
    public MutableLiveData<DocumentSnapshot> FundsMutableLiveData;


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

    public LiveData<List<FundsModel>> getOtherFundByUserId(String userId) {
        if (otherFundsMutableLiveData == null) {
            otherFundsMutableLiveData = new MutableLiveData<>();
            loadOtherFundsData();
        }
        return otherFundsMutableLiveData;

    }

    private void loadOtherFundsData() {
        List<FundsModel> fundsModels = new ArrayList<>();
        for (int a = 0; a < 5; a++) {
            FundsModel fundsModel = new FundsModel();
            fundsModel.setFundName("Fund name");
            fundsModel.setFundImage("Fund name");
            fundsModel.setCreatedBy("Fund name");
            fundsModel.setLikes("Fund name");
            fundsModel.setTotalInvested("Fund name");
            fundsModel.setCurrentValue("Fund name");
            fundsModels.add(fundsModel);
        }
        otherFundsMutableLiveData.setValue(fundsModels);
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
                Log.d(TAG, "onFailed: "+msg);
            }
        });

    }

}
