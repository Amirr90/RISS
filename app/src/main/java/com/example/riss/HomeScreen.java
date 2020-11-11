package com.example.riss;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.riss.databinding.ActivityHomeScreenBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomeScreen extends AppCompatActivity {
    ActivityHomeScreenBinding binding;
    static HomeScreen instance;
    FirebaseUser user;

    NavController navController;


    DocumentSnapshot snapshotUser;

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

    }


    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
}