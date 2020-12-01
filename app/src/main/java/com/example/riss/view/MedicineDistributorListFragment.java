package com.example.riss.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.riss.AdapterInterface;
import com.example.riss.R;
import com.example.riss.adapters.DistributorAdapter;
import com.example.riss.databinding.FragmentMedicineDistributorListBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.List;

import static android.os.Build.ID;
import static com.example.riss.AppUtils.Utils.NAME;
import static com.example.riss.AppUtils.Utils.QUERY_DISTRIBUTOR_LIST;
import static com.example.riss.AppUtils.Utils.TIMESTAMP;
import static com.example.riss.AppUtils.Utils.getFirestoreReference;
import static com.example.riss.AppUtils.Utils.hideAlertDialog;
import static com.example.riss.AppUtils.Utils.hideKeyboard;
import static com.example.riss.AppUtils.Utils.showAlertDialog;


public class MedicineDistributorListFragment extends Fragment implements AdapterInterface {

    private static final String TAG = "MedicineDistributorList";

    FragmentMedicineDistributorListBinding distributorListBinding;
    NavController navController;

    DistributorAdapter distributorAdapter;
    List<DocumentSnapshot> distributorsList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        distributorListBinding = FragmentMedicineDistributorListBinding.inflate(getLayoutInflater());
        return distributorListBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        distributorsList = new ArrayList<>();
        distributorAdapter = new DistributorAdapter(distributorsList, this);
        distributorListBinding.recDistributors.setAdapter(distributorAdapter);
        loadDistributorsData(null);

        distributorListBinding.editTextTextSearchDistributor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                if (null != s.toString()) {
                    loadDistributorsData(s.toString());
                }
            }
        });

    }

    private void loadDistributorsData(String distributorName) {
        if (distributorName == null) {
            showAlertDialog(requireActivity());
            getFirestoreReference().collection(QUERY_DISTRIBUTOR_LIST)
                    .limit(20)
                    .orderBy(TIMESTAMP, Query.Direction.DESCENDING)
                    .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    hideAlertDialog();
                    try {
                        if (null == queryDocumentSnapshots || queryDocumentSnapshots.isEmpty()) {
                            return;
                        }
                        distributorsList.addAll(queryDocumentSnapshots.getDocuments());
                        distributorAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "onFailure: " + e.getLocalizedMessage());
                }
            });
        } else {
            distributorListBinding.loadingLayout.getRoot().setVisibility(View.VISIBLE);
            getFirestoreReference().collection(QUERY_DISTRIBUTOR_LIST)
                    .orderBy("name")
                    .startAt(distributorName)
                    .endAt(distributorName + "\uf8ff")
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            distributorListBinding.loadingLayout.getRoot().setVisibility(View.GONE);
                            try {

                                if (null == queryDocumentSnapshots || queryDocumentSnapshots.isEmpty()) {
                                    return;
                                }
                                distributorsList.clear();
                                distributorsList.addAll(queryDocumentSnapshots.getDocuments());
                                distributorAdapter.notifyDataSetChanged();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    distributorListBinding.loadingLayout.getRoot().setVisibility(View.GONE);
                    hideAlertDialog();
                    Toast.makeText(requireContext(), "try again", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onItemClicked(Object o) {

        String distributorId = (String) o;
        Log.d(TAG, "onItemClicked: ID " + distributorId);
        Bundle bundle = new Bundle();
        bundle.putString("ID", distributorId);
        navController.navigate(R.id.action_medicineDistributorListFragment_to_distributorDetailFragment, bundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
    }
}