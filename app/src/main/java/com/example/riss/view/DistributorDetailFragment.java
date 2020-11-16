package com.example.riss.view;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.riss.AppUtils.ApiCall;
import com.example.riss.R;
import com.example.riss.databinding.DistributorsStatsViewBinding;
import com.example.riss.databinding.FragmentDistributorDetailBinding;
import com.example.riss.databinding.RequestMedicineDialogViewBinding;
import com.example.riss.interfaces.ApiCallbackInterface;
import com.example.riss.models.DistributorStatsModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.riss.AppUtils.Utils.QUERY_DISTRIBUTOR_LIST;
import static com.example.riss.AppUtils.Utils.getFirestoreReference;
import static com.example.riss.AppUtils.Utils.getRandomNumber;
import static com.example.riss.AppUtils.Utils.hideAlertDialog;
import static com.example.riss.AppUtils.Utils.hideKeyboard;
import static com.example.riss.AppUtils.Utils.showAlertDialog;


public class DistributorDetailFragment extends Fragment {
    private static final String TAG = "DistributorDetailFragme";

    FragmentDistributorDetailBinding distributorDetailBinding;
    NavController navController;
    AlertDialog optionDialog;
    DistributorDetailsAdapter detailsAdapter;
    String id;
    List<DistributorStatsModel> statsModels;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        distributorDetailBinding = FragmentDistributorDetailBinding.inflate(getLayoutInflater());
        return distributorDetailBinding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        showAlertDialog(requireActivity());

        id = getArguments().getString("ID");
        Log.d(TAG, "onViewCreated: ID " + id);
        distributorDetailBinding.btnRequestMedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRequestMedicineDialog();
            }
        });

        statsModels = new ArrayList<>();
        detailsAdapter = new DistributorDetailsAdapter(statsModels);
        distributorDetailBinding.recDistributorsStatsView.setAdapter(detailsAdapter);


        setDistributorData();

        //createDemoData();
    }

    private void createDemoData() {

        Map<String, Object> map = new HashMap<>();
        List<DistributorStatsModel> statsModels = new ArrayList<>();

        statsModels.add(new DistributorStatsModel("Total Medicine", "100"));
        statsModels.add(new DistributorStatsModel("Total Visits", String.valueOf(getRandomNumber(10, 200))));
        statsModels.add(new DistributorStatsModel("Total Distribute", String.valueOf(getRandomNumber(10, 200))));

        map.put("stats", statsModels);

        getFirestoreReference().collection("DistributorsList")
                .document("xjNxdSTvRwFNKDGkpcU2")
                .update(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(requireActivity(), "Added", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(requireActivity(), "error" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showRequestMedicineDialog() {

        LayoutInflater inflater = (LayoutInflater) requireActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View formElementsView = inflater.inflate(R.layout.request_medicine_dialog_view, null, false);

        final RequestMedicineDialogViewBinding genderViewBinding = RequestMedicineDialogViewBinding.bind(formElementsView);


        genderViewBinding.btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String qty = genderViewBinding.etQuantity.getText().toString().trim();
                if (!TextUtils.isEmpty(qty)) {
                    requestMedicine(qty);
                }


            }
        });

        genderViewBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                optionDialog.dismiss();
            }
        });

        // the alert dialog
        optionDialog = new AlertDialog.Builder(requireActivity()).create();
        optionDialog.setView(formElementsView);
        optionDialog.show();

    }

    private void setDistributorData() {
        getFirestoreReference().collection(QUERY_DISTRIBUTOR_LIST).document(id).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        hideAlertDialog();
                        if (documentSnapshot.exists()) {
                            distributorDetailBinding.setDistributor(documentSnapshot);
                            Log.d(TAG, "onSuccess: " + documentSnapshot);
                            try {
                                List<Map<String, Object>> list = (List<Map<String, Object>>) documentSnapshot.get("stats");
                                for (int a = 0; a < list.size(); a++) {
                                    String itemName = (String) list.get(a).get("itemName");
                                    String itemValue = (String) list.get(a).get("itemValue");
                                    statsModels.add(new DistributorStatsModel(itemName, itemValue));
                                }
                                detailsAdapter.notifyDataSetChanged();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            setVisibility(true);
                        } else
                            Toast.makeText(requireActivity(), "not found", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                hideAlertDialog();
                Toast.makeText(requireActivity(), "try again", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: " + e.getLocalizedMessage());
            }
        });
    }

    private void setVisibility(boolean visibility) {
        distributorDetailBinding.ivMobile.setVisibility(visibility ? View.VISIBLE : View.GONE);
        distributorDetailBinding.ivAddress.setVisibility(visibility ? View.VISIBLE : View.GONE);

    }

    private void requestMedicine(String qty) {
        hideKeyboard(requireActivity());
        optionDialog.dismiss();
        showAlertDialog(requireActivity());
        ApiCall.requestMedicine(qty, new ApiCallbackInterface() {
            @Override
            public void onSuccess(Object obj) {
                hideAlertDialog();
                navController.navigate(R.id.action_distributorDetailFragment_to_medicineRequestStatusFragment);
                Toast.makeText(requireActivity(), "Requested submitted successfully", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailed(String msg) {
                hideAlertDialog();
                Toast.makeText(requireActivity(), "try again", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private class DistributorDetailsAdapter extends RecyclerView.Adapter<DistributorDetailsAdapter.DetailsVH> {
        List<DistributorStatsModel> statsModels;

        public DistributorDetailsAdapter(List<DistributorStatsModel> statsModels) {
            this.statsModels = statsModels;
        }

        @NonNull
        @Override
        public DistributorDetailsAdapter.DetailsVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            DistributorsStatsViewBinding statsViewBinding = DistributorsStatsViewBinding.inflate(layoutInflater, parent, false);
            return new DetailsVH(statsViewBinding);

        }

        @Override
        public void onBindViewHolder(@NonNull DistributorDetailsAdapter.DetailsVH holder, int position) {

            try {
                holder.statsViewBinding.viewLine.setVisibility(position == 2 ? View.GONE : View.VISIBLE);
                DistributorStatsModel statsModel = statsModels.get(position);
                holder.statsViewBinding.setStats(statsModel);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return statsModels.size();
        }

        public class DetailsVH extends RecyclerView.ViewHolder {
            DistributorsStatsViewBinding statsViewBinding;

            public DetailsVH(DistributorsStatsViewBinding statsViewBinding) {
                super(statsViewBinding.getRoot());
                this.statsViewBinding = statsViewBinding;
            }
        }
    }
}