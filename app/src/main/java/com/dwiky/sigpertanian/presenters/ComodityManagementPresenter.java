package com.dwiky.sigpertanian.presenters;

import android.util.Log;

import com.dwiky.sigpertanian.contracts.ComodityContracts;
import com.dwiky.sigpertanian.models.Comoditas;
import com.dwiky.sigpertanian.models.District;
import com.dwiky.sigpertanian.models.SubDistrict;
import com.dwiky.sigpertanian.responses.WrappedListResponse;
import com.dwiky.sigpertanian.responses.WrappedResponse;
import com.dwiky.sigpertanian.utilities.APIClient;
import com.dwiky.sigpertanian.webservices.APIServices;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ComodityManagementPresenter implements ComodityContracts.ComodityManagementPresenter {
    private ComodityContracts.ComodityManagementView view = null;
    private APIServices apiServices = new APIClient().apiServices();

    public ComodityManagementPresenter(ComodityContracts.ComodityManagementView v){
        this.view = v;
    }

    @Override
    public void fetchSubDistrict(String id_kecamatan) {
        view.loading(true);
        apiServices.fetchSubDistrict(id_kecamatan)
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
    public void fetchSubDistrictAll() {
        view.loading(true);
        apiServices.fetchSubDistrictAll()
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
    public void create(String namakomoditas, String jumlah, String awal, String akhir, String desa, String kecamatan) {
        view.loading(true);
        apiServices.createComodity(namakomoditas, jumlah, awal, akhir, desa, kecamatan)
                .enqueue(new Callback<WrappedResponse<Comoditas>>() {
                    @Override
                    public void onResponse(Call<WrappedResponse<Comoditas>> call, Response<WrappedResponse<Comoditas>> response) {
                        System.out.println("SUCCESS " + response.body());
                        if(response.isSuccessful()){
                            WrappedResponse body = response.body();
                            if(body != null){
                                view.toast(body.getPesan());
                                view.success();
                            }else{
                                view.toast(body.getPesan());
                            }
                        }else{
                            System.out.println("RESPONSE " + response.message());
                            view.toast(response.message());
                        }
                        view.loading(false);
                    }

                    @Override
                    public void onFailure(Call<WrappedResponse<Comoditas>> call, Throwable t) {
                        view.toast("Terjadi Kesalahan");
                        Log.d("MESSAGE", t.getMessage());
                        view.loading(false);
                    }
                });
    }

    @Override
    public void update(String id, String namakomoditas, String jumlah, String awal, String akhir, String desa, String kecamatan) {
        view.loading(true);
        apiServices.updateComodity(id, namakomoditas, jumlah, awal, akhir, desa, kecamatan)
                .enqueue(new Callback<WrappedResponse<Comoditas>>() {
                    @Override
                    public void onResponse(Call<WrappedResponse<Comoditas>> call, Response<WrappedResponse<Comoditas>> response) {
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
                    public void onFailure(Call<WrappedResponse<Comoditas>> call, Throwable t) {
                        view.toast("Terjadi Kesalahan");
                        Log.d("MESSAGE", t.getMessage());
                        view.loading(false);
                    }
                });
    }

    @Override
    public void destroy() {
        view = null;
    }
}
