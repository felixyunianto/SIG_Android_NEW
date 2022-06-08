package com.dwiky.sigpertanian.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.dwiky.sigpertanian.contracts.EmailContract;
import com.dwiky.sigpertanian.databinding.ActivityEmailBinding;
import com.dwiky.sigpertanian.presenters.EmailPresenter;

public class EmailActivity extends AppCompatActivity implements EmailContract.EmailView {
    ActivityEmailBinding binding;
    EmailPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new EmailPresenter(this);
        binding = ActivityEmailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sendEmail();
    }

    public void sendEmail(){
        binding.btnLogin.setOnClickListener(view -> {
            String email = binding.etUsername.getText().toString().trim();

            presenter.sendEmail(email);
        });
    }


    @Override
    public void success() {
        startActivity(new Intent(EmailActivity.this, CodeActivity.class));
        finish();
    }

    @Override
    public void loading(boolean loading) {
        System.out.println("LOADING "+loading);
    }

    @Override
    public void toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}