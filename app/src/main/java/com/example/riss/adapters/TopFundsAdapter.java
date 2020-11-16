package com.example.riss.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.riss.AdapterInterface;
import com.example.riss.databinding.TopFundsViewBinding;
import com.example.riss.models.Fund;
import com.example.riss.models.FundsModel;

import java.util.List;

import kotlin.jvm.functions.FunctionN;

import static com.example.riss.AppUtils.Utils.addLike;
import static com.example.riss.AppUtils.Utils.getCountInRomanFormat;
import static com.example.riss.AppUtils.Utils.getCurrencyFormat;
import static com.example.riss.AppUtils.Utils.getUid;
import static com.example.riss.AppUtils.Utils.removeLike;

public class TopFundsAdapter extends ListAdapter<Fund, TopFundsAdapter.FundsVH> {
    private static final String TAG = "TopFundsAdapter";

    AdapterInterface adapterInterface;

    public TopFundsAdapter(AdapterInterface adapterInterface) {
        super(Fund.itemCallback);
        this.adapterInterface = adapterInterface;
    }

    @NonNull
    @Override
    public TopFundsAdapter.FundsVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        TopFundsViewBinding topFundsViewBinding = TopFundsViewBinding.inflate(layoutInflater, parent, false);
        topFundsViewBinding.setAdpterI(adapterInterface);
        return new FundsVH(topFundsViewBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull final TopFundsAdapter.FundsVH holder, final int position) {
        final Fund fund = getItem(position);
        final boolean isContain = fund.getLikedIds().contains(getUid());
        try {

            holder.topFundsViewBinding.setFund(fund);
            holder.topFundsViewBinding.ivLike.setChecked(isContain);

            String initialValue = getCurrencyFormat(fund.getInitialValue());
            String totalValue = getCurrencyFormat(fund.getTotalInvested());
            holder.topFundsViewBinding.textView14.setText(initialValue);
            holder.topFundsViewBinding.textView16.setText(totalValue);

        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "onBindViewHolder: " + e.getLocalizedMessage());
        }

        holder.topFundsViewBinding.ivLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fundId = fund.getFundId();
                if (isContain) {
                    holder.topFundsViewBinding.ivLike.setChecked(false);
                    removeLike(fundId);
                } else {
                    holder.topFundsViewBinding.ivLike.setChecked(true);
                    addLike(fundId);
                }

            }
        });

    }

    public class FundsVH extends RecyclerView.ViewHolder {
        TopFundsViewBinding topFundsViewBinding;

        public FundsVH(TopFundsViewBinding topFundsViewBinding) {
            super(topFundsViewBinding.getRoot());
            this.topFundsViewBinding = topFundsViewBinding;
        }
    }
}
