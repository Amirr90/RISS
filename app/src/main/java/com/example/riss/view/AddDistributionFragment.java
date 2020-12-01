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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.riss.AppUtils.ApiCall;
import com.example.riss.HomeScreen;
import com.example.riss.R;
import com.example.riss.databinding.FragmentAddDistributionBinding;
import com.example.riss.interfaces.ApiCallbackInterface;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

import static com.example.riss.AppUtils.ApiCall.distributeMedicine;
import static com.example.riss.AppUtils.Utils.hideAlertDialog;
import static com.example.riss.AppUtils.Utils.hideKeyboard;
import static com.example.riss.AppUtils.Utils.showAlertDialog;


public class AddDistributionFragment extends Fragment {

    FragmentAddDistributionBinding addDistributionBinding;

    NavController navController;

    String name, mobile, address, otp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
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

    private void requestOtp(final String number) {

        ApiCall.requestOtp(number, new ApiCallbackInterface() {
            @Override
            public void onSuccess(Object obj) {
                startTimer();
                Toast.makeText(requireActivity(), "OTP send successfully to number " + number, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailed(String msg) {
                Toast.makeText(requireActivity(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startTimer() {

    }

    private void checkMedicineStatus() {
        hideKeyboard(requireActivity());

        showAlertDialog(requireActivity());
        Map<String, String> map = new HashMap<>();
        map.put("name", name);
        map.put("mobile", mobile);
        map.put("otp", otp);
        map.put("address", address);

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
        } else
            return true;
    }
}