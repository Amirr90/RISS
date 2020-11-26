package com.example.riss.view;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.riss.R;
import com.example.riss.databinding.DashboardStatsViewBinding;
import com.example.riss.databinding.FragmentMyDashboardBinding;
import com.example.riss.models.DashboardModel;
import com.example.riss.models.Fund;
import com.example.riss.models.FundsModel;
import com.example.riss.models.StatsModel;
import com.example.riss.viewModel.AppViewModel;

import org.eazegraph.lib.charts.StackedBarChart;
import org.eazegraph.lib.models.BarModel;
import org.eazegraph.lib.models.PieModel;
import org.eazegraph.lib.models.StackedBarModel;

import java.util.ArrayList;
import java.util.List;

import static com.example.riss.AppUtils.Utils.getColorList;
import static com.example.riss.AppUtils.Utils.getCountInRomanFormat;
import static com.example.riss.AppUtils.Utils.getCurrencyFormat;


public class MyDashboardFragment extends Fragment {
    private static final String TAG = "MyDashboardFragment";

    FragmentMyDashboardBinding dashboardBinding;
    AppViewModel appViewModel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        dashboardBinding = FragmentMyDashboardBinding.inflate(getLayoutInflater());
        appViewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);
        return dashboardBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        appViewModel.getDashboardData(requireActivity()).observe(getViewLifecycleOwner(), new Observer<DashboardModel>() {
            @Override
            public void onChanged(DashboardModel dashboardModel) {
                Log.d(TAG, "onChanged: " + dashboardModel.toString());

                setGraph(dashboardModel.getFundList());

                setStatsRec(dashboardModel);

                setData(dashboardModel);
            }
        });
    }

    private void setData(DashboardModel dashboardModel) {
        dashboardBinding.textView64.setText(getCurrencyFormat(dashboardModel.getTotalInvestedAmount()));
        dashboardBinding.textView60.setText(getCurrencyFormat(dashboardModel.getEarnedAmount()));
        dashboardBinding.textView65.setText(getCurrencyFormat(dashboardModel.getTotalEarningFromFunds()));
        dashboardBinding.textView74.setText(getCountInRomanFormat(dashboardModel.getFundList().size()));
    }

    private void setStatsRec(DashboardModel dashboardModel) {
        List<Integer> icons = getIcons();
        List<Integer> colorList = getColorList();
        List<StatsModel> statsModels = new ArrayList<>();
        statsModels.add(new StatsModel(icons.get(0), colorList.get(0),
                "Total Funds Created",
                getCountInRomanFormat(dashboardModel.getTotalCreatedFund()),
                "Funds"));

        statsModels.add(new StatsModel(icons.get(1), colorList.get(1),
                "Total Amount Withdrawal",
                "0",
                "Amount"));

        statsModels.add(new StatsModel(icons.get(2), colorList.get(2),
                "Total Medicine Distributed",
                getCountInRomanFormat(dashboardModel.getTotalMedicineDistributed()),
                "Medicine"));

        statsModels.add(new StatsModel(icons.get(3), colorList.get(3),
                "Total Active Funds",
                getCountInRomanFormat(dashboardModel.getActiveFunds()), "Funds"));

        statsModels.add(new StatsModel(icons.get(4), colorList.get(4),
                "Total De-Active Funds",
                getCountInRomanFormat(dashboardModel.getDeactiveFunds()), "Funds"));

        dashboardBinding.recStats.setAdapter(new StatsAdapter(statsModels));
    }

    private List<Integer> getIcons() {
        List<Integer> icons = new ArrayList<>();
        icons.add(R.drawable.funds);
        icons.add(R.drawable.withdrawal);
        icons.add(R.drawable.pills_small_icons);
        icons.add(R.drawable.funds);
        icons.add(R.drawable.funds);

        return icons;
    }

    private void setGraph(List<FundsModel> fundList) {
        List<Integer> colorList = getColorList();
        Log.d(TAG, "setGraph: " + fundList.size());
        for (int a = 0; a < fundList.size(); a++) {
            dashboardBinding.piechart.addPieSlice(new PieModel(
                    fundList.get(a).getFundName(),
                    Integer.parseInt(fundList.get(a).getTotalInvested()),
                    colorList.get(a)));
        }
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

            try {
                StatsModel statsModel = statsModels.get(position);
                holder.viewBinding.setStats(statsModel);
               /* holder.viewBinding.linearLayout2.setBackgroundColor(statsModel.getColorCode());
                holder.viewBinding.imageView15.setImageResource(statsModel.getIcon());*/
            } catch (Exception e) {
                e.printStackTrace();
                Log.d(TAG, "onBindViewHolder: " + e.getLocalizedMessage());
            }
        }

        @Override
        public int getItemCount() {
            return statsModels.size();
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