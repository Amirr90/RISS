package com.example.riss.view;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.riss.R;
import com.example.riss.databinding.FragmentProfileBinding;
import com.example.riss.interfaces.IUserProfileInterface;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

import static com.example.riss.AppUtils.Utils.AADHAR_NO;
import static com.example.riss.AppUtils.Utils.ADDRESS;
import static com.example.riss.AppUtils.Utils.EDUCATION;
import static com.example.riss.AppUtils.Utils.EMAIL;
import static com.example.riss.AppUtils.Utils.FIRST_NAME;
import static com.example.riss.AppUtils.Utils.LAST_NAME;
import static com.example.riss.AppUtils.Utils.OCCUPATION;
import static com.example.riss.AppUtils.Utils.USER_NAME;
import static com.example.riss.AppUtils.Utils.USER_QUERY;
import static com.example.riss.AppUtils.Utils.checkUserProfile;
import static com.example.riss.AppUtils.Utils.getFirestoreReference;
import static com.example.riss.AppUtils.Utils.getUid;
import static com.example.riss.AppUtils.Utils.hideAlertDialog;
import static com.example.riss.AppUtils.Utils.hideKeyboard;
import static com.example.riss.AppUtils.Utils.showAlertDialog;


public class ProfileFragment extends Fragment {

    FragmentProfileBinding profileBinding;
    NavController navController;
    private static final String TAG = "ProfileFragment";
    Map<String, Object> map;

    String name, mobile, email, address, occupation, education, aadharNo, description, firstName, lastName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        profileBinding = FragmentProfileBinding.inflate(getLayoutInflater());
        return profileBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        showAlertDialog(requireActivity());
        checkUserProfile(new IUserProfileInterface() {
            @Override
            public boolean isProfileVerified() {
                return false;
            }

            @Override
            public void profileData(DocumentSnapshot snapshot) {
                hideAlertDialog();
                profileBinding.setUser(snapshot);
            }

            @Override
            public void isProfileCompleted(boolean isProfileCom) {

            }

            @Override
            public void onError(String msg) {
                hideAlertDialog();
                Log.d(TAG, "onError: " + msg);
                Toast.makeText(requireActivity(), "try again", Toast.LENGTH_SHORT).show();
            }
        });


        profileBinding.btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAllFieldsFill()) {
                    updateProfile();
                }
            }
        });
    }

    private boolean isAllFieldsFill() {
        firstName = profileBinding.etFirstName.getText().toString().trim();
        lastName = profileBinding.etLastName.getText().toString().trim();
        mobile = profileBinding.etMobile.getText().toString().trim();
        email = profileBinding.etEmail.getText().toString().trim();
        address = profileBinding.etAddress.getText().toString().trim();
        occupation = profileBinding.etOccupation.getText().toString().trim();
        education = profileBinding.etEducation.getText().toString().trim();
        aadharNo = profileBinding.etAddharNo.getText().toString().trim();

        if (TextUtils.isEmpty(firstName)) {
            profileBinding.etFirstName.setError("first name required");
            return false;
        } else if (TextUtils.isEmpty(lastName)) {
            profileBinding.etLastName.setError("last name required");
            return false;
        } else if (TextUtils.isEmpty(mobile)) {
            profileBinding.etMobile.setError("mobile number required");
            return false;
        } else if (TextUtils.isEmpty(email)) {
            profileBinding.etEmail.setError("email-Id required");
            return false;
        } else if (TextUtils.isEmpty(address)) {
            profileBinding.etAddress.setError("address required");
            return false;
        } else if (TextUtils.isEmpty(occupation)) {
            profileBinding.etOccupation.setError("occupation required");
            return false;
        } else if (TextUtils.isEmpty(education)) {
            profileBinding.etEducation.setError("education required");
            return false;
        } else if (TextUtils.isEmpty(aadharNo)) {
            profileBinding.etAddharNo.setError("aadharNo required");
            return false;
        } else {
            map = new HashMap<>();
            map.put(USER_NAME, firstName + " " + lastName);
            map.put(EMAIL, email);
            map.put(FIRST_NAME, firstName);
            map.put(LAST_NAME, lastName);
            map.put(ADDRESS, address);
            map.put(OCCUPATION, occupation);
            map.put(EDUCATION, education);
            map.put(AADHAR_NO, aadharNo);
            return true;
        }
    }

    private void updateProfile() {
        hideKeyboard(requireActivity());
        showAlertDialog(requireActivity());
        getFirestoreReference().collection(USER_QUERY).document(getUid()).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                hideAlertDialog();
                Toast.makeText(requireActivity(), R.string.profile_updated_successfully, Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: " + e.getLocalizedMessage());
                Toast.makeText(requireContext(), "try again", Toast.LENGTH_SHORT).show();
            }
        });
    }
}