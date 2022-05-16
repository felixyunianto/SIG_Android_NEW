package com.dwiky.sigpertanian.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dwiky.sigpertanian.R;
import com.dwiky.sigpertanian.contracts.AgricultureContracts;
import com.dwiky.sigpertanian.databinding.ActivityAgricultureManagementBinding;
import com.dwiky.sigpertanian.models.Agriculture;
import com.dwiky.sigpertanian.models.Comoditas;
import com.dwiky.sigpertanian.models.District;
import com.dwiky.sigpertanian.models.SubDistrict;
import com.dwiky.sigpertanian.presenters.AgricultureManagementPresenter;
import com.dwiky.sigpertanian.utilities.UploadImage;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

public class AgricultureManagement extends AppCompatActivity implements AgricultureContracts.AgricultureManagementView, OnMapReadyCallback {
    ActivityAgricultureManagementBinding binding;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 101;
    UploadImage uploadImage = new UploadImage(this);
    File imageUri;


    AgricultureManagementPresenter presenter;

    SubDistrict selectedSubDistrict;
    District selectedDistrict;

    String latitude;
    String longitude;

    GoogleMap myMap;
    SupportMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAgricultureManagementBinding.inflate(getLayoutInflater());
        presenter = new AgricultureManagementPresenter(this);
        setContentView(binding.getRoot());

        binding.btnPickMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AgricultureManagement.this, LocationPicker.class);
                startActivityForResult(intent, 2);
            }
        });

        binding.btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkAndRequestPermissions(AgricultureManagement.this)){
                    uploadImage.chooseImage(AgricultureManagement.this);
                }
            }
        });

        doSave();

        filled();

        if (myMap == null) {
            mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(googleMap -> {
                myMap = googleMap;

                if(!isNew()){
                    myMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(agricultureData().getLatitude()), Double.parseDouble(agricultureData().getLongitude()))));
                    myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
                            Double.parseDouble(agricultureData().getLatitude()), Double.parseDouble(agricultureData().getLongitude())),12f)
                    );
                }
            });
        }
    }

    public void goBack() {
        binding.imageGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public static boolean checkAndRequestPermissions(final Activity context) {
        int WExtstorePermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int cameraPermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.CAMERA);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (WExtstorePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded
                    .add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(context, listPermissionsNeeded
                            .toArray(new String[listPermissionsNeeded.size()]),
                    REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS:
                if (ContextCompat.checkSelfPermission(AgricultureManagement.this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(),
                            "FlagUp Requires Access to Camara.", Toast.LENGTH_SHORT)
                            .show();

                } else if (ContextCompat.checkSelfPermission(AgricultureManagement.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(),
                            "FlagUp Requires Access to Your Storage.",
                            Toast.LENGTH_SHORT).show();

                } else {

                }
                break;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {

                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        binding.btnUpload.setImageBitmap(selectedImage);

                        Uri tempUri = getImageUri(getApplicationContext(), selectedImage);
                        File finalFile = new File(getRealPathFromURI(tempUri));

                        imageUri = finalFile;

                    }
                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {

                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();
                                String filepath = cursor.getString(0);
                                File imageFile = new File(filepath);

                                System.out.println("IMAGE " + imageFile);

                                imageUri = imageFile;
                                binding.btnUpload.setImageURI(selectedImage);

                                cursor.close();
                            }
                        }

                    }
                    break;

                case 2 :
                    if(resultCode == Activity.RESULT_OK){
                        String intentLat = data.getStringExtra("LATITUDE");
                        String intentLng = data.getStringExtra("LONGITUDE");

                        latitude = intentLat;
                        longitude = intentLng;

                        myMap.clear();
                        myMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(intentLat), Double.parseDouble(intentLng))));
                        myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
                                Double.parseDouble(intentLat), Double.parseDouble(intentLng)),12f)
                        );

                        System.out.println("LATLONG " + latitude + " " + longitude);
                    }
                    break;
            }
        }
    }

    @Override
    public void attachSpinnerSubDistrict(List<SubDistrict> sub_districts) {
        ArrayAdapter spinnerAdapter = new ArrayAdapter(this, R.layout.spinner_item, sub_districts);
        if(!isNew()){
            binding.subDistrictSpinner.setText(agricultureData().getDesa().toLowerCase());
        }
        binding.subDistrictSpinner.setAdapter(spinnerAdapter);

        binding.subDistrictSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SubDistrict item = sub_districts.get(i);
                selectedSubDistrict = item;

            }
        });
    }

    @Override
    public void attachSpinnerDistrict(List<District> districts) {
        ArrayAdapter spinnerAdapter = new ArrayAdapter(this, R.layout.spinner_item, districts);

        if(!isNew()){
            binding.districtSpinner.setText(agricultureData().getKecamatan().toLowerCase());
        }

        binding.districtSpinner.setAdapter(spinnerAdapter);

        binding.districtSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Object item = adapterView.getItemAtPosition(i);
                if(item instanceof District){
                    District district = (District) item;
                    selectedDistrict = district;
                }
            }
        });
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
    public void success() {
        finish();
    }

    public void doSave() {
        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RequestBody namapemilik = RequestBody.create(
                        MultipartBody.FORM, binding.etNamaPemilik.getText().toString().trim()
                );

                RequestBody luas = RequestBody.create(
                        MultipartBody.FORM, binding.etLuas.getText().toString().trim()
                );

                RequestBody meter = RequestBody.create(
                        MultipartBody.FORM, binding.etMeter.getText().toString().trim()
                );

                RequestBody desa = RequestBody.create(
                        MultipartBody.FORM, selectedSubDistrict != null ? selectedSubDistrict.toString() : agricultureData().getDesa()
                );

                RequestBody kecamatan = RequestBody.create(
                        MultipartBody.FORM, selectedDistrict != null ? selectedDistrict.toString() : agricultureData().getKecamatan()
                );

                RequestBody reqLatitude = RequestBody.create(
                        MultipartBody.FORM, latitude
                );

                RequestBody reqLongitude = RequestBody.create(
                        MultipartBody.FORM, longitude
                );

                File originalFile = null;
                RequestBody imagePart= null;
                MultipartBody.Part foto= null;

                if(imageUri != null){
                    originalFile = new File(imageUri.getAbsolutePath());

                    imagePart = RequestBody.create(
                            MediaType.parse("image/*"),
                            originalFile
                    );

                    foto = MultipartBody.Part.createFormData(
                            "foto",
                            originalFile.getName(),
                            imagePart
                    );
                }


                if(isNew()){
                    presenter.create(namapemilik,luas,meter,desa,kecamatan, reqLatitude,reqLongitude,foto);
                }else{
                    int idLahan = Integer.parseInt(agricultureData().getId_lahan());
                    System.out.println("ID LAHAN " + idLahan);
                    if(imageUri == null){
                        presenter.updateWithoutPhoto(idLahan, namapemilik,luas,meter,desa,kecamatan, reqLatitude, reqLongitude);
                    }else{
                        presenter.update(idLahan, namapemilik,luas,meter,desa,kecamatan, reqLatitude,reqLongitude,foto );
                    }

                }

            }
        });
    }

    public void getData(){
        presenter.fetchDistrict();
        presenter.fetchSubDistrict();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destroy();
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        String path = "";
        if (getContentResolver() != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }

    public boolean isNew(){
        return getIntent().getBooleanExtra("isNew", true);
    }

    public Agriculture agricultureData() {
        Gson gson = new Gson();
        String agricultureAsString = getIntent().getStringExtra("AGRICULTURE");
        Agriculture agriculture = gson.fromJson(agricultureAsString, Agriculture.class);

        return agriculture;
    }

    private void filled(){
        if(!isNew()){
            binding.etNamaPemilik.setText(agricultureData().getNamapemilik());
            binding.etLuas.setText(agricultureData().getLuas());
            binding.etMeter.setText(agricultureData().getMeter().split(" ")[0]);
            latitude = agricultureData().getLatitude();
            longitude = agricultureData().getLongitude();
            Glide.with(this).load(agricultureData().getFoto()).into(binding.btnUpload);
            binding.btnPickMap.setText("UBAH LOKASI");
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        myMap = googleMap;
        if(ActivityCompat.checkSelfPermission(
                AgricultureManagement.this,
                Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                AgricultureManagement.this,
                Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
        ){
            return;
        }

            myMap.isMyLocationEnabled();

    }
}