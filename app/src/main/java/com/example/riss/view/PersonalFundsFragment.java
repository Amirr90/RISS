package com.example.riss.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.riss.AdapterInterface;
import com.example.riss.R;
import com.example.riss.adapters.TopFundsAdapter;
import com.example.riss.databinding.FragmentPersonalFundsBinding;
import com.example.riss.models.Fund;
import com.example.riss.viewModel.AppViewModel;

import java.util.List;

import static com.example.riss.AppUtils.Utils.KEY_FUND_ID;

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


        appViewModel.getPersonalFundsData(null, requireActivity()).observe(getViewLifecycleOwner(), new Observer<List<Fund>>() {
            @Override
            public void onChanged(List<Fund> funds) {
                personalFundsAdapter.submitList(funds);

            }
        });

        personalFundsBinding.editTextTextSearchFund.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                if (null != s.toString()) {
                    loadData(s.toString());
                }
            }
        });

    }

    private void loadData(String fundName) {
        personalFundsBinding.MyLoadingLayout.getRoot().setVisibility(View.VISIBLE);
        appViewModel.getPersonalFundsData(fundName, requireActivity()).observe(getViewLifecycleOwner(), new Observer<List<Fund>>() {
            @Override
            public void onChanged(List<Fund> funds) {
                personalFundsAdapter.submitList(funds);
                personalFundsBinding.MyLoadingLayout.getRoot().setVisibility(View.GONE);

            }
        });
    }

    @Override
    public void onItemClicked(Object o) {
        try {
            Fund fund = (Fund) o;
            Bundle bundle = new Bundle();
            bundle.putString(KEY_FUND_ID, fund.getFundId());
            navController.navigate(R.id.action_personalFundsFragment_to_myFund, bundle);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
    }
}