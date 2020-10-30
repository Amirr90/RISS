package com.example.riss.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.riss.CreateFundScreen;
import com.example.riss.HomeScreen;
import com.example.riss.databinding.FragmentFundsBinding;


public class FundsFragment extends Fragment {

    FragmentFundsBinding fundsBinding;
    NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fundsBinding = FragmentFundsBinding.inflate(inflater, container, false);
        return fundsBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);


        fundsBinding.btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSupportFundDialog(HomeScreen.getInstance());
            }
        });

    }


    public void showSupportFundDialog(final Activity activity) {
        final CharSequence[] items = {"Direct Support", "Support By Creating A fund"};
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Make your selection");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                dialog.dismiss();
                if (item == 0) {
                    Toast.makeText(activity, "Land To payment Page", Toast.LENGTH_SHORT).show();
                } else {
                    activity.startActivity(new Intent(activity, CreateFundScreen.class));
                }

            }
        }).show();
    }

}