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

import java.util.ArrayList;
import java.util.List;


public class FundFragment extends Fragment implements AdapterInterface {


    FragmentFundBinding fundBinding;
    NavController navController;
    OtherFundsAdapter otherFundsAdapter;
    AppViewModel appViewModel;
    List<FundsModel> fundsModels;

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


        String fundId = "";
        appViewModel.getFundById(fundId).observe(getViewLifecycleOwner(), new Observer<FundsModel>() {
            @Override
            public void onChanged(FundsModel fundsModel) {

            }
        });


        String userId = "";


    }

    private void loadData() {

        for (int a = 0; a < 5; a++) {
            FundsModel fundsModel = new FundsModel();
            fundsModel.setFundName("Fund name");
            fundsModel.setFundImage("Fund name");
            fundsModel.setCreatedBy("Fund name");
            fundsModel.setLikes("Fund name");
            fundsModel.setTotalInvested("Fund name");
            fundsModel.setCurrentValue("Fund name");
            fundsModels.add(fundsModel);
        }
        otherFundsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClicked(Object o) {

    }

    private class OtherFundsAdapter extends RecyclerView.Adapter<OtherFundsAdapter.FundsVH> {
        List<FundsModel> fundsModelList;

        public OtherFundsAdapter(List<FundsModel> fundsModelList) {
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
            }
        }
    }
}