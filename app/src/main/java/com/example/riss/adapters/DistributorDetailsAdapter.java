package com.example.riss.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.riss.AdapterInterface;
import com.example.riss.AppUtils.Utils;
import com.example.riss.databinding.DistributionViewBinding;
import com.google.firebase.firestore.DocumentSnapshot;

import org.w3c.dom.Document;

import java.util.List;

import static com.example.riss.AppUtils.Utils.TIMESTAMP;

public class DistributorDetailsAdapter extends RecyclerView.Adapter<DistributorDetailsAdapter.DistributorVH> {

    AdapterInterface adapterInterface;
    List<DocumentSnapshot> snapshots;
    Utils utils;

    public DistributorDetailsAdapter(AdapterInterface adapterInterface, List<DocumentSnapshot> snapshots) {
        this.adapterInterface = adapterInterface;
        this.snapshots = snapshots;
        utils = new Utils();
    }

    @NonNull
    @Override
    public DistributorDetailsAdapter.DistributorVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        DistributionViewBinding distributorsViewBinding = DistributionViewBinding.inflate(layoutInflater, parent
                , false);
        distributorsViewBinding.setAdapterInterface(adapterInterface);
        return new DistributorVH(distributorsViewBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull DistributorDetailsAdapter.DistributorVH holder, int position) {

        try {
            DocumentSnapshot snapshot = snapshots.get(position);
            holder.distributorsViewBinding.setSnapshots(snapshot);
            holder.distributorsViewBinding.timestamp.setText(utils.getTimeAgo(snapshot.getLong(TIMESTAMP)));
            holder.distributorsViewBinding.ivIsUser.setVisibility(position % 2 == 0 ? View.GONE : View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return snapshots.size();
    }

    public class DistributorVH extends RecyclerView.ViewHolder {
        DistributionViewBinding distributorsViewBinding;

        public DistributorVH(DistributionViewBinding distributorsViewBinding) {
            super(distributorsViewBinding.getRoot());
            this.distributorsViewBinding = distributorsViewBinding;
        }
    }
}
