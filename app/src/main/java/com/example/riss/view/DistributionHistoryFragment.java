package com.example.riss.view;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.riss.AdapterInterface;
import com.example.riss.AppUtils.Utils;
import com.example.riss.R;
import com.example.riss.adapters.DistributorDetailsAdapter;
import com.example.riss.databinding.FragmentDistributionHistoryBinding;
import com.example.riss.databinding.FragmentDistributorDetailBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.model.Document;

import java.util.ArrayList;
import java.util.List;

import static com.example.riss.AppUtils.Utils.MedicineDistribute;
import static com.example.riss.AppUtils.Utils.TIMESTAMP;
import static com.example.riss.AppUtils.Utils.getFirestoreReference;
import static com.example.riss.AppUtils.Utils.getUid;
import static com.example.riss.AppUtils.Utils.uid;


public class DistributionHistoryFragment extends Fragment implements AdapterInterface {

    FragmentDistributionHistoryBinding distributionHistoryBinding;
    NavController navController;
    DistributorDetailsAdapter detailsAdapter;
    List<DocumentSnapshot> snapshots;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        distributionHistoryBinding = FragmentDistributionHistoryBinding.inflate(getLayoutInflater());
        return distributionHistoryBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        snapshots = new ArrayList<>();

        detailsAdapter = new DistributorDetailsAdapter(this, snapshots);

        distributionHistoryBinding.recDistributionList.setAdapter(detailsAdapter);

        loadData();

    }

    private void loadData() {
        getFirestoreReference().collection(MedicineDistribute)
                .whereEqualTo(uid, getUid())
                .orderBy(TIMESTAMP, Query.Direction.DESCENDING)
                .limit(30)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        if (null != queryDocumentSnapshots && !queryDocumentSnapshots.isEmpty()) {
                            snapshots.clear();
                            snapshots.addAll(queryDocumentSnapshots.getDocuments());
                            detailsAdapter.notifyDataSetChanged();
                        } else
                            navController.navigate(R.id.action_distributionHistoryFragment_to_addDistributionFragment);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }


    @Override
    public void onItemClicked(Object o) {
        navController.navigate(R.id.action_distributionHistoryFragment_to_addDistributionFragment);
    }
}