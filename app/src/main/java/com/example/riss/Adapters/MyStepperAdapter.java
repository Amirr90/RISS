package com.example.riss.Adapters;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.example.riss.R;
import com.example.riss.StepsFragment.Fragment1;
import com.example.riss.StepsFragment.Fragment2;
import com.example.riss.StepsFragment.Fragment3;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter;
import com.stepstone.stepper.viewmodel.StepViewModel;

public class MyStepperAdapter extends AbstractFragmentStepAdapter {

    private static final String CURRENT_STEP_POSITION_KEY = "current_step";

    public MyStepperAdapter(FragmentManager fm, Context context) {
        super(fm, context);
    }

    @Override
    public Step createStep(int position) {
        switch (position) {
            case 0: {
                final Fragment1 step = new Fragment1();
                Bundle b = new Bundle();
                b.putInt(CURRENT_STEP_POSITION_KEY, position);
                step.setArguments(b);
                return step;
            }

            case 1: {
                final Fragment2 step = new Fragment2();
                Bundle b = new Bundle();
                b.putInt(CURRENT_STEP_POSITION_KEY, position);
                step.setArguments(b);
                return step;
            }
            default: {
                final Fragment3 step = new Fragment3();
                Bundle b = new Bundle();
                b.putInt(CURRENT_STEP_POSITION_KEY, position);
                step.setArguments(b);
                return step;
            }
        }

    }

    @Override
    public int getCount() {
        return 3;
    }

   /* @NonNull
    @Override
    public StepViewModel getViewModel(@IntRange(from = 0) int position) {
        //Override this method to set Step title for the Tabs, not necessary for other stepper types
        return new StepViewModel.Builder(context)
                .setTitle(R.string.tab_title) //can be a CharSequence instead
                .create();
    }*/


    @NonNull
    @Override
    public StepViewModel getViewModel(@IntRange(from = 0) int position) {
        StepViewModel.Builder builder = new StepViewModel.Builder(context)
                .setTitle(R.string.tab_title);
        switch (position) {
            case 0:
                builder
                        .setEndButtonLabel("Next")
                        .setBackButtonLabel("Cancel")
                        .setNextButtonEndDrawableResId(R.drawable.ms_forward_arrow)
                        .setBackButtonStartDrawableResId(StepViewModel.NULL_DRAWABLE);
                break;
            case 1:
                builder
                        .setEndButtonLabel(R.string.go_to_summary)
                        .setBackButtonLabel("Go to first")
                        .setBackButtonStartDrawableResId(R.drawable.ms_back_arrow);
                break;
            case 2:
                builder
                        .setBackButtonLabel("Go back")
                        .setEndButtonLabel("I'm done!");
                break;
            default:
                throw new IllegalArgumentException("Unsupported position: " + position);
        }
        return builder.create();
    }
}
