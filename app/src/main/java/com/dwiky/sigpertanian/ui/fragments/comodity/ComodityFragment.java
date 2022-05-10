package com.dwiky.sigpertanian.ui.fragments.comodity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dwiky.sigpertanian.adapters.ComodityAdapter;
import com.dwiky.sigpertanian.adapters.OnClickComodityAdapter;
import com.dwiky.sigpertanian.contracts.ComodityContracts;
import com.dwiky.sigpertanian.databinding.FragmentComodityBinding;
import com.dwiky.sigpertanian.models.Comoditas;
import com.dwiky.sigpertanian.presenters.ComodityPresenter;
import com.dwiky.sigpertanian.ui.activities.ComodityManagementActivity;
import com.google.gson.Gson;

import java.util.List;

public class ComodityFragment extends Fragment implements ComodityContracts.ComodityView {

    ComodityPresenter presenter;
    ComodityAdapter adapter;
    private FragmentComodityBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        presenter = new ComodityPresenter(this);
        binding = FragmentComodityBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ComodityManagementActivity.class);
                intent.putExtra("isNew", true);
                startActivity(intent);
            }
        });

        getData();

        return view;
    }

    @Override
    public void attachToRecyclerView(List<Comoditas> comodities) {
        adapter = new ComodityAdapter(getContext(), comodities, new OnClickComodityAdapter() {
            @Override
            public void edit(Comoditas comodity) {
                Intent intent = new Intent(getActivity(), ComodityManagementActivity.class);
                Gson gson = new Gson();
                String comodityAsString = gson.toJson(comodity);
                intent.putExtra("COMODITY", comodityAsString);
                intent.putExtra("isNew", false);
                startActivity(intent);

            }

            @Override
            public void delete(Comoditas comodity) {
                presenter.delete(comodity.getId_komoditas());
            }
        });
        binding.rvComodity.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvComodity.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void toast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean loading(boolean loading) {
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.destroy();
    }

    private void getData(){
        presenter.fetchComodity();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}