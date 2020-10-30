package com.example.riss;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.riss.databinding.ActivityProfileBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import static com.example.riss.AppUtils.Utils.IS_ACTIVE;
import static com.example.riss.AppUtils.Utils.USER_NAME;
import static com.example.riss.AppUtils.Utils.USER_QUERY;
import static com.example.riss.AppUtils.Utils.hideAlertDialog;
import static com.example.riss.AppUtils.Utils.hideKeyboard;
import static com.example.riss.AppUtils.Utils.showAlertDialog;

public class ProfileActivity extends AppCompatActivity {

    ActivityProfileBinding profileBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        profileBinding = DataBindingUtil.setContentView(this, R.layout.activity_profile);


        profileBinding.setUser(HomeScreen.getInstance().getUser());
        profileBinding.button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile(v);
            }
        });

    }

    private void updateProfile(final View v) {
        hideKeyboard(ProfileActivity.this);
        showAlertDialog(ProfileActivity.this);
        Map<String, Object> map = new HashMap<>();
        map.put(USER_NAME, profileBinding.textView2.getText().toString());
        // map.put(IS_ACTIVE, profileBinding.textView7.getText().toString());

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection(USER_QUERY).document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                hideAlertDialog();
                Snackbar.make(v, R.string.profile_updated_successfully, Snackbar.LENGTH_SHORT)
                        .setAction(R.string.dismiss, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                            }
                        }).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                hideAlertDialog();
                Toast.makeText(ProfileActivity.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
            }
        });

    }


}