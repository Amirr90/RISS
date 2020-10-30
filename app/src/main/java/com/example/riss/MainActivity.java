package com.example.riss;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.riss.databinding.ActivityMainBinding;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

import static com.example.riss.AppUtils.Utils.hideAlertDialog;
import static com.example.riss.AppUtils.Utils.showAlertDialog;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    Animation aniFade;
    CountDownTimer myCountdownTimer;
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        aniFade = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);

        myCountdownTimer = new CountDownTimer(3000, 1000) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                updateUI();
            }
        }.start();
        super.onStart();
        binding.imageView4.startAnimation(aniFade);
        binding.textView4.startAnimation(aniFade);
    }


    private void updateUI() {
        Toast.makeText(this, "updating UI", Toast.LENGTH_SHORT).show();
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            showLoginScreen();
        } else {
            startActivity(new Intent(MainActivity.this, HomeScreen.class));
            finish();
        }
    }

    private void showLoginScreen() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.PhoneBuilder().build());
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setLogo(R.drawable.ic_launcher_foreground)
                        .setTheme(R.style.AppTheme_NoActionBar)
                        .build(),
                10);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 10) {

            if (resultCode == RESULT_OK) {
                Toast.makeText(MainActivity.this, "sign in successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                finish();
            } else {
                Toast.makeText(MainActivity.this, "sign in failed", Toast.LENGTH_SHORT).show();
                updateUI();
            }
        }
    }

}