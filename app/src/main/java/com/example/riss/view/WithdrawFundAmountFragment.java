package com.example.riss.view;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.riss.AppUtils.Utils;
import com.example.riss.HomeScreen;
import com.example.riss.R;
import com.example.riss.databinding.FragmentWithdrawFundAmountBinding;
import com.example.riss.databinding.TermsConditionDialogViewBinding;
import com.example.riss.databinding.WithdrawalAmountViewBinding;
import com.example.riss.interfaces.ApiCallbackInterface;
import com.example.riss.interfaces.OnClickListener;

import java.util.List;

import static com.example.riss.AppUtils.ApiCall.withdrawFund;
import static com.example.riss.AppUtils.Utils.getCurrencyFormat;
import static com.example.riss.AppUtils.Utils.getData;
import static com.example.riss.AppUtils.Utils.getHandlingCharger;
import static com.example.riss.AppUtils.Utils.hideAlertDialog;
import static com.example.riss.AppUtils.Utils.showAlertDialog;


public class WithdrawFundAmountFragment extends Fragment implements OnClickListener {
    private static final String TAG = "WithdrawFundAmountFragm";
    FragmentWithdrawFundAmountBinding amountBinding;
    NavController navController;
    String fundAmount;
    String finalAmount = null;
    AlertDialog optionDialog;
    String fundId;


    double fundAmountAfterPercent;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        amountBinding = FragmentWithdrawFundAmountBinding.inflate(getLayoutInflater());
        return amountBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        fundAmount = getArguments().getString("fundAmount");
        fundId = getArguments().getString("fundId");

        amountBinding.textView87.setText(Utils.getCurrencyFormat(fundAmount));
        amountBinding.recData.setAdapter(new DataAdapter(getData(), this));

        updateWithdrawAmount("10", 0);

        amountBinding.imageView13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeScreen.getInstance().onSupportNavigateUp();
            }
        });

        amountBinding.btnWithdrawFinalAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double amount = Double.parseDouble(finalAmount);
                if (null != finalAmount && amount >= 1000) {
                    showFinalAmountSubmitDialog();
                } else {
                    showNotSufficientWithdrawalAmount(amount);
                }
            }
        });

    }

    private void showNotSufficientWithdrawalAmount(double amount) {
        LayoutInflater inflater = (LayoutInflater) requireActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View formElementsView = inflater.inflate(R.layout.terms_condition_dialog_view, null, false);

        final TermsConditionDialogViewBinding genderViewBinding = TermsConditionDialogViewBinding.bind(formElementsView);


        genderViewBinding.tvMessage.setText("Your withdrawal Amount " + getCurrencyFormat(finalAmount) + " is not sufficient to submit withdrawal request.");
        genderViewBinding.tvMessage.setTextColor(R.color.colorRedLight);
        genderViewBinding.btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                optionDialog.dismiss();

            }
        });

        genderViewBinding.btnDismiss.setText("View T&C");
        genderViewBinding.btnDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                optionDialog.dismiss();
                navController.navigate(R.id.action_withdrawFundAmountFragment_to_termsConditionFragment);

            }
        });

        // the alert dialog
        optionDialog = new AlertDialog.Builder(requireActivity()).create();
        optionDialog.setView(formElementsView);
        optionDialog.show();
    }


    private void showFinalAmountSubmitDialog() {
        LayoutInflater inflater = (LayoutInflater) requireActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View formElementsView = inflater.inflate(R.layout.terms_condition_dialog_view, null, false);

        final TermsConditionDialogViewBinding genderViewBinding = TermsConditionDialogViewBinding.bind(formElementsView);


        genderViewBinding.tvMessage.setText("Submitting request to withdraw  " + getCurrencyFormat(finalAmount));
        genderViewBinding.btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                optionDialog.dismiss();
                submittingWithdrawFundAmount(fundId);
            }
        });

        genderViewBinding.btnDismiss.setTag("Cancel");
        genderViewBinding.btnDismiss.setOnClickListener(new View.OnClickListener() {
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

    private void submittingWithdrawFundAmount(String fundId) {
        showAlertDialog(requireActivity(), false);

        double fundWithdrawAmount = fundAmountAfterPercent;

        double amountToWithdrawToBank = Double.parseDouble(finalAmount);

        Log.d(TAG, "submittingWithdrawFundAmount: " + fundId);
        withdrawFund(fundId, fundWithdrawAmount, amountToWithdrawToBank, new ApiCallbackInterface() {
            @Override
            public void onSuccess(Object obj) {
                hideAlertDialog();
                String msg = (String) obj;
                Toast.makeText(requireActivity(), msg, Toast.LENGTH_LONG).show();
                navController.navigate(R.id.action_withdrawFundAmountFragment_to_withdrawalSuccessfullFragment);
            }

            @Override
            public void onFailed(String msg) {
                hideAlertDialog();
                // Toast.makeText(requireActivity(), msg, Toast.LENGTH_LONG).show();
                showFailedDialog(msg);
            }
        });
    }


    private void showFailedDialog(String msg) {
        LayoutInflater inflater = (LayoutInflater) requireActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View formElementsView = inflater.inflate(R.layout.terms_condition_dialog_view, null, false);

        final TermsConditionDialogViewBinding genderViewBinding = TermsConditionDialogViewBinding.bind(formElementsView);


        genderViewBinding.tvMessage.setText(msg);
        genderViewBinding.btnOk.setText("Dismiss");
        genderViewBinding.btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                optionDialog.dismiss();
            }
        });

        genderViewBinding.btnDismiss.setVisibility(View.GONE);
        genderViewBinding.btnDismiss.setOnClickListener(new View.OnClickListener() {
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

    @Override
    public void onItemClick(int position, String limit) {
        updateWithdrawAmount(limit, position);

    }

    private void updateWithdrawAmount(String limit, int position) {
        int amount = Integer.parseInt(fundAmount);
        int percent = Integer.parseInt(limit);

        int percentAmount = (percent * amount) / 100;
        fundAmountAfterPercent = percentAmount;
        amountBinding.textView91.setText(Utils.getCurrencyFormat(percentAmount));

        amountBinding.textView92.setText("Handling Changes  " + getHandlingCharger(position) + "%");
        double hc = (Double.parseDouble(getHandlingCharger(position)) * percentAmount) / 100;
        amountBinding.textView93.setText("-" + Utils.getCurrencyFormat(hc));

        finalAmount = String.valueOf(percentAmount - hc);
        amountBinding.textView95.setText(Utils.getCurrencyFormat(finalAmount));

    }

    private class DataAdapter extends RecyclerView.Adapter<DataAdapter.DataVH> {
        List<String> list;
        OnClickListener clickListener;

        int selectedPosition = 0;

        public DataAdapter(List<String> list, OnClickListener clickListener) {
            this.list = list;
            this.clickListener = clickListener;
        }

        @NonNull
        @Override
        public DataVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            WithdrawalAmountViewBinding amountViewBinding = WithdrawalAmountViewBinding.inflate(inflater, parent, false);

            return new DataVH(amountViewBinding);

        }

        @Override
        public void onBindViewHolder(@NonNull final DataVH holder, final int position) {
            try {

                holder.amountViewBinding.textView89.setText(list.get(position) + "%");
            } catch (Exception e) {
                e.printStackTrace();
            }

            holder.amountViewBinding.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedPosition = position;
                    clickListener.onItemClick(position, list.get(position));
                    notifyDataSetChanged();

                }
            });

            if (selectedPosition == position) {
                changeColor(true, holder);
            } else {
                changeColor(false, holder);
            }
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class DataVH extends RecyclerView.ViewHolder {
            WithdrawalAmountViewBinding amountViewBinding;

            public DataVH(@NonNull WithdrawalAmountViewBinding amountViewBinding) {
                super(amountViewBinding.getRoot());
                this.amountViewBinding = amountViewBinding;
            }
        }
    }

    private void changeColor(Boolean selected, DataAdapter.DataVH holder) {
        if (selected) {
            holder.amountViewBinding.textView89.setTextColor(getResources().getColor(R.color.ms_white));
            holder.amountViewBinding.cardView.setBackgroundTintList(ContextCompat.getColorStateList(requireActivity(), R.color.colorPrimary));
        } else {
            holder.amountViewBinding.cardView.setBackgroundTintList(ContextCompat.getColorStateList(requireActivity(), R.color.ms_white));
            holder.amountViewBinding.textView89.setTextColor(getResources().getColor(R.color.ms_black));
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
    }
}