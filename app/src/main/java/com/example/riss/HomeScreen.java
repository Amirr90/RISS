package com.example.riss;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.riss.databinding.ActivityHomeScreenBinding;
import com.example.riss.view.CreateFundFragment;
import com.fxn.pix.Pix;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;

import java.util.ArrayList;

import static com.example.riss.AppUtils.Utils.PAYMENT_STATUS_FAILED;
import static com.example.riss.AppUtils.Utils.PAYMENT_STATUS_SUCCESS;
import static com.example.riss.view.ProfileFragment.BACK_IMAGE_URL;
import static com.example.riss.view.ProfileFragment.FRONT_IMAGE_URL;
import static com.example.riss.view.ProfileFragment.REQ_CODE_BACK_IMAGE;
import static com.example.riss.view.ProfileFragment.REQ_CODE_FRONT_IMAGE;
import static com.example.riss.view.ProfileFragment.profileBinding;
import static com.example.riss.view.SupportFundFragment.payment;

public class HomeScreen extends AppCompatActivity implements PaymentResultWithDataListener {
    private static final String TAG = "HomeScreen";

    ActivityHomeScreenBinding binding;
    static HomeScreen instance;
    FirebaseUser user;

    NavController navController;

    DocumentSnapshot snapshotUser;

    MenuItem addImage;


    public static HomeScreen getInstance() {
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeScreenBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        instance = this;


        user = FirebaseAuth.getInstance().getCurrentUser();

        navController = Navigation.findNavController(this, R.id.nav_controller_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController);

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                if (null != addImage) {
                    if (destination.getId() == R.id.distributionHistoryFragment)
                        addImage.setVisible(true);
                    else addImage.setVisible(false);
                }
            }
        });

    }


    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        addImage = menu.findItem(R.id.action_distributionHistoryFragment_to_addDistributionFragment);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.addDistributionFragment) {

        }
        return NavigationUI.onNavDestinationSelected(item, navController) || super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (isLogin()) {
            user = FirebaseAuth.getInstance().getCurrentUser();
        } else {
            startActivity(new Intent(HomeScreen.this, MainActivity.class));
            finish();
        }
    }


    private boolean isLogin() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            return false;
        } else return true;
    }


    public void navigateUp() {
        onSupportNavigateUp();
    }

    public void setUser(DocumentSnapshot user) {
        snapshotUser = user;
    }

    public DocumentSnapshot getUser() {
        return snapshotUser;
    }

    @Override
    public void onPaymentSuccess(String s, PaymentData paymentData) {
        Log.d(TAG, "onPaymentSuccess: Status " + s);
        Log.d(TAG, "onPaymentSuccess: " + paymentData.getSignature());
        try {
            if (null != payment)
                payment.updatePaymentStatus(paymentData.getPaymentId(), PAYMENT_STATUS_SUCCESS);
            else
                CreateFundFragment.startPayment.updatePaymentStatus(paymentData.getPaymentId(), PAYMENT_STATUS_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "onPaymentSuccess: " + e.getLocalizedMessage());
        }

    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {
        Log.d(TAG, "onPaymentError: " + paymentData.getData());
        payment.updatePaymentStatus(paymentData.getPaymentId(), PAYMENT_STATUS_FAILED);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && requestCode == REQ_CODE_FRONT_IMAGE) {
            ArrayList<String> returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
            if (null != returnValue && returnValue.isEmpty()) {
                Toast.makeText(instance, "try again", Toast.LENGTH_SHORT).show();
                return;
            }
            Bitmap bm = BitmapFactory.decodeFile(returnValue.get(0));
            Log.d(TAG, "ImagePath: " + bm);
            profileBinding.ivFrontImage.setImageBitmap(bm);
            FRONT_IMAGE_URL = returnValue.get(0);
        } else if (resultCode == Activity.RESULT_OK && requestCode == REQ_CODE_BACK_IMAGE) {
            ArrayList<String> returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
            if (null != returnValue && returnValue.isEmpty()) {
                Toast.makeText(instance, "try again", Toast.LENGTH_SHORT).show();
                return;
            }
            Bitmap bm = BitmapFactory.decodeFile(returnValue.get(0));
            profileBinding.ivBackImage.setImageBitmap(bm);
            BACK_IMAGE_URL = returnValue.get(0);
        } else {
            ArrayList<String> returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
            Bitmap bm = BitmapFactory.decodeFile(returnValue.get(0));
            profileBinding.ivBackImage.setImageBitmap(bm);
            Log.d(TAG, "RandomImagePath: " + bm);
        }
    }
}