package com.example.riss.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.riss.databinding.PublicReviewViewBinding;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

import static com.example.riss.AppUtils.Utils.TIMESTAMP;
import static com.example.riss.AppUtils.Utils.getDateInDMY;
import static com.example.riss.AppUtils.Utils.getTimeAgo;

public class PublicReviewAdapter extends RecyclerView.Adapter<PublicReviewAdapter.PublicVH> {

    List<DocumentSnapshot> reviewList;

    public PublicReviewAdapter(List<DocumentSnapshot> reviewList) {
        this.reviewList = reviewList;
    }

    @NonNull
    @Override
    public PublicReviewAdapter.PublicVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        PublicReviewViewBinding reviewViewBinding = PublicReviewViewBinding.inflate(layoutInflater, parent, false);
        return new PublicVH(reviewViewBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull PublicReviewAdapter.PublicVH holder, int position) {


        try {

            DocumentSnapshot documentSnapshot = reviewList.get(position);
            holder.reviewViewBinding.setReviewModel(documentSnapshot);

            String date = getTimeAgo(documentSnapshot.getLong(TIMESTAMP));
            holder.reviewViewBinding.setDate(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public class PublicVH extends RecyclerView.ViewHolder {
        PublicReviewViewBinding reviewViewBinding;

        public PublicVH(PublicReviewViewBinding reviewViewBinding) {
            super(reviewViewBinding.getRoot());
            this.reviewViewBinding = reviewViewBinding;
        }
    }
}
