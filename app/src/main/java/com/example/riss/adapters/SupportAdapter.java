package com.example.riss.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.riss.databinding.RecentSupportViewBinding;
import com.example.riss.interfaces.OnClickListener;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

import static com.example.riss.AppUtils.Utils.getCurrencyFormat;
import static com.example.riss.AppUtils.Utils.getTimeAgo;
import static com.example.riss.AppUtils.Utils.getUid;

public class SupportAdapter extends RecyclerView.Adapter<SupportAdapter.SupportVH> {
    private static final String TAG = "SupportAdapter";
    List<DocumentSnapshot> snapshots;
    OnClickListener onClickListener;
    String uid;


    public SupportAdapter(List<DocumentSnapshot> snapshots, OnClickListener onClickListener) {
        this.snapshots = snapshots;
        this.onClickListener = onClickListener;
        uid = getUid();

    }

    @NonNull
    @Override
    public SupportAdapter.SupportVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        RecentSupportViewBinding recentSupportViewBinding = RecentSupportViewBinding.inflate(layoutInflater, parent, false);
        recentSupportViewBinding.setListener(onClickListener);
        return new SupportVH(recentSupportViewBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull SupportAdapter.SupportVH holder, int position) {


        DocumentSnapshot snapshot = snapshots.get(position);
        try {
            String id = snapshot.getString("uid");
            holder.recentSupportViewBinding.setFunds(snapshot);
            holder.recentSupportViewBinding.textView55.setText(id.equals(uid) ? "Self" : snapshot.getString("userName"));
            holder.recentSupportViewBinding.tvAmount.setText("+ " + getCurrencyFormat(snapshot.getLong("amount")));
            holder.recentSupportViewBinding.textView56.setText(getTimeAgo(snapshot.getLong("timestamp")));
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "OnSupportAdapterClick : " + e.getLocalizedMessage());
        }
    }

    @Override
    public int getItemCount() {
        return snapshots.size();
    }

    public class SupportVH extends RecyclerView.ViewHolder {
        RecentSupportViewBinding recentSupportViewBinding;

        public SupportVH(RecentSupportViewBinding recentSupportViewBinding) {
            super(recentSupportViewBinding.getRoot());
            this.recentSupportViewBinding = recentSupportViewBinding;
        }
    }
}
