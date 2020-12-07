package com.example.riss.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;

import com.example.riss.adapters.WithdrawFundAdapter;
import com.example.riss.databinding.FragmentWithdrawFundHistoryBinding;
import com.example.riss.viewModel.AppViewModel;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;


public class WithdrawFundHistoryFragment extends Fragment {

    FragmentWithdrawFundHistoryBinding withdrawFundAmountBinding;
    NavController navController;
    AppViewModel appViewModel;
    WithdrawFundAdapter adapter;
    List<DocumentSnapshot> snapshots;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        withdrawFundAmountBinding = FragmentWithdrawFundHistoryBinding.inflate(getLayoutInflater());
        return withdrawFundAmountBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        appViewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);


        snapshots = new ArrayList<>();
        adapter = new WithdrawFundAdapter(snapshots,requireActivity());

        withdrawFundAmountBinding.recHistoryFund.setAdapter(adapter);

        withdrawFundAmountBinding.recHistoryFund.addItemDecoration(new
                DividerItemDecoration(requireActivity(),
                DividerItemDecoration.VERTICAL));


        appViewModel.getWithdrawFundHistory(requireActivity()).observe(getViewLifecycleOwner(), new Observer<List<DocumentSnapshot>>() {
            @Override
            public void onChanged(List<DocumentSnapshot> documentSnapshots) {
                snapshots.clear();
                snapshots.addAll(documentSnapshots);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
    }
}