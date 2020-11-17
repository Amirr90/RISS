package com.example.riss.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.riss.HomeScreen;
import com.example.riss.R;
import com.example.riss.databinding.FragmentRansactionCompleteBinding;
import com.example.riss.databinding.FragmentSupportFundBinding;


public class TransactionCompleteFragment extends Fragment {


    NavController navController;
    FragmentRansactionCompleteBinding transactionBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        transactionBinding = FragmentRansactionCompleteBinding.inflate(getLayoutInflater());
        return transactionBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        transactionBinding.btnGotoHomeScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeScreen.getInstance().onSupportNavigateUp();
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