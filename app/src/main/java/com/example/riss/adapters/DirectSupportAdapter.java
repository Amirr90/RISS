package com.example.riss.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.riss.databinding.DirectSupportViewBinding;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

import static com.example.riss.AppUtils.Utils.TIMESTAMP;
import static com.example.riss.AppUtils.Utils.getCurrencyFormat;
import static com.example.riss.AppUtils.Utils.getDateInDMY;

public class DirectSupportAdapter extends RecyclerView.Adapter<DirectSupportAdapter.DirectVH> {
    private static final String TAG = "DirectSupportAdapter";
    List<DocumentSnapshot> snapshots;

    public DirectSupportAdapter(List<DocumentSnapshot> snapshots) {
        this.snapshots = snapshots;
    }

    @NonNull
    @Override
    public DirectSupportAdapter.DirectVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        DirectSupportViewBinding binding = DirectSupportViewBinding.inflate(inflater, parent, false);
        return new DirectVH(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DirectSupportAdapter.DirectVH holder, int position) {

        DocumentSnapshot snapshot = snapshots.get(position);
        try {
            holder.binding.setDirect(snapshot);
            holder.binding.tvFundAmount.setText(getCurrencyFormat(snapshot.getString("amount")));
            holder.binding.tvDate.setText(getDateInDMY(snapshot.getLong(TIMESTAMP)));
            Log.d(TAG, "onBindViewHolder: " + snapshot);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "onBindViewHolder: " + e.getLocalizedMessage());
        }
    }

    @Override
    public int getItemCount() {
        return snapshots.size();
    }

    public class DirectVH extends RecyclerView.ViewHolder {
        DirectSupportViewBinding binding;

        public DirectVH(@NonNull DirectSupportViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
