package com.example.riss.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.riss.AdapterInterface;
import com.example.riss.databinding.TopFundsViewBinding;
import com.example.riss.models.FundsModel;

public class TopFundsAdapter extends ListAdapter<FundsModel, TopFundsAdapter.FundsVH> {
    AdapterInterface adapterInterface;

    public TopFundsAdapter(AdapterInterface adapterInterface) {
        super(FundsModel.itemCallback);
        this.adapterInterface = adapterInterface;

    }

    @NonNull
    @Override
    public TopFundsAdapter.FundsVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        TopFundsViewBinding fundsViewBinding = TopFundsViewBinding.inflate(inflater, parent, false);
        return new FundsVH(fundsViewBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull TopFundsAdapter.FundsVH holder, int position) {

        final FundsModel fundsModel = getItem(position);
        holder.fundsViewBinding.rlMainFundsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterInterface.onItemClicked(fundsModel);
            }
        });


    }

    public class FundsVH extends RecyclerView.ViewHolder {
        TopFundsViewBinding fundsViewBinding;

        public FundsVH(TopFundsViewBinding fundsViewBinding) {
            super(fundsViewBinding.getRoot());
            this.fundsViewBinding = fundsViewBinding;
        }
    }
}
