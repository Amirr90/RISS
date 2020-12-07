package com.example.riss.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.fxn.pix.Options;
import com.fxn.pix.Pix;
import com.fxn.utility.PermUtil;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
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
import static com.example.riss.AppUtils.Utils.getMobile;
import static com.example.riss.AppUtils.Utils.getUid;
import static com.example.riss.AppUtils.Utils.hideAlertDialog;
import static com.example.riss.AppUtils.Utils.hideKeyboard;
import static com.example.riss.AppUtils.Utils.showAlertDialog;


public class ProfileFragment extends Fragment {

    private static final int FRONT_IMAGE = 1;
    private static final int BACK_IMAGE = 2;
    private static final int REQ_CODE_FRONT_IMAGE = 1100;
    private static final int REQ_CODE_BACK_IMAGE = 2200;
    FragmentProfileBinding profileBinding;
    NavController navController;
    private static final String TAG = "ProfileFragment";
    Map<String, Object> map;

    String name, mobile, email, address, occupation, education, aadharNo, description, firstName, lastName;
    int ID_TYPE;

    CharSequence[] items = {"Aadhar Card", "PAN card", "VoterId card", "Driving Licence"};

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

        profileBinding.etMobile.setText(getMobile());
        checkUserProfile(new IUserProfileInterface() {
            @Override
            public void isProfileVerified(boolean b) {

            }

            @Override
            public void profileData(DocumentSnapshot snapshot) {
                hideAlertDialog();
                profileBinding.setUser(snapshot);
            }

            @Override
            public void isProfileCompleted(boolean isProfileCom) {
                if (!isProfileCom)
                    Toast.makeText(requireActivity(), "Complete Your profile", Toast.LENGTH_SHORT).show();
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

        profileBinding.tvSelectIdType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectIdTypeDialog();
            }
        });

        profileBinding.ivFrontImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (profileBinding.imageView13.getTag() == "1") {
                    Toast.makeText(requireActivity(), "Image Already verified, it can not be change", Toast.LENGTH_SHORT).show();
                } else
                    selectImage(FRONT_IMAGE);

            }
        });
        profileBinding.ivBackImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (profileBinding.imageView16.getTag() == "1") {
                    Toast.makeText(requireActivity(), "Image Already verified, it can not be change", Toast.LENGTH_SHORT).show();
                } else
                    selectImage(BACK_IMAGE);

            }
        });
    }

    private void selectImage(int imageCode) {
        Options options = Options.init()
                .setRequestCode(imageCode == FRONT_IMAGE ? REQ_CODE_FRONT_IMAGE : REQ_CODE_BACK_IMAGE)                                           //Request code for activity results
                .setCount(1)                                                   //Number of images to restict selection count
                .setFrontfacing(false)                                         //Front Facing camera on start
                //.setPreSelectedUrls(returnValue)                               //Pre selected Image Urls
                //.setSpanCount(4)                                               //Span count for gallery min 1 & max 5
                .setExcludeVideos(false)                                       //Option to exclude videos
                .setVideoDurationLimitinSeconds(30)                            //Duration for video recording
                .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT)     //Orientaion
                .setPath("/RISS/images/profile_image");                                       //Custom Path For media Storage

        Pix.start(requireActivity(), options);
    }

    private void showSelectIdTypeDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

        builder.setTitle("Select ID type");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                dialog.dismiss();
                ID_TYPE = item;
                profileBinding.tvIdTypeText.setText(items[item]);

            }
        }).show();
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
            profileBinding.etAddharNo.setError("Aadhar Number required");
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(requireActivity(), "Approve permissions to open Pix ImagePicker", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQ_CODE_FRONT_IMAGE) {
            ArrayList<String> returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
            Bitmap bm = BitmapFactory.decodeFile(returnValue.get(0));
            profileBinding.ivFrontImage.setImageBitmap(bm);
        } else if (resultCode == Activity.RESULT_OK && requestCode == REQ_CODE_BACK_IMAGE) {
            ArrayList<String> returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
            Toast.makeText(requireActivity(), "" + returnValue.get(0).toString(), Toast.LENGTH_SHORT).show();
            Bitmap bm = BitmapFactory.decodeFile(returnValue.get(0));
            profileBinding.ivBackImage.setImageBitmap(bm);
        }

    }
}