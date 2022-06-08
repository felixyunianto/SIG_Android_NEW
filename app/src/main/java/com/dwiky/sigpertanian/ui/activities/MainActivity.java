package com.dwiky.sigpertanian.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;

import com.dwiky.sigpertanian.R;
import com.dwiky.sigpertanian.databinding.ActivityMainBinding;
import com.dwiky.sigpertanian.utilities.Storage;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        isLoggedIn();

        BottomNavigationView navView = findViewById(R.id.bottom_navigation);
        NavController navController = Navigation.findNavController(this, R.id.nav_host);
        NavigationUI.setupWithNavController(binding.bottomNavigation, navController);
    }

    public void setActionBarTitle(String title){
//        YOUR_CUSTOM_ACTION_BAR_TITLE
    }

    public void isLoggedIn(){
        Storage storage = new Storage();
        String token = storage.getToken(this);

        System.out.println("TOKEN " + token);
        if(token == null){

            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }
    }
}