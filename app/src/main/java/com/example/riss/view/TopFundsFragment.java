package com.example.riss.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.riss.Adapters.TopFundsAdapter;
import com.example.riss.FundsModel;
import com.example.riss.databinding.FragmentTopFundsBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.example.riss.AppUtils.Utils.FUNDS;
import static com.example.riss.AppUtils.Utils.IMAGE;
import static com.example.riss.AppUtils.Utils.LIKE;
import static com.example.riss.AppUtils.Utils.LIKED_IDS;
import static com.example.riss.AppUtils.Utils.NAME;
import static com.example.riss.AppUtils.Utils.SUPPORT;
import static com.example.riss.AppUtils.Utils.getFirestoreReference;
import static com.example.riss.AppUtils.Utils.getRandomNumber;
import static com.example.riss.AppUtils.Utils.getUid;


public class TopFundsFragment extends Fragment {

    FragmentTopFundsBinding fundsBinding;
    List<FundsModel> topFundList = new ArrayList<>();
    List<Boolean> isliked = new ArrayList<>();
    TopFundsAdapter adapter;
    private int selected = 0;

    public static NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fundsBinding = FragmentTopFundsBinding.inflate(inflater, container, false);
        return fundsBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new TopFundsAdapter(topFundList, isliked);
        fundsBinding.rcTopFunds.setAdapter(adapter);

        navController = Navigation.findNavController(view);

        //fundsBinding.ivFilter.setOnClickListener(requireActivity());

        loadFundsData();
    }

    public void loadFundsData() {
        topFundList.clear();
        isliked.clear();
        getFirestoreReference().collection(FUNDS)
                .orderBy(SUPPORT, Query.Direction.DESCENDING)
                // .limit(40)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (null != queryDocumentSnapshots) {
                            for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                                boolean isLikeByUser = false;
                                ArrayList<String> likedIds = new ArrayList<>();
                                if (snapshot.contains(LIKED_IDS) && null != snapshot.get(LIKED_IDS)) {
                                    likedIds.addAll((ArrayList<String>) snapshot.get(LIKED_IDS));
                                    if (likedIds.contains(getUid()))
                                        isLikeByUser = true;
                                }
                                isliked.add(isLikeByUser);
                                topFundList.add(new FundsModel(snapshot.getString(NAME),
                                        "" + snapshot.getLong(SUPPORT),
                                        "" + snapshot.getLong(LIKE),
                                        "" + getRandomNumber(100, 10000),
                                        snapshot.getString(IMAGE),
                                        isLikeByUser,
                                        snapshot.getId(),
                                        likedIds));
                            }

                            fundsBinding.textView10.setText(adapter.getItemCount() + " Fund(s) Found");
                            adapter.notifyDataSetChanged();
                        }
                    }
                });


        Collections.sort(topFundList, new Comparator<FundsModel>() {
            @Override
            public int compare(FundsModel o1, FundsModel o2) {
                return o2.getFundSupport().compareTo(o1.getFundSupport());
            }
        });

    }
}