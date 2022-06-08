package com.dwiky.sigpertanian.ui.fragments.account;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dwiky.sigpertanian.R;
import com.dwiky.sigpertanian.databinding.FragmentAccountBinding;
import com.dwiky.sigpertanian.databinding.FragmentComodityBinding;
import com.dwiky.sigpertanian.models.User;
import com.dwiky.sigpertanian.ui.activities.LoginActivity;
import com.dwiky.sigpertanian.utilities.Storage;
import com.google.gson.Gson;

public class AccountFragment extends Fragment {
    private FragmentAccountBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAccountBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        getData();
        logout();
        return view;
    }

    public void getData(){
        User user;
        String dataUser = new Storage().getToken(getContext());
        Gson gson = new Gson();
        user = gson.fromJson(dataUser, User.class);

        binding.fullname.setText(user.getFullname());
        binding.email.setText(user.getEmail());
        binding.username.setText(user.getUsername());
    }

    public void logout(){
        binding.btnLogout.setOnClickListener(view -> {
            Storage storage = new Storage();
            storage.clearToken(getContext());
            startActivity(new Intent(getActivity(), LoginActivity.class));;
            getActivity().finish();
        });
    }
}