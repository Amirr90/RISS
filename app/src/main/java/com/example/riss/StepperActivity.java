package com.example.riss;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.riss.Adapters.MyStepperAdapter;
import com.example.riss.databinding.ActivityHomeScreenBinding;
import com.example.riss.databinding.ActivityStepperBinding;

public class StepperActivity extends AppCompatActivity {

    ActivityStepperBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStepperBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
    }

    @Override
    protected void onStart() {
        super.onStart();
        init();
    }

    private void init() {
        binding.stepperLayout.setAdapter(new MyStepperAdapter(getSupportFragmentManager(), StepperActivity.this));
    }
}