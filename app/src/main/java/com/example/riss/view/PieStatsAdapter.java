package com.example.riss.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.riss.databinding.DashboardStatsViewBinding;
import com.example.riss.databinding.PieChartLegendsViewBinding;
import com.example.riss.models.StatsModel;

import java.util.List;

public class PieStatsAdapter extends RecyclerView.Adapter<PieStatsAdapter.PieVH> {
    List<StatsModel> statsModels;

    public PieStatsAdapter(List<StatsModel> statsModels) {
        this.statsModels = statsModels;
    }

    @NonNull
    @Override
    public PieVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        PieChartLegendsViewBinding viewBinding = PieChartLegendsViewBinding.inflate(layoutInflater, parent, false);
        return new PieVH(viewBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull PieVH holder, int position) {

        try {
            StatsModel statsModel = statsModels.get(position);
            holder.viewBinding.setStats(statsModel);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return statsModels.size();
    }

    public class PieVH extends RecyclerView.ViewHolder {
        PieChartLegendsViewBinding viewBinding;

        public PieVH(PieChartLegendsViewBinding viewBinding) {
            super(viewBinding.getRoot());

            this.viewBinding = viewBinding;
        }
    }
}
