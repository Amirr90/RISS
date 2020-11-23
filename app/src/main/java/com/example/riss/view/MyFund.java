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
import com.example.riss.adapters.SupportAdapter;
import com.example.riss.databinding.FragmentMyFundBinding;
import com.example.riss.interfaces.IUserProfileInterface;
import com.example.riss.interfaces.OnClickListener;
import com.firebase.ui.auth.data.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static com.example.riss.AppUtils.Utils.FUNDS;
import static com.example.riss.AppUtils.Utils.FundSupport;
import static com.example.riss.AppUtils.Utils.KEY_FUND_ID;
import static com.example.riss.AppUtils.Utils.LIKED_IDS;
import static com.example.riss.AppUtils.Utils.TIMESTAMP;
import static com.example.riss.AppUtils.Utils.TotalFundAmount;
import static com.example.riss.AppUtils.Utils.checkUserProfile;
import static com.example.riss.AppUtils.Utils.getCountInRomanFormat;
import static com.example.riss.AppUtils.Utils.getCurrencyFormat;
import static com.example.riss.AppUtils.Utils.getFirestoreReference;
import static com.example.riss.AppUtils.Utils.hideAlertDialog;
import static com.example.riss.AppUtils.Utils.showAlertDialog;
import static com.example.riss.AppUtils.Utils.support;
import static com.example.riss.AppUtils.Utils.totalInvested;


public class MyFund extends Fragment implements OnClickListener {
    private static final String TAG = "MyFund";

    FragmentMyFundBinding myFundBinding;
    NavController navController;
    String fundId;

    SupportAdapter supportAdapter;
    List<DocumentSnapshot> supportFundsList;


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
        fundId = getArguments().getString(KEY_FUND_ID);


        supportFundsList = new ArrayList<>();
        supportAdapter = new SupportAdapter(supportFundsList, this);
        myFundBinding.supporterRec.setAdapter(supportAdapter);

        setFund();

    }

    private void setFund() {
        showAlertDialog(requireActivity());
        getFirestoreReference().collection(FUNDS).document(fundId).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (null == documentSnapshot && !documentSnapshot.exists()) {
                            Log.d(TAG, "onFailure: document not found");
                            return;
                        }

                        try {

                            setSupportRecData(documentSnapshot);
                            myFundBinding.setFund(documentSnapshot);
                            myFundBinding.textView53.setText(getCurrencyFormat(documentSnapshot.getLong(totalInvested)));

                            List<String> list = (List<String>) documentSnapshot.get(LIKED_IDS);
                            myFundBinding.tvLikes.setText(getCountInRomanFormat(list.size()));

                            myFundBinding.tvMember.setText(getCountInRomanFormat(documentSnapshot.getLong(support)));

                        } catch (Exception e) {
                            hideAlertDialog();
                            e.printStackTrace();
                            Log.d(TAG, "onSuccess: in try catch " + e.getLocalizedMessage());
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                hideAlertDialog();
                Toast.makeText(requireActivity(), "try again", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: " + e.getLocalizedMessage());
            }
        });
    }

    private void setSupportRecData(DocumentSnapshot documentSnapshot) {
        documentSnapshot.getReference().collection(FundSupport)
                .limit(10)
                .orderBy(TIMESTAMP, Query.Direction.DESCENDING)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                hideAlertDialog();
                for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                    Log.d(TAG, "Document ID : " + snapshot.getId());
                    Log.d(TAG, "Amount : " + snapshot.getLong("amount"));
                }
                supportFundsList.clear();
                supportFundsList.addAll(queryDocumentSnapshots.getDocuments());
                supportAdapter.notifyDataSetChanged();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                hideAlertDialog();
                Log.d(TAG, "onFailure: " + e.getLocalizedMessage());
            }
        });
    }

    @Override
    public void onItemClick(int position, String id) {

    }
}