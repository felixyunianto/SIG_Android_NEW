package com.dwiky.sigpertanian.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.dwiky.sigpertanian.R;
import com.dwiky.sigpertanian.contracts.ComodityContracts;
import com.dwiky.sigpertanian.databinding.ActivityComodityManagementBinding;
import com.dwiky.sigpertanian.models.Comoditas;
import com.dwiky.sigpertanian.models.District;
import com.dwiky.sigpertanian.models.SubDistrict;
import com.dwiky.sigpertanian.presenters.ComodityManagementPresenter;
import com.dwiky.sigpertanian.utilities.APIClient;
import com.dwiky.sigpertanian.utilities.Constants;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ComodityManagementActivity extends AppCompatActivity implements ComodityContracts.ComodityManagementView{
    ActivityComodityManagementBinding binding;
    ComodityManagementPresenter presenter;
    SubDistrict selectedSubDistrict;
    District selectedDistrict;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityComodityManagementBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        presenter = new ComodityManagementPresenter(this);
        filled();
        goBack();
        binding.etAwal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openStartDatePickerDialog(binding.etAwal);
            }
        });

        binding.etAkhir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openStartDatePickerDialog(binding.etAkhir);
            }
        });

        doSave();
        if(isNew())binding.inSubDistrictSpinner.setEnabled(false);
        else binding.inSubDistrictSpinner.setEnabled(true);
    }

    public void goBack() {
        binding.imageGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public boolean isNew(){return getIntent().getBooleanExtra("isNew", true);}
    public Comoditas comodityData(){
        Gson gson = new Gson();
        String comodityAsString = getIntent().getStringExtra("COMODITY");
        Comoditas comodity = gson.fromJson(comodityAsString, Comoditas.class);

        return comodity;
    }

    private void filled(){
        if(isNew()){
            binding.titleToolbar.setText("Tambah Komoditas");
        }else{
            binding.titleToolbar.setText("Edit Komoditas");
            binding.etNamaKomoditas.setText(comodityData().getNamakomoditas());
            binding.etJumlah.setText(comodityData().getJumlah());
            binding.etAwal.setText(comodityData().getAwal());
            binding.etAkhir.setText(comodityData().getAkhir());

        }
    }

    public void openStartDatePickerDialog(EditText editText){
        Calendar newCalendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                editText.setText(formatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    public void getData(){
        presenter.fetchDistrict();
        if(!isNew()){
            presenter.fetchSubDistrictAll();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    @Override
    public void attachSpinnerSubDistrict(List<SubDistrict> sub_districts) {
        ArrayAdapter spinnerAdapter = new ArrayAdapter(this, R.layout.spinner_item, sub_districts);
        if(!isNew()){
            binding.subDistrictSpinner.setText(comodityData().getDesa().toLowerCase());
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
            for (int i = 0; i < districts.size(); i++){
                if(districts.get(i).getKecamatan() == comodityData().getKecamatan().toLowerCase()){
                    selectedDistrict = districts.get(i);
                }
            }
            binding.districtSpinner.setText(comodityData().getKecamatan().toLowerCase());
        }

        binding.districtSpinner.setAdapter(spinnerAdapter);

        binding.districtSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Object item = adapterView.getItemAtPosition(i);
                if(item instanceof District){
                    District district = (District) item;
                    selectedDistrict = district;

                    presenter.fetchSubDistrict(district.getId());
                    binding.inSubDistrictSpinner.setEnabled(true);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destroy();
    }

    public boolean validNamaKomoditas() {
        if(binding.etNamaKomoditas.getText().toString().trim().isEmpty()){
            binding.inNamaKomoditas.setHelperText("Form ini harus disi");
            binding.inNamaKomoditas.setHelperTextColor(
                    ColorStateList.valueOf(Color.RED)
            );

            return false;
        }

        return true;
    }

    public boolean validNamaJumlah() {
        if(binding.etJumlah.getText().toString().trim().isEmpty()){
            binding.inJumlah.setHelperText("Form ini harus disi");
            binding.inJumlah.setHelperTextColor(
                    ColorStateList.valueOf(Color.RED)
            );

            return false;
        }

        return true;
    }

    public boolean validAwal() {
        if(binding.etAwal.getText().toString().trim().isEmpty()){
            binding.inAwal.setHelperText("Form ini harus disi");
            binding.inAwal.setHelperTextColor(
                    ColorStateList.valueOf(Color.RED)
            );

            return false;
        }

        return true;
    }

    public boolean validAkhir() {
        if(binding.etAkhir.getText().toString().trim().isEmpty()){
            binding.inAkhir.setHelperText("Form ini harus disi");
            binding.inAkhir.setHelperTextColor(
                    ColorStateList.valueOf(Color.RED)
            );

            return false;
        }

        return true;
    }


    public boolean validDesa() {
        if(isNew()){
            if(selectedSubDistrict == null){
                binding.inSubDistrictSpinner.setHelperText("Pilih salah satu Desa");
                binding.inSubDistrictSpinner.setHelperTextColor(
                        ColorStateList.valueOf(Color.RED)
                );

                return false;
            }
        }

        return true;
    }

    public boolean validKecamatan() {
        if(isNew()){
            if(selectedDistrict == null){
                binding.inDistrictSpinner.setHelperText("Pilih salah satu Kecamatan");
                binding.inDistrictSpinner.setHelperTextColor(
                        ColorStateList.valueOf(Color.RED)
                );

                return false;
            }
        }

        return true;
    }



    private void doSave(){
        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validNamaKomoditas();
                validNamaJumlah();
                validAwal();
                validAkhir();
                validDesa();
                validKecamatan();

                if(validKecamatan() && validNamaJumlah() && validAwal() && validAkhir() && validDesa() && validKecamatan() ){
                    String namakomoditas = binding.etNamaKomoditas.getText().toString().trim();
                    String jumlah = binding.etJumlah.getText().toString().trim();
                    String awal = Constants.converDateToSave(binding.etAwal.getText().toString().trim());
                    String akhir = Constants.converDateToSave(binding.etAkhir.getText().toString().trim());
                    String desa = selectedSubDistrict != null ? selectedSubDistrict.toString() : comodityData().getDesa();
                    String kecamatan = selectedDistrict != null ? selectedDistrict.toString() : comodityData().getKecamatan();

                    System.out.println("AWAL "+ awal);
                    System.out.println("AKHIR "+ akhir);

                    if(isNew()){
                        presenter.create(namakomoditas, jumlah, awal, akhir, desa, kecamatan);
                    }else{
                        presenter.update(comodityData().id_komoditas, namakomoditas, jumlah,awal, akhir,desa, kecamatan);
                    }
                }

            }
        });


    }
}