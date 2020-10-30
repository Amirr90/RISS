package com.example.riss;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.riss.databinding.ActivityCreateFundScreenBinding;
import com.fxn.pix.Options;
import com.fxn.pix.Pix;
import com.fxn.utility.PermUtil;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.shreyaspatil.EasyUpiPayment.EasyUpiPayment;
import com.shreyaspatil.EasyUpiPayment.listener.PaymentStatusListener;
import com.shreyaspatil.EasyUpiPayment.model.TransactionDetails;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.riss.AppUtils.Utils.DONATION;
import static com.example.riss.AppUtils.Utils.FUNDS;
import static com.example.riss.AppUtils.Utils.FUNDS_DURATION;
import static com.example.riss.AppUtils.Utils.IMAGE;
import static com.example.riss.AppUtils.Utils.LIKE;
import static com.example.riss.AppUtils.Utils.LIKED_IDS;
import static com.example.riss.AppUtils.Utils.NAME;
import static com.example.riss.AppUtils.Utils.NUMBER;
import static com.example.riss.AppUtils.Utils.SUPPORT;
import static com.example.riss.AppUtils.Utils.TIMESTAMP;
import static com.example.riss.AppUtils.Utils.UID;
import static com.example.riss.AppUtils.Utils.hideAlertDialog;

public class CreateFundScreen extends AppCompatActivity implements PaymentStatusListener {

    private static final int ADD_IMAGE_REQ_CODE = 10;
    ActivityCreateFundScreenBinding binding;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    FirebaseUser user;
    Uri imageUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_fund_screen);

        user = FirebaseAuth.getInstance().getCurrentUser();

        binding.toolbar4.setTitle(R.string.create_fund);
        setSupportActionBar(binding.toolbar4);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.etSelectAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] items = {
                        "Rs 100 for 3 months",
                        "Rs 500 for 6 months",
                        "Rs 1,000 for 9 months",
                        "Rs 2,500 for 12 months",
                        "Rs 5,000 for 15 months",
                        "Rs 10,00 for 18 months",
                        "Rs 25,000 for 21 months",
                        "Rs 50,000 for 24 months",
                        "Rs 1,00,000 for 30 months",
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(CreateFundScreen.this);
                builder.setTitle("Make your selection");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        dialog.dismiss();
                        binding.etFundPeriod.setText(getFundPeriod(item));
                        binding.etSelectAmount.setText(items[item]);
                    }
                }).show();
            }
        });
    }

    private String getFundPeriod(int item) {
        if (item == 0)
            return "3 months";
        else if (item == 1)
            return "6 months";
        else if (item == 2)
            return "9 months";
        else if (item == 3)
            return "12 months";
        else if (item == 4)
            return "15 months";
        else if (item == 5)
            return "18 months";
        else if (item == 6)
            return "21 months";
        else if (item == 7)
            return "24 months";
        else return "30 months";
    }

    public void createFund(View view) {
        if (view.getId() == R.id.btnCreatefund) {
            if (checkFields())
                //startPayment();
                uploadImage();


        }
    }

    private void startPayment() {
        final EasyUpiPayment easyUpiPayment = new EasyUpiPayment.Builder()
                .with(this)
                .setPayeeVpa("9044865611@ybl")
                .setPayeeName("Amir Khan")
                .setTransactionId(String.valueOf(System.currentTimeMillis()))
                .setTransactionRefId(String.valueOf(System.currentTimeMillis() + 20))
                .setDescription("For Creating Fund")
                .setAmount("1.00")
                .build();

        easyUpiPayment.startPayment();

        easyUpiPayment.setPaymentStatusListener(this);
    }

    private boolean checkFields() {
        if (binding.etFundName.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Funds Name required", Toast.LENGTH_SHORT).show();
            return false;
        } else if (binding.etSelectAmount.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Select Amount/Support", Toast.LENGTH_SHORT).show();
            return false;
        } else if (binding.etFundPeriod.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Select fund Period", Toast.LENGTH_SHORT).show();
            return false;
        } else
            return true;
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    private void createFund(String imageUrl) {

        List<String> likeList = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put(NAME, binding.etFundName.getText().toString().trim());
        map.put(FUNDS_DURATION, binding.etFundPeriod.getText().toString().trim());
        map.put(DONATION, binding.etSelectAmount.getText().toString().trim());
        map.put(IMAGE, imageUrl);
        map.put(NUMBER, user.getPhoneNumber());
        map.put(UID, user.getUid());
        map.put(TIMESTAMP, System.currentTimeMillis());
        map.put(SUPPORT, 0);
        map.put(LIKE, 0);
        map.put(LIKED_IDS, likeList);

        firestore.collection(FUNDS).add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference reference) {
                hideAlertDialog();
                Toast.makeText(CreateFundScreen.this, R.string.funds_added, Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                hideAlertDialog();
                Toast.makeText(CreateFundScreen.this, R.string.failed_to_add_funds, Toast.LENGTH_SHORT).show();
            }
        }).addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {
                hideAlertDialog();
                Toast.makeText(CreateFundScreen.this, R.string.cancel_add_funds, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void uploadImage() {
        String fundId = "" + System.currentTimeMillis();
        if (imageUri == null) {
            Toast.makeText(this, "image Not Found", Toast.LENGTH_SHORT).show();
            createFund("");
        } else {
            // Create a storage reference from our app
            StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
            // Create a reference to "mountains.jpg"
            final StorageReference mountainsRef = mStorageRef.child("FundsImage").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("fundsImage")
                    .child(fundId + ".jpg");
            // Get the data from an ImageView as bytes
            binding.imageView2.setDrawingCacheEnabled(true);
            binding.imageView2.buildDrawingCache();
            Bitmap bitmap = ((BitmapDrawable) binding.imageView2.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();
            UploadTask uploadTask = mountainsRef.putBytes(data);

            uploadTask = mountainsRef.putFile(imageUri);

            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return mountainsRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        createFund(String.valueOf(downloadUri));
                    } else {
                        Toast.makeText(CreateFundScreen.this, "Failed To Upload Image", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }


    public void onClick(View view) {
        if (view.getId() == R.id.tvAddImage) {
            addImage();
        }
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Pix.start(CreateFundScreen.this, Options.init().setRequestCode(ADD_IMAGE_REQ_CODE));
                } else {
                    Toast.makeText(CreateFundScreen.this, "Approve permissions to select Image from Gallery", Toast.LENGTH_LONG).show();
                }

            }
            break;

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (null != data) {
                Uri uri = data.getData();

                imageUri = uri;
                binding.imageView2.setImageURI(uri);
                if (binding.imageView2.getDrawable() != null && !binding.imageView2.getDrawable().toString().isEmpty())
                    binding.tvAddImage.setText(R.string.change_image);
                else binding.tvAddImage.setText(R.string.select_image);

                try {
                    //uploadImageToFirebase();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(CreateFundScreen.this, "try again " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }

        }

    }

    private void addImage() {
        ImagePicker.Companion.with(CreateFundScreen.this)
                .crop(4f, 4f)                    //Crop image(Optional), Check Customization for more option
                .compress(512)            //Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                .start();
    }

    @Override
    public void onTransactionCompleted(TransactionDetails transactionDetails) {
        Toast.makeText(this, transactionDetails.getStatus(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTransactionSuccess() {
        Toast.makeText(this, "Payment Success", Toast.LENGTH_SHORT).show();
        // uploadImage();
    }

    @Override
    public void onTransactionSubmitted() {
        Toast.makeText(this, "Transaction Submitted", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTransactionFailed() {
        Toast.makeText(this, "Transaction Failed", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onTransactionCancelled() {
        Toast.makeText(this, "Transaction Cancelled", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onAppNotFound() {
        Toast.makeText(this, "App not found", Toast.LENGTH_SHORT).show();

    }
}