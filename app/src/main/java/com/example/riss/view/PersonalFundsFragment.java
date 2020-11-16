package com.example.riss.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.riss.AdapterInterface;
import com.example.riss.adapters.TopFundsAdapter;
import com.example.riss.databinding.FragmentPersonalFundsBinding;
import com.example.riss.models.Fund;
import com.example.riss.viewModel.AppViewModel;

import java.util.List;

public class PersonalFundsFragment extends Fragment implements AdapterInterface {


    FragmentPersonalFundsBinding personalFundsBinding;
    NavController navController;

    AppViewModel appViewModel;
    TopFundsAdapter personalFundsAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        personalFundsBinding = FragmentPersonalFundsBinding.inflate(getLayoutInflater());
        return personalFundsBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        appViewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);

        personalFundsAdapter = new TopFundsAdapter(this);

        personalFundsBinding.personalFundsRec.setAdapter(personalFundsAdapter);


        appViewModel.getPersonalFundsData(requireActivity()).observe(getViewLifecycleOwner(), new Observer<List<Fund>>() {
            @Override
            public void onChanged(List<Fund> funds) {
                personalFundsAdapter.submitList(funds);

            }
        });


    }

    @Override
    public void onItemClicked(Object o) {

    }
}