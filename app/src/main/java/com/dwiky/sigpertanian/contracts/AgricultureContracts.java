package com.dwiky.sigpertanian.contracts;

import com.dwiky.sigpertanian.models.Agriculture;
import com.dwiky.sigpertanian.models.District;
import com.dwiky.sigpertanian.models.SubDistrict;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public interface AgricultureContracts {
    interface AgricultureView{
        void attachToRecyclerView(List<Agriculture> agricultures);
        void attachDetailView(Agriculture agriculture);
        void toast(String message);
        boolean loading(boolean loading);
    }

    interface AgriculturePresenter{
        void fetchAgriculture();
        void fetchDetail(String id);
        void delete(String id_lahan);
        void destroy();
    }

    interface AgricultureManagementView {
        void attachSpinnerSubDistrict(List<SubDistrict> sub_districts);
        void attachSpinnerDistrict(List<District> districts);
        void toast(String message);
        boolean loading(boolean loading);
        void success();
    }

    interface AgricultureManagementPresenter{
        void fetchSubDistrict();
        void fetchDistrict();
        void create(RequestBody namapemilik, RequestBody luas, RequestBody meter, RequestBody desa, RequestBody kecamatan, RequestBody latitude, RequestBody longitude, MultipartBody.Part foto);

        void update(int id, RequestBody namapemilik, RequestBody luas, RequestBody meter, RequestBody desa, RequestBody kecamatan, RequestBody latitude, RequestBody longitude, MultipartBody.Part foto);
        void updateWithoutPhoto(int id, RequestBody namapemilik, RequestBody luas, RequestBody meter, RequestBody desa, RequestBody kecamatan, RequestBody latitude, RequestBody longitude);
        void destroy();
    }
}
