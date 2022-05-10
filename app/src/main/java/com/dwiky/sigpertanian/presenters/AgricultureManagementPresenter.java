package com.dwiky.sigpertanian.presenters;

import com.dwiky.sigpertanian.contracts.AgricultureContracts;
import com.dwiky.sigpertanian.models.Agriculture;
import com.dwiky.sigpertanian.models.District;
import com.dwiky.sigpertanian.models.SubDistrict;
import com.dwiky.sigpertanian.responses.WrappedListResponse;
import com.dwiky.sigpertanian.responses.WrappedResponse;
import com.dwiky.sigpertanian.utilities.APIClient;
import com.dwiky.sigpertanian.webservices.APIServices;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AgricultureManagementPresenter implements AgricultureContracts.AgricultureManagementPresenter {
    private AgricultureContracts.AgricultureManagementView view;
    private APIServices apiServices = new APIClient().apiServices();

    public AgricultureManagementPresenter(AgricultureContracts.AgricultureManagementView v) {
        this.view = v;
    }

    @Override
    public void fetchSubDistrict() {
        view.loading(true);
        apiServices.fetchSubDistrict()
                .enqueue(new Callback<WrappedListResponse<SubDistrict>>() {
                    @Override
                    public void onResponse(Call<WrappedListResponse<SubDistrict>> call, Response<WrappedListResponse<SubDistrict>> response) {
                        if(response.isSuccessful()){
                            WrappedListResponse body = response.body();
                            if(body != null){
                                view.toast(body.getPesan());
                                view.attachSpinnerSubDistrict(body.getData());
                            }else{
                                view.toast("Error");
                            }
                        }
                        view.loading(false);
                    }

                    @Override
                    public void onFailure(Call<WrappedListResponse<SubDistrict>> call, Throwable t) {
                        System.out.println("Terjadi kesalahan " + t.getMessage());
                        view.loading(false);

                    }
                });
    }

    @Override
    public void fetchDistrict() {
        apiServices.fetchDistrict()
                .enqueue(new Callback<WrappedListResponse<District>>() {
                    @Override
                    public void onResponse(Call<WrappedListResponse<District>> call, Response<WrappedListResponse<District>> response) {
                        if(response.isSuccessful()){
                            WrappedListResponse body = response.body();
                            if(body != null){
                                view.toast(body.getPesan());
                                view.attachSpinnerDistrict(body.getData());
                            }else{
                                view.toast("Error");
                            }
                        }
                        view.loading(false);
                    }

                    @Override
                    public void onFailure(Call<WrappedListResponse<District>> call, Throwable t) {
                        System.out.println("Terjadi kesalahan " + t.getMessage());
                        view.loading(false);
                    }
                });
    }

    @Override
    public void create(RequestBody namapemilik, RequestBody luas, RequestBody meter, RequestBody desa, RequestBody kecamatan, RequestBody latitude, RequestBody longitude, MultipartBody.Part foto) {
        view.loading(true);
        apiServices.createAgriculture(namapemilik, luas, meter, desa, kecamatan,latitude, longitude, foto)
                .enqueue(new Callback<WrappedResponse<Agriculture>>() {
                    @Override
                    public void onResponse(Call<WrappedResponse<Agriculture>> call, Response<WrappedResponse<Agriculture>> response) {
                        if(response.isSuccessful()){
                            WrappedResponse body = response.body();
                            if(body != null){
                                view.toast(body.getPesan());
                                view.success();
                            }else{
                                view.toast(body.getPesan());
                            }
                        }else{
                            view.toast(response.message());
                        }
                        view.loading(false);
                    }

                    @Override
                    public void onFailure(Call<WrappedResponse<Agriculture>> call, Throwable t) {
                        System.out.println("Terjadi kesalahan " + t.getMessage());
                        view.loading(false);
                    }
                });
    }

    @Override
    public void update(String id, RequestBody namapemilik, RequestBody luas, RequestBody meter, RequestBody desa, RequestBody kecamatan, RequestBody latitude, RequestBody longitude, MultipartBody.Part foto) {

    }

    @Override
    public void destroy() {

    }
}
