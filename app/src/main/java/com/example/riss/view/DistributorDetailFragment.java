package com.example.riss.view;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.riss.R;
import com.example.riss.databinding.FragmentDistributorDetailBinding;
import com.example.riss.databinding.LinkAadharDialogViewBinding;
import com.example.riss.databinding.RequestMedicineDialogViewBinding;


public class DistributorDetailFragment extends Fragment {

    FragmentDistributorDetailBinding distributorDetailBinding;
    NavController navController;
    AlertDialog optionDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        distributorDetailBinding = FragmentDistributorDetailBinding.inflate(getLayoutInflater());
        return distributorDetailBinding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);


        distributorDetailBinding.btnRequestMedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRequestMedicineDialog();
            }
        });
    }

    private void showRequestMedicineDialog() {

        LayoutInflater inflater = (LayoutInflater) requireActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View formElementsView = inflater.inflate(R.layout.request_medicine_dialog_view, null, false);

        RequestMedicineDialogViewBinding genderViewBinding = RequestMedicineDialogViewBinding.bind(formElementsView);


        genderViewBinding.btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                optionDialog.dismiss();
                navController.navigate(R.id.action_createFundFragment_to_profileFragment);

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

}