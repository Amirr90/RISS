package com.example.riss.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.riss.R;
import com.example.riss.databinding.FragmentMyFundBinding;
import com.firebase.ui.auth.data.model.User;


public class MyFund extends Fragment {


    FragmentMyFundBinding myFundBinding;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myFundBinding = FragmentMyFundBinding.inflate(getLayoutInflater());
        return myFundBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);




    }
}