package com.dwiky.sigpertanian.contracts;

import com.dwiky.sigpertanian.models.Comoditas;
import com.dwiky.sigpertanian.models.District;
import com.dwiky.sigpertanian.models.SubDistrict;

import java.util.List;

public interface ComodityContracts {
    interface ComodityView{
        void attachToRecyclerView(List<Comoditas> comodities);
        void toast(String message);
        boolean loading(boolean loading);
    }

    interface ComodityPresenter{
        void fetchComodity();
        void delete(String id_komoditas);
        void destroy();
    }

    interface ComodityManagementView {
        void attachSpinnerSubDistrict(List<SubDistrict> sub_districts);
        void attachSpinnerDistrict(List<District> districts);
        void toast(String message);
        boolean loading(boolean loading);
        void success();
    }

    interface ComodityManagementPresenter{
        void fetchSubDistrict();
        void fetchDistrict();
        void create(String namakomoditas, String jumlah, String awal, String akhir, String desa, String kecamatan);
        void update(String id, String namakomoditas, String jumlah, String awal, String akhir, String desa, String kecamatan);
        void destroy();
    }
}
