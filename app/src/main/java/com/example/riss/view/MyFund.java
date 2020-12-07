package com.example.riss.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.riss.R;
import com.example.riss.adapters.SupportAdapter;
import com.example.riss.databinding.FragmentMyFundBinding;
import com.example.riss.databinding.RequestAmountDialogViewBinding;
import com.example.riss.databinding.TermsConditionDialogViewBinding;
import com.example.riss.interfaces.IUserProfileInterface;
import com.example.riss.interfaces.OnClickListener;
import com.firebase.ui.auth.data.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static com.example.riss.AppUtils.Utils.FUNDS;
import static com.example.riss.AppUtils.Utils.FundSupport;
import static com.example.riss.AppUtils.Utils.KEY_FUND_ID;
import static com.example.riss.AppUtils.Utils.LIKED_IDS;
import static com.example.riss.AppUtils.Utils.PAYMENT_STATUS_PENDING;
import static com.example.riss.AppUtils.Utils.TIMESTAMP;
import static com.example.riss.AppUtils.Utils.TotalFundAmount;
import static com.example.riss.AppUtils.Utils.checkUserProfile;
import static com.example.riss.AppUtils.Utils.fundAmount_KEY;
import static com.example.riss.AppUtils.Utils.getCountInRomanFormat;
import static com.example.riss.AppUtils.Utils.getCurrencyFormat;
import static com.example.riss.AppUtils.Utils.getData;
import static com.example.riss.AppUtils.Utils.getFirestoreReference;
import static com.example.riss.AppUtils.Utils.getUid;
import static com.example.riss.AppUtils.Utils.hideAlertDialog;
import static com.example.riss.AppUtils.Utils.showAlertDialog;
import static com.example.riss.AppUtils.Utils.support;
import static com.example.riss.AppUtils.Utils.totalInvested;


public class MyFund extends Fragment implements OnClickListener {
    private static final String TAG = "MyFund";

    FragmentMyFundBinding myFundBinding;
    NavController navController;
    String fundId;

    SupportAdapter supportAdapter;
    List<DocumentSnapshot> supportFundsList;

    AlertDialog optionDialog;

    String fundAmount = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myFundBinding = FragmentMyFundBinding.inflate(getLayoutInflater());
        return myFundBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        fundId = getArguments().getString(KEY_FUND_ID);


        supportFundsList = new ArrayList<>();
        supportAdapter = new SupportAdapter(supportFundsList, this);
        myFundBinding.supporterRec.setAdapter(supportAdapter);

        setFund();

        myFundBinding.btnWithdrawFund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTermsAndConditionDialog();
            }
        });

        setPendingAmount();

    }

    private void setPendingAmount() {

        getFirestoreReference().collection("WithdrawFundAmountRequest")
                .whereEqualTo("uid", getUid())
                .whereEqualTo("status", PAYMENT_STATUS_PENDING)
                .orderBy(TIMESTAMP, Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Log.d(TAG, "onSuccess: " + queryDocumentSnapshots.getDocuments().toString());
                        if (null != queryDocumentSnapshots) {
                            if (!queryDocumentSnapshots.getDocuments().isEmpty()) {
                                String amount = queryDocumentSnapshots.getDocuments().get(0).getString("amountToWithdraw");
                                myFundBinding.tvPendingAmount.setText("Pending Amount : " + getCurrencyFormat(amount));
                                myFundBinding.tvPendingAmount.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: " + e.getLocalizedMessage());
            }
        });

    }

    private void showTermsAndConditionDialog() {
        LayoutInflater inflater = (LayoutInflater) requireActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View formElementsView = inflater.inflate(R.layout.terms_condition_dialog_view, null, false);

        final TermsConditionDialogViewBinding genderViewBinding = TermsConditionDialogViewBinding.bind(formElementsView);


        genderViewBinding.btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                optionDialog.dismiss();
                navController.navigate(R.id.action_myFund_to_termsConditionFragment);
            }
        });

        genderViewBinding.btnDismiss.setText("Proceed");
        genderViewBinding.btnDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                optionDialog.dismiss();
                if (fundAmount != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString("fundAmount", fundAmount);
                    bundle.putString("fundId", fundId);
                    navController.navigate(R.id.action_myFund_to_withdrawFundAmountFragment, bundle);
                } else {
                    Toast.makeText(requireActivity(), "try again", Toast.LENGTH_SHORT).show();
                }

            }
        });

        // the alert dialog
        optionDialog = new AlertDialog.Builder(requireActivity()).create();
        optionDialog.setView(formElementsView);
        optionDialog.show();

    }

    private void setFund() {
        showAlertDialog(requireActivity());
        getFirestoreReference().collection(FUNDS).document(fundId).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (null == documentSnapshot && !documentSnapshot.exists()) {
                            Log.d(TAG, "onFailure: document not found");
                            return;
                        }

                        try {

                            setSupportRecData(documentSnapshot);
                            myFundBinding.setFund(documentSnapshot);

                            fundAmount = String.valueOf(documentSnapshot.getLong(fundAmount_KEY));
                            myFundBinding.textView53.setText(getCurrencyFormat(documentSnapshot.getLong(fundAmount_KEY)));

                            List<String> list = (List<String>) documentSnapshot.get(LIKED_IDS);
                            myFundBinding.tvLikes.setText(getCountInRomanFormat(list.size()));

                            myFundBinding.tvMember.setText(getCountInRomanFormat(documentSnapshot.getLong(support)));

                        } catch (Exception e) {
                            hideAlertDialog();
                            e.printStackTrace();
                            Log.d(TAG, "onSuccess: in try catch " + e.getLocalizedMessage());
                        }
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

    private void setSupportRecData(DocumentSnapshot documentSnapshot) {
        documentSnapshot.getReference().collection(FundSupport)
                .limit(10)
                .orderBy(TIMESTAMP, Query.Direction.DESCENDING)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                hideAlertDialog();
                for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                    Log.d(TAG, "Document ID : " + snapshot.getId());
                    Log.d(TAG, "Amount : " + snapshot.getLong("amount"));
                }
                supportFundsList.clear();
                supportFundsList.addAll(queryDocumentSnapshots.getDocuments());
                supportAdapter.notifyDataSetChanged();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                hideAlertDialog();
                Log.d(TAG, "onFailure: " + e.getLocalizedMessage());
            }
        });
    }

    @Override
    public void onItemClick(int position, String id) {

    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
    }
}