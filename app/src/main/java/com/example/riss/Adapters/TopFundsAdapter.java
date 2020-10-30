package com.example.riss.Adapters;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.riss.FundsModel;
import com.example.riss.HomeScreen;
import com.example.riss.R;
import com.example.riss.databinding.TopFundsViewBinding;

import java.util.List;

import static com.example.riss.AppUtils.Utils.FUND_ID;
import static com.example.riss.AppUtils.Utils.addLike;
import static com.example.riss.AppUtils.Utils.removeLike;
import static com.example.riss.view.TopFundsFragment.navController;

public class TopFundsAdapter extends RecyclerView.Adapter<TopFundsAdapter.ViewHolder> {


    List<FundsModel> snapshots;
    List<Boolean> isLikedIds;


    public TopFundsAdapter(List<FundsModel> snapshots, List<Boolean> isLikedIds) {
        this.snapshots = snapshots;
        this.isLikedIds = isLikedIds;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        TopFundsViewBinding fundsViewBinding = TopFundsViewBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(fundsViewBinding);

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        final FundsModel model = snapshots.get(position);
        holder.binding.setFunds(model);

        holder.binding.rlFundLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString(FUND_ID, model.getId());
                navController.navigate(R.id.action_topFundsFragment_to_fundsFragment, bundle);
            }
        });

        holder.binding.likeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean likedStatus = holder.binding.likeIcon.isChecked();
                Toast.makeText(HomeScreen.getInstance(), likedStatus ? "Liked" : "UnLiked", Toast.LENGTH_SHORT).show();
                if (!likedStatus) {
                    removeLike(model.getId(), HomeScreen.getInstance(), model.getLikedIdsList());
                } else {
                    addLike(model.getId(), HomeScreen.getInstance(), model.getLikedIdsList());

                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return snapshots.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TopFundsViewBinding binding;

        public ViewHolder(@NonNull TopFundsViewBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}

