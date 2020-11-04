package com.example.riss.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.riss.models.FundsModel;
import com.example.riss.repositories.AppRepo;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

public class AppViewModel extends ViewModel {

    AppRepo repo = new AppRepo();

    public LiveData<List<FundsModel>> getTopFundsData() {
        return repo.getTopFundsData();
    }
}
