package com.example.riss.view;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.riss.R;
import com.example.riss.databinding.FragmentCreateFundBinding;
import com.example.riss.databinding.LinkAadharDialogViewBinding;
import com.example.riss.interfaces.IUserProfileInterface;
import com.example.riss.models.Fund;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.example.riss.AppUtils.Utils.FUNDS;
import static com.example.riss.AppUtils.Utils.KEY_FUND_ID;
import static com.example.riss.AppUtils.Utils.LIKED_IDS;
import static com.example.riss.AppUtils.Utils.USER_NAME;
import static com.example.riss.AppUtils.Utils.USER_QUERY;
import static com.example.riss.AppUtils.Utils.checkUserProfile;
import static com.example.riss.AppUtils.Utils.getFirestoreReference;
import static com.example.riss.AppUtils.Utils.getUid;
import static com.example.riss.AppUtils.Utils.hideAlertDialog;
import static com.example.riss.AppUtils.Utils.hideKeyboard;
import static com.example.riss.AppUtils.Utils.isAadharVerified;
import static com.example.riss.AppUtils.Utils.showAlertDialog;
import static com.google.common.net.HttpHeaders.FROM;


public class CreateFundFragment extends Fragment {


    AlertDialog optionDialog;
    FragmentCreateFundBinding createFundBinding;
    NavController navController;

    Fund fund = new Fund();

    String fundID;

    String createdBy, mobileNo, email, address, description, from;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        createFundBinding = FragmentCreateFundBinding.inflate(getLayoutInflater());
        return createFundBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        from = getArguments().getString(FROM);


        createFundBinding.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFieldFilled()) {
                    hideKeyboard(requireActivity());
                    showAlertDialog(requireActivity());
                    getFirestoreReference().collection(USER_QUERY).document(getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            try {
                                boolean status = documentSnapshot.getBoolean(isAadharVerified);
                                if (status) {
                                    createFund();
                                } else {
                                    hideAlertDialog();
                                    showLinkAadharDialog();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(requireActivity(), "try again", Toast.LENGTH_SHORT).show();
                            hideAlertDialog();
                        }
                    });
                }
            }
        });

        setSpinnerPlan();
      /*  createFundBinding.tvFundName.setVisibility(from.equalsIgnoreCase("HomeFragment") ? View.GONE : View.VISIBLE);
        createFundBinding.tvFundID.setVisibility(from.equalsIgnoreCase("HomeFragment") ? View.GONE : View.VISIBLE);*/
    }

    private void setSpinnerPlan() {
        String[] ITEMS = {"Plan 1", "Plan 2", "Plan 3", "Plan 4", "Plan 5", "Plan 6"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(requireActivity(), android.R.layout.simple_spinner_item, ITEMS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        createFundBinding.spinner.setAdapter(adapter);
    }

    private void createFund() {
        getFirestoreReference().collection(FUNDS).add(fund).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                hideAlertDialog();
                if (task.isSuccessful()) {
                    Toast.makeText(requireContext(), "Fund Created Successfully", Toast.LENGTH_SHORT).show();
                    String fundId = task.getResult().getId();

                    Bundle bundle = new Bundle();
                    bundle.putString(KEY_FUND_ID, fundId);
                    navController.navigate(R.id.action_createFundFragment_to_fundFragment, bundle);
                } else
                    Toast.makeText(requireContext(), "failed to create fund, try again", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                hideAlertDialog();
                Toast.makeText(requireContext(), "failed to create fund, try again", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private boolean isFieldFilled() {

        createdBy = createFundBinding.etCreatedBy.getText().toString().trim();
        mobileNo = createFundBinding.etMobileNumber.getText().toString().trim();
        email = createFundBinding.etEmail.getText().toString().trim();
        address = createFundBinding.etAddress.getText().toString().trim();
        description = createFundBinding.etDescription.getText().toString().trim();


        if (TextUtils.isEmpty(createdBy)) {
            createFundBinding.etCreatedBy.setError("required");
            return false;
        } else if (TextUtils.isEmpty(mobileNo)) {
            createFundBinding.etMobileNumber.setError("required");
            return false;
        } else if (TextUtils.isEmpty(email)) {
            createFundBinding.etEmail.setError("required");
            return false;
        } else if (TextUtils.isEmpty(address)) {
            createFundBinding.etAddress.setError("required");
            return false;
        } else if (TextUtils.isEmpty(description)) {
            createFundBinding.etDescription.setError("required");
            return false;
        } else {

            List<String> likeList = new ArrayList<>();
            fund.setFundName(createdBy);
            fund.setMobileNo(mobileNo);
            fund.setEmail(email);
            fund.setAddress(address);
            fund.setDescription(description);
            fund.setUid(getUid());
            fund.setTotalInvested(0);
            fund.setCurrentValue(0);
            fund.setTimestamp(System.currentTimeMillis());
            fund.setLikedIds(likeList);

            if (from.equalsIgnoreCase("SupportFundFragment")) {
                fundID = getArguments().getString("id");
                fund.setRefId(fundID);
            }

            return true;
        }
    }

    private void showLinkAadharDialog() {
        LayoutInflater inflater = (LayoutInflater) requireActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View formElementsView = inflater.inflate(R.layout.link_aadhar_dialog_view, null, false);

        LinkAadharDialogViewBinding genderViewBinding = LinkAadharDialogViewBinding.bind(formElementsView);


        genderViewBinding.btnLinkAadhar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                optionDialog.dismiss();
                navController.navigate(R.id.action_createFundFragment_to_profileFragment);

            }
        });

        // the alert dialog
        optionDialog = new AlertDialog.Builder(requireActivity()).create();
        optionDialog.setView(formElementsView);
        optionDialog.show();
    }
}