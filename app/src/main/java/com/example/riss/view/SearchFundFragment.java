package com.example.riss.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.example.riss.AppUtils.Utils;
import com.example.riss.R;
import com.example.riss.adapters.TopFundsAdapter;
import com.example.riss.databinding.FragmentSearchFundBinding;
import com.example.riss.databinding.FragmentTopFundsBinding;
import com.example.riss.models.Fund;
import com.example.riss.viewModel.AppViewModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import static com.example.riss.AppUtils.Utils.fundName;

public class SearchFundFragment extends Fragment implements AdapterInterface {


    FragmentSearchFundBinding searchFundBinding;
    NavController navController;

    AppViewModel appViewModel;
    TopFundsAdapter topFundsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        searchFundBinding = FragmentSearchFundBinding.inflate(getLayoutInflater());
        return searchFundBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);


        appViewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);

        topFundsAdapter = new TopFundsAdapter(this);

        searchFundBinding.searchFundsRec.setAdapter(topFundsAdapter);

        searchFundBinding.editTextTextSearchFund.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                if (null != s.toString()) {
                    SearchFund(s.toString());
                }
            }
        });

    }

    private void SearchFund(String text) {
        appViewModel.getTopFundById(text, requireActivity()).observe(getViewLifecycleOwner(), new Observer<List<Fund>>() {
            @Override
            public void onChanged(List<Fund> funds) {
                topFundsAdapter.submitList(funds);

            }
        });


    }

    @Override
    public void onItemClicked(Object o) {

    }
}