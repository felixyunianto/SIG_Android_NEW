package com.dwiky.sigpertanian.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dwiky.sigpertanian.R;
import com.dwiky.sigpertanian.adapters.AgricultureAdapter;
import com.dwiky.sigpertanian.contracts.AgricultureContracts;
import com.dwiky.sigpertanian.databinding.ActivityAgricultureDetailBinding;
import com.dwiky.sigpertanian.models.Agriculture;
import com.dwiky.sigpertanian.presenters.AgricultureManagementPresenter;
import com.dwiky.sigpertanian.presenters.AgriculturePresenter;
import com.dwiky.sigpertanian.responses.WrappedListResponse;
import com.dwiky.sigpertanian.responses.WrappedResponse;
import com.dwiky.sigpertanian.utilities.APIClient;
import com.dwiky.sigpertanian.webservices.APIServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AgricultureDetailActivity extends AppCompatActivity implements AgricultureContracts.AgricultureView, OnMapReadyCallback {
    ActivityAgricultureDetailBinding binding;
    AgriculturePresenter presenter;

    GoogleMap myMap;
    SupportMapFragment mapFragment;
    Agriculture data;

    private APIServices apiServices = new APIClient().apiServices();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAgricultureDetailBinding.inflate(getLayoutInflater());
        presenter = new AgriculturePresenter(this);
        setContentView(binding.getRoot());

        binding.imageGoBack.setOnClickListener(view -> {
            onBack();
        });

        if (myMap == null) {
            mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(googleMap -> {
                myMap = googleMap;
            });
        }

        binding.btnEdit.setOnClickListener(view -> {
            edit();
        });

        binding.btnHapus.setOnClickListener(view -> {
            hapus();
        });

    }

    public void onBack(){
        super.onBackPressed();
    }

    public void getData(){
        String id = getIntent().getStringExtra("ID_LAHAN");
        presenter.fetchDetail(id);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.destroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    @Override
    public void attachToRecyclerView(List<Agriculture> agricultures) {

    }

    @Override
    public void attachDetailView(Agriculture agriculture) {
        Glide.with(this).load(agriculture.getFoto()).into(binding.imageDetail);
        data = agriculture;
        myMap.clear();
        myMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(data.getLatitude()), Double.parseDouble(data.getLongitude()))));
        myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
                Double.parseDouble(agriculture.getLatitude()), Double.parseDouble(agriculture.getLongitude())),12f)
        );

        binding.luas.setText(agriculture.getLuas());
        binding.meter.setText(agriculture.getMeter());

        binding.namaPemilik.setText(agriculture.getNamapemilik());
        binding.desa.setText(agriculture.getDesa());
        binding.kecamatan.setText(agriculture.getKecamatan());
    }

    @Override
    public void toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean loading(boolean loading) {
        return false;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        myMap = googleMap;
        if(ActivityCompat.checkSelfPermission(
                AgricultureDetailActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                AgricultureDetailActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
        ){
            return;
        }

        myMap.isMyLocationEnabled();
    }

    public void edit() {
        Intent intent = new Intent(this, AgricultureManagement.class);
        Gson gson = new Gson();
        String agricultureAsString = gson.toJson(data);
        intent.putExtra("AGRICULTURE", agricultureAsString);
        intent.putExtra("isNew", false);
        startActivity(intent);
    }



    public void hapus(){
        AlertDialog alertDialog = new AlertDialog.Builder(AgricultureDetailActivity.this).create();
        alertDialog.setTitle("Confirm");
        alertDialog.setMessage("Apakah yakin ingin menghapus?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which) {
                        apiServices.delete(Integer.parseInt(data.getId_lahan()))
                                .enqueue(new Callback<WrappedResponse<Agriculture>>() {
                                    @Override
                                    public void onResponse(Call<WrappedResponse<Agriculture>> call, Response<WrappedResponse<Agriculture>> response) {
                                        if(response.isSuccessful()){
                                            WrappedResponse body = response.body();
                                            if(body != null){
                                                toast(body.getMessage());
                                                finish();
                                            }else{
                                                toast("Error");
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<WrappedResponse<Agriculture>> call, Throwable t) {
                                        System.out.println("Terjadi kesalahan " + t.getMessage());
                                    }
                                });
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Batal",
                new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

        alertDialog.show();

    }
}