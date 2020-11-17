package com.example.riss.view;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.riss.AppUtils.Utils;
import com.example.riss.PaymentUtils.Pay;
import com.example.riss.PaymentUtils.PaymentCallback;
import com.example.riss.PaymentUtils.StartPayment;
import com.example.riss.R;
import com.example.riss.databinding.FragmentSupportFundBinding;
import com.example.riss.databinding.RequestAmountDialogViewBinding;
import com.example.riss.databinding.RequestMedicineDialogViewBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import okhttp3.internal.Util;

import static com.example.riss.AppUtils.Utils.FundSupportPayment;
import static com.example.riss.AppUtils.Utils.PAYMENT_STATUS_PENDING;
import static com.example.riss.AppUtils.Utils.SUPPORT_TYPE_DIRECT;
import static com.example.riss.AppUtils.Utils.hideAlertDialog;
import static com.example.riss.AppUtils.Utils.showAlertDialog;
import static com.google.common.net.HttpHeaders.FROM;


public class SupportFundFragment extends Fragment {
    private static final String TAG = "SupportFundFragment";


    NavController navController;
    FragmentSupportFundBinding supportFundBinding;
    public static StartPayment payment;
    String fundId;
    AlertDialog optionDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        supportFundBinding = FragmentSupportFundBinding.inflate(getLayoutInflater());
        return supportFundBinding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        fundId = getArguments().getString("id");
        supportFundBinding.btnCreateFund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString(FROM, "SupportFundFragment");
                bundle.putString("id", fundId);
                navController.navigate(R.id.action_supportFundFragment_to_createFundFragment, bundle);
            }
        });

        supportFundBinding.btnDirectSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showAmountDialog();

            }
        });
    }

    private void showAmountDialog() {
        LayoutInflater inflater = (LayoutInflater) requireActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View formElementsView = inflater.inflate(R.layout.request_amount_dialog_view, null, false);

        final RequestAmountDialogViewBinding genderViewBinding = RequestAmountDialogViewBinding.bind(formElementsView);


        genderViewBinding.btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amount = genderViewBinding.etQuantity.getText().toString().trim();
                if (!TextUtils.isEmpty(amount)) {
                    optionDialog.dismiss();
                    createOrder(amount);
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

    private void createOrder(String amount) {
        showAlertDialog(requireActivity());
        DocumentReference ref = Utils.getFirestoreReference().collection(FundSupportPayment).document();
        String txId = ref.getId();

        payment = new StartPayment(requireActivity());

        payment.setTxId(txId);
        payment.setUid(Utils.getUid());
        payment.setFundId(fundId);
        payment.setSupportType(SUPPORT_TYPE_DIRECT);
        payment.setAmount(amount);
        payment.setPaymentStatus(PAYMENT_STATUS_PENDING);
        payment.setTimestamp(System.currentTimeMillis());

        Utils.getFirestoreReference().collection(FundSupportPayment)
                .document(txId)
                .set(payment)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        payment.initPayment(new PaymentCallback() {
                            @Override
                            public void onPaymentSuccess() {
                                hideAlertDialog();
                                Toast.makeText(requireActivity(), "Payment Success", Toast.LENGTH_SHORT).show();
                                navController.navigate(R.id.action_supportFundFragment_to_transactionCompleteFragment);
                            }

                            @Override
                            public void onPaymentFailed() {
                                hideAlertDialog();
                                Toast.makeText(requireActivity(), "Payment Failed", Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(requireActivity(), "try again", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
    }

    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
    }
}