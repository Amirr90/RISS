package com.example.riss.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.riss.AppUtils.ApiCall;
import com.example.riss.HomeScreen;
import com.example.riss.R;
import com.example.riss.databinding.FragmentAddDistributionBinding;
import com.example.riss.interfaces.ApiCallbackInterface;
import com.example.riss.models.FundsModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.riss.AppUtils.ApiCall.distributeMedicine;
import static com.example.riss.AppUtils.Utils.FUNDS;
import static com.example.riss.AppUtils.Utils.FUND_ID;
import static com.example.riss.AppUtils.Utils.getFirestoreReference;
import static com.example.riss.AppUtils.Utils.getUid;
import static com.example.riss.AppUtils.Utils.hideAlertDialog;
import static com.example.riss.AppUtils.Utils.hideKeyboard;
import static com.example.riss.AppUtils.Utils.showAlertDialog;


public class AddDistributionFragment extends Fragment {
    private static final String TAG = "AddDistributionFragment";
    private static final String TEXT_SELECT_FUND = "-select fund-";

    FragmentAddDistributionBinding addDistributionBinding;

    NavController navController;

    String name, mobile, address, otp;
    List<String> fundNameList = new ArrayList<>();
    List<String> fundIdsList = new ArrayList<>();

    String selectedFundName = null;
    String selectedFundId = null;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        addDistributionBinding = FragmentAddDistributionBinding.inflate(getLayoutInflater());
        return addDistributionBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        addDistributionBinding.btnDistributeMedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (allFieldFilled()) {
                    checkMedicineStatus();
                }
            }
        });


        setSpinnerData();

        addDistributionBinding.etMobileToDis.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (null != charSequence) {
                    try {
                        int length = charSequence.length();
                        if (length == 10) {
                            addDistributionBinding.btnGetOTP.setEnabled(true);
                        } else addDistributionBinding.btnGetOTP.setEnabled(false);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        addDistributionBinding.btnGetOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = addDistributionBinding.etMobileToDis.getText().toString().trim();
                if (number.length() == 10) {
                    addDistributionBinding.btnGetOTP.setEnabled(false);
                    requestOtp(number);
                }
            }
        });
    }

    private void setSpinnerData() {

        showAlertDialog(requireActivity());
        getFirestoreReference().collection(FUNDS)
                .whereEqualTo("uid", getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        if (queryDocumentSnapshots == null && queryDocumentSnapshots.getDocuments().isEmpty())
                            return;

                        fundIdsList.clear();
                        fundNameList.clear();


                        fundNameList.add(TEXT_SELECT_FUND);
                        for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                            fundNameList.add(snapshot.getString("fundName"));
                            fundIdsList.add(snapshot.getId());
                        }


                        ArrayAdapter<String> branchListAdapter = new ArrayAdapter<>(getActivity(),
                                android.R.layout.simple_spinner_item, fundNameList);
                        branchListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        addDistributionBinding.showFundSpinner.setAdapter(branchListAdapter);


                        addDistributionBinding.showFundSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                                if (position != 0) {
                                    selectedFundName = fundNameList.get(position);
                                    selectedFundId = fundIdsList.get(position - 1);
                                    Toast.makeText(requireActivity(), "selected '" + selectedFundName + "'", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                        hideAlertDialog();

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: " + e.getLocalizedMessage());
                hideAlertDialog();
            }
        });

    }

    private void requestOtp(final String number) {

        showAlertDialog(requireActivity());
        hideKeyboard(requireActivity());
        ApiCall.requestOtp(number, new ApiCallbackInterface() {
            @Override
            public void onSuccess(Object obj) {
                String otp = (String) obj;
                sendOtp(otp);

            }

            @Override
            public void onFailed(String msg) {
                hideAlertDialog();
                Toast.makeText(requireActivity(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startTimer() {

    }

    private void sendOtp(String otp) {

        String number = addDistributionBinding.etMobileToDis.getText().toString();
        ApiCall.sendOtp(otp, number, new ApiCallbackInterface() {
            @Override
            public void onSuccess(Object obj) {
                hideAlertDialog();
                Toast.makeText(requireActivity(), "OTP send successfully to number " + addDistributionBinding.etMobileToDis.getText().toString(), Toast.LENGTH_SHORT).show();
                startTimer();
            }

            @Override
            public void onFailed(String msg) {

                hideAlertDialog();
                Toast.makeText(requireActivity(), "Failed to sent OTP", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkMedicineStatus() {
        hideKeyboard(requireActivity());

        showAlertDialog(requireActivity());
        Map<String, String> map = new HashMap<>();
        map.put("name", name);
        map.put("mobile", mobile);
        map.put("otp", otp);
        map.put("address", address);
        map.put("fundID", selectedFundId);

        distributeMedicine(map, new ApiCallbackInterface() {
            @Override
            public void onSuccess(Object obj) {

                hideAlertDialog();

                Toast.makeText(requireActivity(), "" + (String) obj, Toast.LENGTH_SHORT).show();

                HomeScreen.getInstance().onSupportNavigateUp();

            }

            @Override
            public void onFailed(String msg) {
                hideAlertDialog();
                Toast.makeText(requireActivity(), msg, Toast.LENGTH_SHORT).show();

            }
        });


    }

    private boolean allFieldFilled() {

        name = addDistributionBinding.etName.getText().toString().trim();
        mobile = addDistributionBinding.etMobileToDis.getText().toString().trim();
        otp = addDistributionBinding.etOtp.getText().toString().trim();
        address = addDistributionBinding.etAddress.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(requireActivity(), "name required", Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(mobile)) {
            Toast.makeText(requireActivity(), "mobile number required", Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(otp)) {
            Toast.makeText(requireActivity(), "enter OTP", Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(address)) {
            Toast.makeText(requireActivity(), "address required", Toast.LENGTH_SHORT).show();
            return false;
        } else if (selectedFundName == null && selectedFundId == null) {
            Toast.makeText(requireActivity(), "select Fund name from list", Toast.LENGTH_SHORT).show();
            return false;
        } else
            return true;
    }
}