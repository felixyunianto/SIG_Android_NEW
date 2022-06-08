package com.dwiky.sigpertanian.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.dwiky.sigpertanian.contracts.LoginContract;
import com.dwiky.sigpertanian.databinding.ActivityLoginBinding;
import com.dwiky.sigpertanian.models.User;
import com.dwiky.sigpertanian.presenters.LoginPresenter;
import com.dwiky.sigpertanian.utilities.Storage;
import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity implements LoginContract.LoginView {
    ActivityLoginBinding binding;
    LoginPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        presenter = new LoginPresenter(this);
        setContentView(binding.getRoot());
        doLogin();
        isLoggedIn();
        resetPassword();
    }

    public void doLogin(){
        binding.btnLogin.setOnClickListener(view -> {
            String username = binding.etUsername.getText().toString().trim();
            String password = binding.etPassword.getText().toString().trim();

            presenter.login(username, password);

        });
    }


    @Override
    public void toast(String message) {
        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean loading(boolean loading) {
        return loading;
    }

    @Override
    public void success(User user) {
        Gson gson = new Gson();
        String json = gson.toJson(user);
        Storage storage = new Storage();
        storage.setToken(this, json);
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public void isLoggedIn(){
        Storage storage = new Storage();
        String token = storage.getToken(this);

        System.out.println("TOKEN " + token);
    }

    public void resetPassword (){
        binding.tvForgetPassword.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, EmailActivity.class));
        });
    }
}