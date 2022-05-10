package com.dwiky.sigpertanian.ui.fragments.agriculture;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dwiky.sigpertanian.R;
import com.dwiky.sigpertanian.adapters.AgricultureAdapter;
import com.dwiky.sigpertanian.adapters.OnClickAgricultureAdapter;
import com.dwiky.sigpertanian.contracts.AgricultureContracts;
import com.dwiky.sigpertanian.databinding.FragmentAgricultureBinding;
import com.dwiky.sigpertanian.models.Agriculture;
import com.dwiky.sigpertanian.presenters.AgriculturePresenter;
import com.dwiky.sigpertanian.ui.activities.AgricultureManagement;

import java.time.chrono.JapaneseDate;
import java.util.List;

public class AgricultureFragment extends Fragment implements AgricultureContracts.AgricultureView {
    AgriculturePresenter presenter;
    AgricultureAdapter adapter;
    private FragmentAgricultureBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        presenter = new AgriculturePresenter(this);
        binding = FragmentAgricultureBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AgricultureManagement.class);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void attachToRecyclerView(List<Agriculture> agricultures) {
        adapter = new AgricultureAdapter(getContext(), agricultures, new OnClickAgricultureAdapter() {
            @Override
            public void edit(Agriculture agriculture) {

            }

            @Override
            public void delete(Agriculture agriculture) {

            }
        });

        binding.rvAgriculture.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvAgriculture.setAdapter(adapter);
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
        presenter.fetchAgriculture();
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }
}