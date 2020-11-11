package com.example.riss.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.riss.HomeScreen;
import com.example.riss.R;
import com.example.riss.databinding.FragmentWriteReviewBinding;
import com.example.riss.models.PublicReviewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.auth.User;

import static com.example.riss.AppUtils.Utils.IMAGE;
import static com.example.riss.AppUtils.Utils.QUERY_PUBLIC_REVIEW;
import static com.example.riss.AppUtils.Utils.USER_NAME;
import static com.example.riss.AppUtils.Utils.getFirestoreReference;
import static com.example.riss.AppUtils.Utils.getUid;
import static com.example.riss.AppUtils.Utils.hideAlertDialog;
import static com.example.riss.AppUtils.Utils.hideKeyboard;
import static com.example.riss.AppUtils.Utils.showAlertDialog;
import static com.example.riss.view.HomeFragment.getUser;


public class WriteReviewFragment extends Fragment {


    FragmentWriteReviewBinding writeReviewBinding;
    String description;
    double rating;
    PublicReviewModel reviewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        writeReviewBinding = FragmentWriteReviewBinding.inflate(getLayoutInflater());
        return writeReviewBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        writeReviewBinding.btnWriteReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isDescriptionIsWritten()) {
                    writeReview();
                }
            }
        });
    }

    private boolean isDescriptionIsWritten() {

        rating = writeReviewBinding.ratingBar2.getRating();
        description = writeReviewBinding.editTextTextWriteReview.getText().toString().trim();

        if (TextUtils.isEmpty(description)) {
            Toast.makeText(requireActivity(), "Write Description", Toast.LENGTH_SHORT).show();
            return false;
        } else if (description.length() < 25) {
            Toast.makeText(requireActivity(), "Description must Be greater then 25 characters", Toast.LENGTH_SHORT).show();
            return false;
        } else return true;
    }

    private void writeReview() {

        hideKeyboard(requireActivity());
        showAlertDialog(requireActivity());

        reviewModel = new PublicReviewModel();

        reviewModel.setRating(rating);
        reviewModel.setText(description);
        reviewModel.setUid(getUid());

        reviewModel.setUsername(getUser().getString(USER_NAME));
        reviewModel.setImage(getUser().getString(IMAGE));

        reviewModel.setTimestamp(System.currentTimeMillis());

        getFirestoreReference().collection(QUERY_PUBLIC_REVIEW)
                .document(getUid())
                .set(reviewModel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                hideAlertDialog();
                Toast.makeText(requireActivity(), "Review Added Successfully", Toast.LENGTH_SHORT).show();
                HomeScreen.getInstance().onSupportNavigateUp();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                hideAlertDialog();
                Toast.makeText(requireActivity(), "try again", Toast.LENGTH_SHORT).show();

            }
        });
    }
}