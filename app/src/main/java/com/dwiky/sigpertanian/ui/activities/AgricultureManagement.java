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

import com.dwiky.sigpertanian.R;
import com.dwiky.sigpertanian.contracts.AgricultureContracts;
import com.dwiky.sigpertanian.databinding.ActivityAgricultureManagementBinding;
import com.dwiky.sigpertanian.models.Agriculture;
import com.dwiky.sigpertanian.models.District;
import com.dwiky.sigpertanian.models.SubDistrict;
import com.dwiky.sigpertanian.presenters.AgricultureManagementPresenter;
import com.dwiky.sigpertanian.utilities.UploadImage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

public class AgricultureManagement extends AppCompatActivity implements AgricultureContracts.AgricultureManagementView {
    ActivityAgricultureManagementBinding binding;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 101;
    UploadImage uploadImage = new UploadImage(this);
    File imageUri;

    AgricultureManagementPresenter presenter;

    SubDistrict selectedSubDistrict;
    District selectedDistrict;


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



//        binding.btnUpload.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(checkAndRequestPermissions(AgricultureManagement.this)){
//                    uploadImage.chooseImage(AgricultureManagement.this);
//                }
//            }
//        });

        doSave();
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
//                        binding.imageView.setImageBitmap(selectedImage);
                    }
                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        System.out.println("DATA"+ data);
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();
                                String filepath = cursor.getString(0);
                                File imageFile = new File(filepath);

                                System.out.println("IMAGE " + imageFile);

                                imageUri = imageFile;
//                                binding.imageView.setImageURI(selectedImage);

                                cursor.close();
                            }
                        }

                    }
                    break;
            }
        }
    }

    @Override
    public void attachSpinnerSubDistrict(List<SubDistrict> sub_districts) {
        ArrayAdapter spinnerAdapter = new ArrayAdapter(this, R.layout.spinner_item, sub_districts);
//        if(!isNew()){
//            binding.subDistrictSpinner.setText(comodityData().getDesa().toLowerCase());
//        }
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

//        if(!isNew()){
//            for (int i = 0; i < districts.size(); i++){
//                if(districts.get(i).getKecamatan() == comodityData().getKecamatan().toLowerCase()){
//                    selectedDistrict = districts.get(i);
//                }
//            }
//            binding.districtSpinner.setText(comodityData().getKecamatan().toLowerCase());
//        }

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
                        MultipartBody.FORM, "Dwiky"
                );

                RequestBody luas = RequestBody.create(
                        MultipartBody.FORM, "200"
                );

                RequestBody meter = RequestBody.create(
                        MultipartBody.FORM, "200 m2"
                );

                RequestBody desa = RequestBody.create(
                        MultipartBody.FORM, "Desa"
                );

                RequestBody kecamatan = RequestBody.create(
                        MultipartBody.FORM, "Kecamatan"
                );

                RequestBody latitude = RequestBody.create(
                        MultipartBody.FORM, "123456"
                );

                RequestBody longitude = RequestBody.create(
                        MultipartBody.FORM, "123456"
                );

                File originalFile = new File(imageUri.getPath());


                RequestBody imagePart = RequestBody.create(
                        MediaType.parse("image/*"),
                        originalFile
                );

                MultipartBody.Part foto = MultipartBody.Part.createFormData(
                        "foto",
                        originalFile.getName(),
                        imagePart
                );

                presenter.create(namapemilik,luas,meter,desa,kecamatan, latitude,longitude,foto);

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

}