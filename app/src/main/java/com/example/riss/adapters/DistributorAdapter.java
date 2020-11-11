package com.example.riss.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.riss.AdapterInterface;
import com.example.riss.databinding.DistributorsViewBinding;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.model.DocumentSet;

import org.w3c.dom.Document;

import java.util.List;

public class DistributorAdapter extends RecyclerView.Adapter<DistributorAdapter.DistributorVH> {
    List<DocumentSnapshot> distributorsList;
    AdapterInterface adapterInterface;

    public DistributorAdapter(List<DocumentSnapshot> distributorsList, AdapterInterface adapterInterface) {
        this.distributorsList = distributorsList;
        this.adapterInterface = adapterInterface;
    }

    @NonNull
    @Override
    public DistributorAdapter.DistributorVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        DistributorsViewBinding distributorsViewBinding = DistributorsViewBinding.inflate(inflater, parent, false);
        return new DistributorVH(distributorsViewBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull DistributorAdapter.DistributorVH holder, int position) {

        try {
            DocumentSnapshot distributorModel = distributorsList.get(position);
            holder.distributorsViewBinding.setDistributorModel(distributorModel);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return distributorsList.size();
    }

    public class DistributorVH extends RecyclerView.ViewHolder {
        DistributorsViewBinding distributorsViewBinding;

        public DistributorVH(DistributorsViewBinding distributorsViewBinding) {
            super(distributorsViewBinding.getRoot());
            this.distributorsViewBinding = distributorsViewBinding;
        }
    }
}
