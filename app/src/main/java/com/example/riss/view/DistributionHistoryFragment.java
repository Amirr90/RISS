package com.example.riss.view;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.riss.AdapterInterface;
import com.example.riss.R;
import com.example.riss.adapters.DistributorDetailsAdapter;
import com.example.riss.databinding.FragmentDistributionHistoryBinding;
import com.example.riss.viewModel.AppViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

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
    AppViewModel appViewModel;

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


        appViewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);

        snapshots = new ArrayList<>();

        detailsAdapter = new DistributorDetailsAdapter(this, snapshots);

        distributionHistoryBinding.recDistributionList.setAdapter(detailsAdapter);

        distributionHistoryBinding.editTextTextSearchMember.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                if (null != s.toString()) {
                    loadData(s.toString());
                }
            }
        });

        distributionHistoryBinding.addDistributorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_distributionHistoryFragment_to_addDistributionFragment);
            }
        });

        loadData(null);

    }

    private void loadData(String name) {
        if (null == name) {
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
        } else {
            getFirestoreReference().collection(MedicineDistribute)
                    //.whereEqualTo(uid, getUid())
                    .orderBy("name")
                    .startAt(name)
                    .endAt(name + "\uf8ff")
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if (null != queryDocumentSnapshots && !queryDocumentSnapshots.isEmpty()) {
                                snapshots.clear();
                                snapshots.addAll(queryDocumentSnapshots.getDocuments());
                                detailsAdapter.notifyDataSetChanged();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }


    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
    }

    @Override
    public void onItemClicked(Object o) {
        navController.navigate(R.id.action_distributionHistoryFragment_to_addDistributionFragment);
    }
}