package com.example.riss.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.riss.AdapterInterface;
import com.example.riss.R;
import com.example.riss.adapters.TopFundsAdapter;
import com.example.riss.databinding.FragmentTopFundsBinding;
import com.example.riss.models.FundsModel;
import com.example.riss.viewModel.AppViewModel;

import java.util.List;


public class TopFundsFragment extends Fragment implements AdapterInterface {


    FragmentTopFundsBinding topFundsBinding;
    AppViewModel appViewModel;
    TopFundsAdapter topFundsAdapter;
    NavController navController;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        topFundsBinding = FragmentTopFundsBinding.inflate(getLayoutInflater());
        return topFundsBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        appViewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);

        topFundsAdapter = new TopFundsAdapter(this);

        topFundsBinding.topFundsRec.setAdapter(topFundsAdapter);

        appViewModel.getTopFundsData().observe(requireActivity(), new Observer<List<FundsModel>>() {
            @Override
            public void onChanged(List<FundsModel> fundsModels) {
                topFundsAdapter.submitList(fundsModels);
            }
        });


    }

    @Override
    public void onItemClicked(Object o) {
        navController.navigate(R.id.action_topFundsFragment_to_fundFragment);
    }
}