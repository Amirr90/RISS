package com.example.riss.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.riss.R;
import com.example.riss.databinding.FragmentMyFundBinding;
import com.example.riss.interfaces.IUserProfileInterface;
import com.firebase.ui.auth.data.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

import static com.example.riss.AppUtils.Utils.FUNDS;
import static com.example.riss.AppUtils.Utils.checkUserProfile;
import static com.example.riss.AppUtils.Utils.getFirestoreReference;


public class MyFund extends Fragment {
    private static final String TAG = "MyFund";


    FragmentMyFundBinding myFundBinding;
    NavController navController;
    String fundId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myFundBinding = FragmentMyFundBinding.inflate(getLayoutInflater());
        return myFundBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        fundId = getArguments().getString("id");
        setFund();

    }

    private void setFund() {
        getFirestoreReference().collection(FUNDS).document(fundId).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (null == documentSnapshot && !documentSnapshot.exists()) {

                            Log.d(TAG, "onFailure: document not found");
                            return;
                        }

                        try {
                            myFundBinding.setFund(documentSnapshot);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.d(TAG, "onSuccess: in try catch " + e.getLocalizedMessage());
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(requireActivity(), "try again", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: " + e.getLocalizedMessage());
            }
        });
    }
}