package com.example.riss;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.riss.AppUtils.Utils;
import com.example.riss.databinding.ActivityMainBinding;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.riss.AppUtils.Utils.USER_QUERY;
import static com.example.riss.AppUtils.Utils.getFirestoreReference;
import static com.example.riss.AppUtils.Utils.getUid;

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
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            showLoginScreen();
        } else {
            updateToken();
            startActivity(new Intent(MainActivity.this, HomeScreen.class));
            finish();
        }
    }

    private void updateToken() {
        String uid = Utils.getUid();

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();
                        Map<String, Object> map = new HashMap<>();
                        map.put("token", token);
                        getFirestoreReference()
                                .collection(USER_QUERY).document(getUid())
                                .update(map);

                        Log.d(TAG, "TOKEN: " + token);
                    }
                });
    }

    private void showLoginScreen() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(new AuthUI.IdpConfig.PhoneBuilder().build());

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
                startActivity(new Intent(MainActivity.this, HomeScreen.class));
                finish();
            } else {
                Toast.makeText(MainActivity.this, "sign in failed", Toast.LENGTH_SHORT).show();
                updateUI();
            }
        }
    }

}