package com.example.riss.view;

import android.annotation.SuppressLint;
import android.content.Context;
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
    Context context;

    public PieStatsAdapter(List<StatsModel> statsModels, Context context) {
        this.statsModels = statsModels;
        this.context = context;
    }

    @NonNull
    @Override
    public PieVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        PieChartLegendsViewBinding viewBinding = PieChartLegendsViewBinding.inflate(layoutInflater, parent, false);
        return new PieVH(viewBinding);
    }

    @SuppressLint("UseCompatLoadingForColorStateLists")
    @Override
    public void onBindViewHolder(@NonNull PieVH holder, int position) {

        try {
            StatsModel statsModel = statsModels.get(position);
            holder.viewBinding.setStats(statsModel);
            holder.viewBinding.linearLayout4.setBackgroundTintList(context.getResources().getColorStateList(statsModel.getColorCode()));

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
