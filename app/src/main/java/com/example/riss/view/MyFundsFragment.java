package com.example.riss.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.riss.databinding.FragmentCreateFundBinding;
import com.example.riss.databinding.FragmentCreatedFundStatusBinding;

public class MyFundsFragment extends Fragment {


    FragmentCreatedFundStatusBinding createdFundStatusBinding;
    NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        createdFundStatusBinding = FragmentCreatedFundStatusBinding.inflate(getLayoutInflater());
        return createdFundStatusBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

    }
}