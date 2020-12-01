package com.example.riss.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.riss.R;
import com.example.riss.adapters.PublicReviewAdapter;
import com.example.riss.databinding.FragmentPublicReviewsBinding;
import com.example.riss.viewModel.AppViewModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static com.example.riss.AppUtils.Utils.QUERY_PUBLIC_REVIEW;
import static com.example.riss.AppUtils.Utils.TIMESTAMP;
import static com.example.riss.AppUtils.Utils.getFirestoreReference;

public class PublicReviewsFragment extends Fragment {


    FragmentPublicReviewsBinding publicReviewsBinding;

    AppViewModel appViewModel;
    PublicReviewAdapter reviewAdapter;
    NavController navController;
    List<DocumentSnapshot> reviewList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        publicReviewsBinding = FragmentPublicReviewsBinding.inflate(getLayoutInflater());
        return publicReviewsBinding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        appViewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);

        reviewList = new ArrayList<>();
        reviewAdapter = new PublicReviewAdapter(reviewList);
        publicReviewsBinding.publicReviewRec.setAdapter(reviewAdapter);
        loadData();


        publicReviewsBinding.btnProceedToWriteReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_publicReviewsFragment_to_writeReviewFragment);
            }
        });
    }

    private void loadData() {
        getFirestoreReference().collection(QUERY_PUBLIC_REVIEW)
                .limit(20)
                .orderBy(TIMESTAMP, Query.Direction.DESCENDING)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                try {
                    reviewList.clear();
                    reviewList.addAll(queryDocumentSnapshots.getDocuments());
                    reviewAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
    }
}