package com.example.riss.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.riss.HomeScreen;
import com.example.riss.R;
import com.example.riss.databinding.FragmentMedicineDistributorListBinding;
import com.example.riss.databinding.FragmentMedicineRequestStatusBinding;
import com.google.firebase.firestore.DocumentSnapshot;

import static com.example.riss.AppUtils.Utils.USER_NAME;


public class MedicineRequestStatusFragment extends Fragment {

    private static final String TAG = "MedicineRequestStatusFr";

    FragmentMedicineRequestStatusBinding requestStatusBinding;

    NavController navController;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        requestStatusBinding = FragmentMedicineRequestStatusBinding.inflate(getLayoutInflater());
        return requestStatusBinding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        requestStatusBinding.btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeScreen.getInstance().onSupportNavigateUp();
            }
        });

        try {
            DocumentSnapshot snapshot = HomeFragment.getUser();

            requestStatusBinding.setTitle(snapshot.getString(USER_NAME));
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "onViewCreated: "+e.getLocalizedMessage());
            HomeScreen.getInstance().onSupportNavigateUp();
        }


    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
    }

    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
    }
}