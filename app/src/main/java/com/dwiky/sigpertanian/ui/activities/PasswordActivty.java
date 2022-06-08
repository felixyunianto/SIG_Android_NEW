package com.dwiky.sigpertanian.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.dwiky.sigpertanian.contracts.EmailContract;
import com.dwiky.sigpertanian.databinding.ActivityPasswordActivtyBinding;
import com.dwiky.sigpertanian.presenters.PasswordPresenter;
import com.dwiky.sigpertanian.utilities.Storage;

public class PasswordActivty extends AppCompatActivity implements EmailContract.PasswordView {
    ActivityPasswordActivtyBinding binding;
    PasswordPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPasswordActivtyBinding.inflate(getLayoutInflater());
        presenter = new PasswordPresenter(this);
        setContentView(binding.getRoot());

        changePassword();
    }

    public void changePassword(){
        binding.btnLogin.setOnClickListener(view -> {
            Storage storage = new Storage();

            String token =storage.getCode(this);
            String password = binding.etPassword.getText().toString().trim();
            String cPassword = binding.etCPassword.getText().toString().trim();

            presenter.changePassword(token, password, cPassword);

        });
    }

    @Override
    public void success() {
        Storage storage = new Storage();
        storage.clearCode(this);
        startActivity(new Intent(PasswordActivty.this, LoginActivity.class));
    }

    @Override
    public void loading(boolean loading) {
        System.out.println("LOADING " + loading);
    }

    @Override
    public void toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}