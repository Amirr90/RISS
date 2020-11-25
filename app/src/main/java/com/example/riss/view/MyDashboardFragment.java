package com.example.riss.view;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.riss.R;
import com.example.riss.databinding.DashboardStatsViewBinding;
import com.example.riss.databinding.FragmentMyDashboardBinding;
import com.example.riss.models.StatsModel;

import org.eazegraph.lib.charts.StackedBarChart;
import org.eazegraph.lib.models.BarModel;
import org.eazegraph.lib.models.PieModel;
import org.eazegraph.lib.models.StackedBarModel;

import java.util.ArrayList;
import java.util.List;


public class MyDashboardFragment extends Fragment {

    FragmentMyDashboardBinding dashboardBinding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        dashboardBinding = FragmentMyDashboardBinding.inflate(getLayoutInflater());
        return dashboardBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setGraph();

        setStatsRec();
    }

    private void setStatsRec() {
        List<StatsModel> statsModels = new ArrayList<>();
        dashboardBinding.recStats.setAdapter(new StatsAdapter(statsModels));
    }

    private void setGraph() {
        dashboardBinding.piechart.addPieSlice(new PieModel("Freetime", 15, Color.parseColor("#FE6DA8")));
        dashboardBinding.piechart.addPieSlice(new PieModel("Sleep", 25, Color.parseColor("#56B7F1")));
        dashboardBinding.piechart.addPieSlice(new PieModel("Work", 35, Color.parseColor("#CDA67F")));
        dashboardBinding.piechart.addPieSlice(new PieModel("Eating", 40, Color.parseColor("#FED70E")));
        dashboardBinding.piechart.startAnimation();

        setPieChartLegendsRec();
    }

    private void setPieChartLegendsRec() {

        List<StatsModel> statsModels = new ArrayList<>();
        dashboardBinding.recPieStats.setAdapter(new PieStatsAdapter(statsModels));
    }


    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
    }

    private class StatsAdapter extends RecyclerView.Adapter<StatsAdapter.StatsVH> {
        List<StatsModel> statsModels;

        public StatsAdapter(List<StatsModel> statsModels) {
            this.statsModels = statsModels;
        }

        @NonNull
        @Override
        public StatsAdapter.StatsVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            DashboardStatsViewBinding viewBinding = DashboardStatsViewBinding.inflate(layoutInflater, parent, false);
            return new StatsVH(viewBinding);
        }

        @Override
        public void onBindViewHolder(@NonNull StatsAdapter.StatsVH holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 4;
        }

        public class StatsVH extends RecyclerView.ViewHolder {
            DashboardStatsViewBinding viewBinding;

            public StatsVH(DashboardStatsViewBinding viewBinding) {
                super(viewBinding.getRoot());
                this.viewBinding = viewBinding;
            }
        }
    }
}