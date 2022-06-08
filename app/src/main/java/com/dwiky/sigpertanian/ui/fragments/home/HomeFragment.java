package com.dwiky.sigpertanian.ui.fragments.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dwiky.sigpertanian.R;
import com.dwiky.sigpertanian.databinding.FragmentHomeBinding;
import com.dwiky.sigpertanian.models.User;
import com.dwiky.sigpertanian.ui.fragments.account.AccountFragment;
import com.dwiky.sigpertanian.ui.fragments.agriculture.AgricultureFragment;
import com.dwiky.sigpertanian.ui.fragments.comodity.ComodityFragment;
import com.dwiky.sigpertanian.utilities.Storage;
import com.google.gson.Gson;

public class HomeFragment extends Fragment {
    FragmentHomeBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(getLayoutInflater(), container, false);
        fillName();
        clickComodity();
        clickAgriculture();
        clickProfile();
        return binding.getRoot();
    }

    public void fillName() {
        User user;
        String dataUser = new Storage().getToken(getContext());
        Gson gson = new Gson();
        user = gson.fromJson(dataUser, User.class);

        binding.tvName.setText(user.getFullname());
    }

    public void clickAgriculture(){
        binding.btnLahanPetani.setOnClickListener(view -> {
            final FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.nav_host, new AgricultureFragment(), "AgricultureFragment");
            ft.addToBackStack(null);
            ft.commit();
        });
    }

    public void clickComodity(){
        binding.btnKomoditas.setOnClickListener(view -> {
            final FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.nav_host, new ComodityFragment(), "ComodityFragment");
            ft.addToBackStack(null);
            ft.commit();
        });
    }

    public void clickProfile(){
        binding.btnProfile.setOnClickListener(view -> {
            final FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.nav_host, new AccountFragment(), "AccountFragment");
            ft.addToBackStack(null);
            ft.commit();
        });
    }
}