package com.example.riss.view;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.riss.R;
import com.example.riss.databinding.FragmentCreateFundBinding;
import com.example.riss.databinding.LinkAadharDialogViewBinding;


public class CreateFundFragment extends Fragment {


    AlertDialog optionDialog;
    FragmentCreateFundBinding createFundBinding;
    NavController navController;

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

        createFundBinding.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLinkAadharDialog();
            }
        });
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