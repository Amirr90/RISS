package com.example.riss.view;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.riss.AppUtils.AddMonthsToCurrentDate;
import com.example.riss.R;
import com.example.riss.databinding.FragmentCreateFundBinding;
import com.example.riss.databinding.LinkAadharDialogViewBinding;
import com.example.riss.models.Fund;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import static com.example.riss.AppUtils.Utils.FUNDS;
import static com.example.riss.AppUtils.Utils.KEY_FUND_ID;
import static com.example.riss.AppUtils.Utils.USER_QUERY;
import static com.example.riss.AppUtils.Utils.getDuration;
import static com.example.riss.AppUtils.Utils.getFirestoreReference;
import static com.example.riss.AppUtils.Utils.getInitialValue;
import static com.example.riss.AppUtils.Utils.getPlans;
import static com.example.riss.AppUtils.Utils.getUid;
import static com.example.riss.AppUtils.Utils.hideAlertDialog;
import static com.example.riss.AppUtils.Utils.hideKeyboard;
import static com.example.riss.AppUtils.Utils.isAadharVerified;
import static com.example.riss.AppUtils.Utils.showAlertDialog;
import static com.google.common.net.HttpHeaders.FROM;


public class CreateFundFragment extends Fragment {


    private static final String TAG = "CreateFundFragment";
    AlertDialog optionDialog;
    FragmentCreateFundBinding createFundBinding;
    NavController navController;

    Fund fund = new Fund();

    String fundID;

    String createdBy, mobileNo, email, address, description, from;

    String initialValue, duration;
    String[] ITEMS;

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
                                    Log.d(TAG, "onSuccess: Creating fund ");
                                    createFund();
                                } else {
                                    hideAlertDialog();
                                    showLinkAadharDialog();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.d(TAG, "error in creating Fund:=>" + e.getLocalizedMessage());
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            hideAlertDialog();
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
        ITEMS = getPlans();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(requireActivity(), android.R.layout.simple_spinner_item, ITEMS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        createFundBinding.spinner.setAdapter(adapter);
        createFundBinding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position >= 0) {
                    initialValue = getInitialValue(position);
                    duration = getDuration(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void createFund() {
        Log.d(TAG, "createFund: ");
        getFirestoreReference().collection(FUNDS).add(fund).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                hideAlertDialog();
                if (task.isSuccessful()) {
                    Log.d(TAG, "onComplete: Task");
                    Toast.makeText(requireContext(), "Fund Created Successfully", Toast.LENGTH_SHORT).show();
                    String fundId = task.getResult().getId();

                    Bundle bundle = new Bundle();
                    bundle.putString(KEY_FUND_ID, fundId);
                    navController.navigate(R.id.action_createFundFragment_to_fundFragment, bundle);
                } else {
                    Log.d(TAG, "onComplete: fail " + task.getException());
                    Toast.makeText(requireActivity(), "failed to create fund, try again", Toast.LENGTH_SHORT).show();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: " + e.getLocalizedMessage());
                hideAlertDialog();
                Toast.makeText(requireActivity(), "failed to create fund, try again", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(requireActivity(), "Fund Name required", Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(mobileNo)) {
            createFundBinding.etMobileNumber.setError("required");
            Toast.makeText(requireActivity(), "Mobile number required", Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(email)) {
            createFundBinding.etEmail.setError("required");
            Toast.makeText(requireActivity(), "email required", Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(address)) {
            createFundBinding.etAddress.setError("required");
            Toast.makeText(requireActivity(), "address required", Toast.LENGTH_SHORT).show();
            return false;
        } else if (null == initialValue) {
            Toast.makeText(requireActivity(), "select plan", Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(description)) {
            createFundBinding.etDescription.setError("required");
            Toast.makeText(requireActivity(), "description required", Toast.LENGTH_SHORT).show();
            return false;
        } else {

            AddMonthsToCurrentDate date = new AddMonthsToCurrentDate();

            List<String> likeList = new ArrayList<>();
            fund.setFundName(createdBy);
            fund.setMobileNo(mobileNo);
            fund.setEmail(email);
            fund.setAddress(address);
            fund.setDescription(description);
            fund.setUid(getUid());
            fund.setTotalInvested(0);
            fund.setCurrentValue(0);
            fund.setInitialValue(Integer.parseInt(initialValue));
            fund.setTimestamp(System.currentTimeMillis());
            fund.setLikedIds(likeList);
            fund.setExpiryDate(date.getDateAfterMonth(duration));
            fund.setStartDate(date.getCurrentDate());
            fund.setDuration(Integer.parseInt(duration));


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