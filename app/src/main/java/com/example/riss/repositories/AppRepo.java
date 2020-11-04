package com.example.riss.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.riss.models.FundsModel;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class AppRepo {

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    public MutableLiveData<List<FundsModel>> topFundsMutableLiveData;


    public LiveData<List<FundsModel>> getTopFundsData() {
        if (topFundsMutableLiveData == null) {
            topFundsMutableLiveData = new MutableLiveData<>();
            loadTopFundsData();
        }
        return topFundsMutableLiveData;

    }

    private void loadTopFundsData() {

        /*firestore.collection(FUNDS).limit(20).addSnapshotListener(HomeScreen.getInstance(), new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error == null && !value.isEmpty()) {

                    topFundsMutableLiveData.setValue(value.getDocuments());
                }
            }
        });*/

        List<FundsModel> fundsModels = new ArrayList<>();
        for (int a = 0; a < 20; a++) {
            FundsModel fundsModel = new FundsModel();
            fundsModel.setFundName("Fund name");
            fundsModel.setFundImage("Fund name");
            fundsModel.setCreatedBy("Fund name");
            fundsModel.setLikes("Fund name");
            fundsModel.setTotalInvested("Fund name");
            fundsModel.setCurrentValue("Fund name");
            fundsModels.add(fundsModel);
        }
        topFundsMutableLiveData.setValue(fundsModels);
    }

}
