package com.example.riss.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.riss.AdapterInterface;
import com.example.riss.R;
import com.example.riss.databinding.FragmentFundBinding;
import com.example.riss.databinding.OtherFundsViewBinding;
import com.example.riss.models.FundsModel;
import com.example.riss.viewModel.AppViewModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static com.example.riss.AppUtils.Utils.FUNDS;
import static com.example.riss.AppUtils.Utils.KEY_FUND_ID;
import static com.example.riss.AppUtils.Utils.LIKED_IDS;
import static com.example.riss.AppUtils.Utils.TIMESTAMP;
import static com.example.riss.AppUtils.Utils.getCountInRomanFormat;
import static com.example.riss.AppUtils.Utils.getCurrencyFormat;
import static com.example.riss.AppUtils.Utils.getFirestoreReference;
import static com.example.riss.AppUtils.Utils.getRandomNumber;
import static com.example.riss.AppUtils.Utils.getUid;
import static com.example.riss.AppUtils.Utils.uid;


public class FundFragment extends Fragment implements AdapterInterface {


    FragmentFundBinding fundBinding;
    NavController navController;
    OtherFundsAdapter otherFundsAdapter;
    AppViewModel appViewModel;
    List<DocumentSnapshot> fundsModels;

    String fundID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fundBinding = FragmentFundBinding.inflate(inflater, container, false);
        return fundBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        fundID = getArguments().getString(KEY_FUND_ID);

        fundBinding.btnSupportFund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_fundFragment_to_supportFundFragment);
            }
        });


        fundsModels = new ArrayList<>();
        otherFundsAdapter = new OtherFundsAdapter(fundsModels);

        appViewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);

        fundBinding.otherFundRec.setAdapter(otherFundsAdapter);
        loadData();

        appViewModel.getFundById(fundID).observe(getViewLifecycleOwner(), new Observer<DocumentSnapshot>() {
            @Override
            public void onChanged(DocumentSnapshot fundsModel) {
                fundBinding.setFund(fundsModel);

                try {

                    String initialValue = getCurrencyFormat(fundsModel.getLong("initialValue"));
                    String totalValue = getCurrencyFormat(fundsModel.getLong("totalInvested"));
                    fundBinding.textView22.setText(initialValue + "-" + totalValue);
                    List<String> list = (List<String>) fundsModel.get(LIKED_IDS);

                    fundBinding.tvLikes.setText(getCountInRomanFormat(list.size()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }

    private void loadData() {

        getFirestoreReference().collection(FUNDS).whereEqualTo(uid, getUid())
                .orderBy(TIMESTAMP, Query.Direction.DESCENDING)
                .limit(3)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (null != queryDocumentSnapshots) {
                            fundsModels.clear();
                            fundsModels.addAll(queryDocumentSnapshots.getDocuments());
                            otherFundsAdapter.notifyDataSetChanged();

                        }
                    }
                });
    }

    @Override
    public void onItemClicked(Object o) {

    }

    public class OtherFundsAdapter extends RecyclerView.Adapter<OtherFundsAdapter.FundsVH> {
        List<DocumentSnapshot> fundsModelList;

        public OtherFundsAdapter(List<DocumentSnapshot> fundsModelList) {
            this.fundsModelList = fundsModelList;
        }

        @NonNull
        @Override
        public OtherFundsAdapter.FundsVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            OtherFundsViewBinding otherFundsViewBinding = OtherFundsViewBinding.inflate(inflater, parent, false);
            return new FundsVH(otherFundsViewBinding);
        }

        @Override
        public void onBindViewHolder(@NonNull OtherFundsAdapter.FundsVH holder, int position) {

        }

        @Override
        public int getItemCount() {
            return fundsModelList.size();
        }

        public class FundsVH extends RecyclerView.ViewHolder {
            OtherFundsViewBinding otherFundsViewBinding;

            public FundsVH(OtherFundsViewBinding otherFundsViewBinding) {
                super(otherFundsViewBinding.getRoot());
                this.otherFundsViewBinding = otherFundsViewBinding;
            }
        }
    }
}