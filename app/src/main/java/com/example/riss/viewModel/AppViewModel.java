package com.example.riss.viewModel;

import android.app.Activity;
import android.provider.DocumentsContract;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.riss.models.Fund;
import com.example.riss.models.FundsModel;
import com.example.riss.repositories.AppRepo;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

public class AppViewModel extends ViewModel {

    AppRepo repo = new AppRepo();

    public LiveData<List<Fund>> getTopFundsData(Activity activity) {
        return repo.getTopFundsData(activity);
    }

    public LiveData<DocumentSnapshot> getFundById(String fundId) {
        return repo.getFundById(fundId);
    }
    public LiveData<List<FundsModel>> getOtherFundByUserId(String userId) {
        return repo.getOtherFundByUserId(userId);
    }
}
