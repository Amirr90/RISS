package com.example.riss.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.riss.HomeScreen;
import com.example.riss.databinding.FragmentCreateFundBinding;

public class CreateFundFragment extends Fragment {

    FragmentCreateFundBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCreateFundBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.etSelectAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] items = {
                        "Rs 100 for 3 months",
                        "Rs 500 for 6 months",
                        "Rs 1,000 for 9 months",
                        "Rs 2,500 for 12 months",
                        "Rs 5,000 for 15 months",
                        "Rs 10,00 for 18 months",
                        "Rs 25,000 for 21 months",
                        "Rs 50,000 for 24 months",
                        "Rs 1,00,000 for 30 months",
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(HomeScreen.getInstance());
                builder.setTitle("Make your selection");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        dialog.dismiss();
                        // binding.etFundPeriod.setText(getFundPeriod(item));
                        binding.etSelectAmount.setText(items[item]);
                    }
                }).show();
            }
        });
    }

}