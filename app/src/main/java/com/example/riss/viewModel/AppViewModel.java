package com.example.riss.viewModel;

import android.app.Activity;
import android.provider.DocumentsContract;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.riss.models.DashboardModel;
import com.example.riss.models.Fund;
import com.example.riss.models.FundsModel;
import com.example.riss.repositories.AppRepo;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

public class AppViewModel extends ViewModel {

    AppRepo repo = new AppRepo();

    public LiveData<DashboardModel> getDashboardData(Activity activity) {
        return repo.getDashboardData(activity);
    }

    public LiveData<List<Fund>> getTopFundById(String text, Activity activity) {
        return repo.getTopFundById(text, activity);
    }

    public LiveData<List<Fund>> getPersonalFundsData(String fundName, Activity activity) {
        return repo.getPersonalFundsData(fundName, activity);
    }

    public LiveData<List<Fund>> getTopFundsData(Activity activity) {
        return repo.getTopFundsData(activity);
    }

    public LiveData<DocumentSnapshot> getFundById(String fundId) {
        return repo.getFundById(fundId);
    }

}
