package com.example.riss.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.Uri;
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
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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

    public static final int FRONT_IMAGE = 1;
    public static final int BACK_IMAGE = 2;
    public static final int REQ_CODE_FRONT_IMAGE = 1100;
    public static final int REQ_CODE_BACK_IMAGE = 2200;
    public static FragmentProfileBinding profileBinding;
    NavController navController;
    private static final String TAG = "ProfileFragment";
    Map<String, Object> map;

    String name, mobile, email, address, occupation, education, aadharNo, description, firstName, lastName;
    int ID_TYPE = -1;

    CharSequence[] items = {"Aadhar Card", "PAN card", "VoterId card", "Driving Licence"};

    public static String FRONT_IMAGE_URL = null;
    public static String BACK_IMAGE_URL = null;


    Map<String, Object> urlsMap;


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        profileBinding = FragmentProfileBinding.inflate(getLayoutInflater());
        return profileBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        showAlertDialog(requireActivity());

        urlsMap = new HashMap<>();
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
                    Toast.makeText(requireActivity(), getString(R.string.already_image_verifified_text), Toast.LENGTH_SHORT).show();
                } else
                    selectImage(FRONT_IMAGE);

            }
        });
        profileBinding.ivBackImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (profileBinding.imageView16.getTag() == "1") {
                    Toast.makeText(requireActivity(), getString(R.string.already_image_verifified_text), Toast.LENGTH_SHORT).show();
                } else
                    selectImage(BACK_IMAGE);

            }
        });
    }

    private void selectImage(int imageCode) {
        Options options = Options.init()
                .setRequestCode(imageCode == FRONT_IMAGE ? REQ_CODE_FRONT_IMAGE : REQ_CODE_BACK_IMAGE)
                .setCount(1)
                .setFrontfacing(false)
                .setExcludeVideos(false)

                .setVideoDurationLimitinSeconds(30)
                .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT)
                .setPath("/RISS/images/profile_image");

        Pix.start(requireActivity(), options);
    }

    private void showSelectIdTypeDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

        builder.setTitle(R.string.selected_id_type);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                dialog.dismiss();
                ID_TYPE = item;
                profileBinding.tvIdTypeText.setText(items[item]);
               /* if (item == 1 || item == 3) {
                    profileBinding.ivBackImage.setVisibility(View.GONE);
                    profileBinding.textView58.setVisibility(View.GONE);
                } else {
                    profileBinding.ivBackImage.setVisibility(View.VISIBLE);
                    profileBinding.textView58.setVisibility(View.VISIBLE);
                }
*/
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
        showAlertDialog(requireActivity(), false);
        getFirestoreReference().collection(USER_QUERY).document(getUid()).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                uploadImage();
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
    public void onRequestPermissionsResult(int requestCode, @NotNull String permissions[], @NotNull int[] grantResults) {
        switch (requestCode) {
            case PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(requireActivity(), "Approve permissions to open Pix ImagePicker", Toast.LENGTH_LONG).show();
                }

            }
        }
    }


    private void uploadImage() {
       /* if (ID_TYPE == 1 || ID_TYPE == 3) {
            if (FRONT_IMAGE_URL != null)
                uploadFrontImage(FRONT_IMAGE_URL);
            else
                Toast.makeText(requireActivity(), R.string.profile_updated_successfully, Toast.LENGTH_SHORT).show();
        } else {
            if (null != FRONT_IMAGE_URL && null != BACK_IMAGE_URL) {
                uploadFrontImage(FRONT_IMAGE_URL);
            } else
                Toast.makeText(requireActivity(), "select front and back both Image", Toast.LENGTH_SHORT).show();
        }*/
        if (null != FRONT_IMAGE_URL && null != BACK_IMAGE_URL) {
            uploadFrontImage(FRONT_IMAGE_URL);
        } else if (null == FRONT_IMAGE_URL && null != BACK_IMAGE_URL) {
            Toast.makeText(requireActivity(), "Select Front Image", Toast.LENGTH_SHORT).show();
        } else if (null == BACK_IMAGE_URL && null != FRONT_IMAGE_URL) {
            Toast.makeText(requireActivity(), "Select Back Image", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(requireActivity(), R.string.profile_updated_successfully, Toast.LENGTH_SHORT).show();
    }

    private void uploadFrontImage(String frontImageUrl) {
        StorageReference mStorageRef;
        mStorageRef = FirebaseStorage.getInstance().getReference();
        Uri file = Uri.fromFile(new File(frontImageUrl));
        final StorageReference riversRef = mStorageRef.child("aadharImage/" + getUid() + "/frontImage/" + System.currentTimeMillis() + ".jpg");
        UploadTask uploadTask = riversRef.putFile(file);
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw Objects.requireNonNull(task.getException());
                }
                return riversRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    Log.d(TAG, "onCompleteFrontImage: " + downloadUri);
                    urlsMap.put("aadharFront", "" + downloadUri);
                    if (null == BACK_IMAGE_URL) {
                        updateImage(urlsMap);
                        return;
                    }
                    uploadBackImage(BACK_IMAGE_URL);
                } else {
                    Log.d(TAG, "onUploadingBackImageError: " + Objects.requireNonNull(task.getException()).getLocalizedMessage());
                }
            }
        });
    }

    private void uploadBackImage(String frontImageUrl) {
        StorageReference mStorageRef;
        mStorageRef = FirebaseStorage.getInstance().getReference();
        Uri file = Uri.fromFile(new File(frontImageUrl));
        final StorageReference riversRef = mStorageRef.child("aadharImage/" + getUid() + "/backImage/" + System.currentTimeMillis() + ".jpg");
        UploadTask uploadTask = riversRef.putFile(file);
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw Objects.requireNonNull(task.getException());
                }

                return riversRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    Log.d(TAG, "onCompleteBackImage: " + downloadUri);
                    urlsMap.put("aadharBack", "" + downloadUri);
                    updateImage(urlsMap);
                } else {
                    Log.d(TAG, "onUploadingFrontImageError: " + Objects.requireNonNull(task.getException()).getLocalizedMessage());
                }
            }
        });
    }

    private void updateImage(Map<String, Object> urlsMap) {
        getFirestoreReference().collection(USER_QUERY).document(getUid()).update(urlsMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                hideAlertDialog();
                Toast.makeText(requireActivity(), "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                hideAlertDialog();
                Toast.makeText(requireActivity(), "could't update profile try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

}