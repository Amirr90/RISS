package com.example.riss.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.riss.HomeScreen;
import com.example.riss.databinding.FragmentSearchFundBinding;

import java.util.Objects;

import br.com.liveo.searchliveo.SearchLiveo;

public class SearchFundFragment extends Fragment implements SearchLiveo.OnSearchListener {


    FragmentSearchFundBinding searchFundBinding;
    NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        searchFundBinding = FragmentSearchFundBinding.inflate(inflater, container, false);
        return searchFundBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        searchFundBinding.searchLiveo.with(getActivity())
                .hideSearch(new SearchLiveo.OnHideSearchListener() {
                    @Override
                    public void hideSearch() {
                        HomeScreen.getInstance().navigateUp();
                    }
                }).build();
        searchFundBinding.searchLiveo.show();
        searchFundBinding.searchLiveo.hideKeyboardAfterSearch();

    }

    @Override
    public void changedSearch(CharSequence charSequence) {

    }

    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).hide();
    }

    @Override
    public void onStop() {
        super.onStop();
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).show();
    }
}