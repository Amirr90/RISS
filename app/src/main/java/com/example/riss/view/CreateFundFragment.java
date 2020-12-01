package com.example.riss.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.example.riss.PaymentUtils.PaymentCallback;
import com.example.riss.PaymentUtils.StartPayment;
import com.example.riss.R;
import com.example.riss.databinding.FragmentCreateFundBinding;
import com.example.riss.databinding.LinkAadharDialogViewBinding;
import com.example.riss.models.Fund;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.riss.AppUtils.Utils.FUNDS;
import static com.example.riss.AppUtils.Utils.FundSupportPayment;
import static com.example.riss.AppUtils.Utils.KEY_FUND_ID;
import static com.example.riss.AppUtils.Utils.PAYMENT_STATUS_FAILED;
import static com.example.riss.AppUtils.Utils.PAYMENT_STATUS_PENDING;
import static com.example.riss.AppUtils.Utils.PAYMENT_STATUS_SUCCESS;
import static com.example.riss.AppUtils.Utils.SUPPORT_TYPE_BY_CREATING_FUND;
import static com.example.riss.AppUtils.Utils.USER_QUERY;
import static com.example.riss.AppUtils.Utils.getDuration;
import static com.example.riss.AppUtils.Utils.getFirestoreReference;
import static com.example.riss.AppUtils.Utils.getInitialValue;
import static com.example.riss.AppUtils.Utils.getPlans;
import static com.example.riss.AppUtils.Utils.getRandomNumber;
import static com.example.riss.AppUtils.Utils.getUid;
import static com.example.riss.AppUtils.Utils.hideAlertDialog;
import static com.example.riss.AppUtils.Utils.hideKeyboard;
import static com.example.riss.AppUtils.Utils.isAadharVerified;
import static com.example.riss.AppUtils.Utils.isInternetConnected;
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
    String createdFundId;

    public static StartPayment startPayment;


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
                if (isInternetConnected()) {
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
                                        startPayment();

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
                } else {
                    Toast.makeText(requireActivity(), "No internet available", Toast.LENGTH_SHORT).show();
                }
            }
        });

        setSpinnerPlan();

    }


    private void startPayment() {
        createdFundId = "RISS" + System.currentTimeMillis();
        String txId = String.valueOf(getRandomNumber(10000000, 99999999));
        startPayment = new StartPayment(requireActivity());
        startPayment.setAmount(initialValue);
        startPayment.setUid(getUid());
        startPayment.setFundName(createdBy);
        startPayment.setSupportType(SUPPORT_TYPE_BY_CREATING_FUND);
        startPayment.setTxId(String.valueOf(System.currentTimeMillis()));
        startPayment.setTxId(txId);
        startPayment.setTimestamp(System.currentTimeMillis());
        startPayment.setFundId(createdFundId);
        startPayment.setPaymentStatus(PAYMENT_STATUS_PENDING);

        //init payment
        getFirestoreReference().collection(FundSupportPayment).document(createdFundId)
                .set(startPayment).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                startPayment.initPayment(new PaymentCallback() {
                    @Override
                    public void onPaymentSuccess() {
                        updatePaymentStatus(true);
                    }

                    @Override
                    public void onPaymentFailed() {
                        updatePaymentStatus(false);
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });


    }

    private void updatePaymentStatus(final boolean isPaid) {
        Map<String, Object> updatePaymentMap = new HashMap<>();
        if (isPaid)
            updatePaymentMap.put("paymentStatus", PAYMENT_STATUS_SUCCESS);
        else {
            updatePaymentMap.put("paymentStatus", PAYMENT_STATUS_FAILED);
        }
        getFirestoreReference().collection(FundSupportPayment).document(createdFundId).update(updatePaymentMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                if (isPaid) {
                    createFund(createdFundId);
                } else
                    Toast.makeText(requireActivity(), "Payment Failed", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                new AlertDialog.Builder(requireActivity())
                        .setMessage("Failed to update transaction, If money is deducted it will revert back to you with in 5 working days or contact us")
                        .setPositiveButton("Contact us", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                Toast.makeText(requireActivity(), "show Contact us page", Toast.LENGTH_SHORT).show();
                            }
                        }).setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).setCancelable(false)
                        .show();
            }
        });

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


    private void createFund(final String createdFundId) {
        getFirestoreReference().collection(FUNDS).document(createdFundId)
                .set(fund)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        hideAlertDialog();
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: Task");
                            Toast.makeText(requireContext(), "Fund Created Successfully", Toast.LENGTH_SHORT).show();

                            Bundle bundle = new Bundle();
                            bundle.putString(KEY_FUND_ID, createdFundId);
                            navController.navigate(R.id.action_createFundFragment_to_fundFragment, bundle);
                        } else {
                            Log.d(TAG, "onComplete: fail " + task.getException());
                            Toast.makeText(requireActivity(), "failed to create fund, try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {
                hideAlertDialog();
                Toast.makeText(requireActivity(), "failed to create fund, try again", Toast.LENGTH_SHORT).show();
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
            fund.setSupport(0);
            fund.setCreatedBy("");
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