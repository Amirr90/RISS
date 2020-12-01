package com.example.riss.view;

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

import com.example.riss.R;
import com.example.riss.databinding.FragmentWithdrawFundAmountBinding;
import com.example.riss.databinding.WithdrawalAmountViewBinding;

import java.util.ArrayList;
import java.util.List;


public class WithdrawFundAmountFragment extends Fragment {

    FragmentWithdrawFundAmountBinding amountBinding;
    NavController navController;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        amountBinding = FragmentWithdrawFundAmountBinding.inflate(getLayoutInflater());
        return amountBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        amountBinding.recData.setAdapter(new DataAdapter(getData()));
    }

    private List<String> getData() {
        List<String> strings = new ArrayList<>();
        strings.add("10%");
        strings.add("20%");
        strings.add("30%");
        strings.add("40%");
        strings.add("50%");
        strings.add("70%");
        strings.add("100%");

        return strings;
    }

    private class DataAdapter extends RecyclerView.Adapter<DataAdapter.DataVH> {
        List<String> list;

        int selectedPosition = 0;


        public DataAdapter(List<String> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public DataVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            WithdrawalAmountViewBinding amountViewBinding = WithdrawalAmountViewBinding.inflate(inflater, parent, false);
            return new DataVH(amountViewBinding);

        }

        @Override
        public void onBindViewHolder(@NonNull final DataVH holder, final int position) {
            try {

                holder.amountViewBinding.textView89.setText(list.get(position));
            } catch (Exception e) {
                e.printStackTrace();
            }

            holder.amountViewBinding.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedPosition = position;
                    notifyDataSetChanged();

                }
            });

            if (selectedPosition == position) {
                changeColor(true, holder);
            } else {
                changeColor(false, holder);
            }
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class DataVH extends RecyclerView.ViewHolder {
            WithdrawalAmountViewBinding amountViewBinding;

            public DataVH(@NonNull WithdrawalAmountViewBinding amountViewBinding) {
                super(amountViewBinding.getRoot());
                this.amountViewBinding = amountViewBinding;
            }
        }
    }

    private void changeColor(Boolean selected, DataAdapter.DataVH holder) {
        if (selected) {
            holder.amountViewBinding.textView89.setTextColor(getResources().getColor(R.color.ms_white));
            holder.amountViewBinding.cardView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        } else {
            holder.amountViewBinding.cardView.setBackgroundColor(getResources().getColor(R.color.ms_white));
            holder.amountViewBinding.textView89.setTextColor(getResources().getColor(R.color.ms_black));
        }
    }
}