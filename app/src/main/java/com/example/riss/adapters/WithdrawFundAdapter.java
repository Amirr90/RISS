package com.example.riss.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.riss.databinding.FundsWithdrawHistoryViewBinding;
import com.example.riss.models.Fund;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firestore.v1.Document;

import java.util.List;

import static com.example.riss.AppUtils.Utils.getCurrencyFormat;
import static com.example.riss.AppUtils.Utils.getTimeAgo;

public class WithdrawFundAdapter extends RecyclerView.Adapter<WithdrawFundAdapter.WithdrawVH> {

    List<DocumentSnapshot> snapshots;

    public WithdrawFundAdapter(List<DocumentSnapshot> snapshots) {
        this.snapshots = snapshots;
    }

    @NonNull
    @Override
    public WithdrawVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        FundsWithdrawHistoryViewBinding binding = FundsWithdrawHistoryViewBinding.inflate(inflater, parent, false);
        return new WithdrawVH(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull WithdrawVH holder, int position) {

        DocumentSnapshot snapshot = snapshots.get(position);
        try {
            holder.binding.setWithdrawFund(snapshot);
            holder.binding.tvAmount.setText(getCurrencyFormat(snapshot.getString("amountToWithdraw")));
            holder.binding.tvTimestamp.setText(getTimeAgo(snapshot.getLong("timestamp")));

        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return snapshots.size();
    }

    public class WithdrawVH extends RecyclerView.ViewHolder {
        FundsWithdrawHistoryViewBinding binding;

        public WithdrawVH(@NonNull FundsWithdrawHistoryViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
