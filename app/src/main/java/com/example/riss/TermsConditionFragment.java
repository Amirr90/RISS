package com.example.riss;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.riss.databinding.FragmentTermsConditionBinding;
import com.example.riss.databinding.WithdrawalViewBinding;
import com.example.riss.models.TermsConditionModel;

import java.util.ArrayList;
import java.util.List;


public class TermsConditionFragment extends Fragment {

    FragmentTermsConditionBinding termsConditionBinding;
    NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        termsConditionBinding = FragmentTermsConditionBinding.inflate(getLayoutInflater());
        return termsConditionBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);


        termsConditionBinding.recView.setAdapter(new DataAdapter(getData()));

    }

    private List<TermsConditionModel> getData() {

        List<TermsConditionModel> models = new ArrayList<>();

        models.add(new TermsConditionModel("A", "10%", "2.50%"));
        models.add(new TermsConditionModel("B", "20%", "3.50%"));
        models.add(new TermsConditionModel("C", "30%", "5.00%"));
        models.add(new TermsConditionModel("D", "40%", "7.00%"));
        models.add(new TermsConditionModel("E", "50%", "10.00%"));
        models.add(new TermsConditionModel("F", "75%", "15.00%"));
        models.add(new TermsConditionModel("G", "100%", "20.00%"));
        return models;
    }

    private class DataAdapter extends RecyclerView.Adapter<DataAdapter.DataVH> {
        List<TermsConditionModel> models;

        public DataAdapter(List<TermsConditionModel> models) {
            this.models = models;
        }

        @NonNull
        @Override
        public DataVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            WithdrawalViewBinding binding = WithdrawalViewBinding.inflate(inflater, parent, false);
            return new DataVH(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull DataVH holder, int position) {

            try {
                TermsConditionModel termsConditionModel = models.get(position);
                holder.binding.setTermsCondition(termsConditionModel);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return models.size();
        }

        public class DataVH extends RecyclerView.ViewHolder {
            WithdrawalViewBinding binding;

            public DataVH(@NonNull WithdrawalViewBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
        }
    }
}