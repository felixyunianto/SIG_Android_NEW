package com.dwiky.sigpertanian.presenters;
import android.util.Log;

import com.dwiky.sigpertanian.contracts.ComodityContracts;
import com.dwiky.sigpertanian.models.Comoditas;
import com.dwiky.sigpertanian.responses.WrappedListResponse;
import com.dwiky.sigpertanian.responses.WrappedResponse;
import com.dwiky.sigpertanian.utilities.APIClient;
import com.dwiky.sigpertanian.webservices.APIServices;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ComodityPresenter implements ComodityContracts.ComodityPresenter {
    private ComodityContracts.ComodityView view;
    private APIServices apiService = new APIClient().apiServices();

    public ComodityPresenter(ComodityContracts.ComodityView v){
        this.view = v;
    }

    @Override
    public void fetchComodity() {
        view.loading(true);
        apiService.fetchComodity()
                .enqueue(new Callback<WrappedListResponse<Comoditas>>() {
                    @Override
                    public void onResponse(Call<WrappedListResponse<Comoditas>> call, Response<WrappedListResponse<Comoditas>> response) {
                        if(response.isSuccessful()){
                            WrappedListResponse body = response.body();
                            if(body != null){
                                view.toast(body.getPesan());
                                view.attachToRecyclerView(body.getData());
                            }else{
                                view.toast("Error");
                            }
                        }
                        view.loading(false);
                    }

                    @Override
                    public void onFailure(Call<WrappedListResponse<Comoditas>> call, Throwable t) {
                        System.out.println("Terjadi kesalahan " + t.getMessage());
                        view.loading(false);
                    }
                });
    }

    @Override
    public void delete(String id_komoditas) {
        view.loading(true);
        apiService.deleteComodity(id_komoditas)
                .enqueue(new Callback<WrappedResponse<Comoditas>>() {
                    @Override
                    public void onResponse(Call<WrappedResponse<Comoditas>> call, Response<WrappedResponse<Comoditas>> response) {
                        if (response.isSuccessful()){
                            WrappedResponse body = response.body();
                            if(body != null){
                                view.toast(body.getPesan());
                                fetchComodity();
                            }
                        }
                        view.loading(false);
                    }

                    @Override
                    public void onFailure(Call<WrappedResponse<Comoditas>> call, Throwable t) {
                        Log.d("MESSAGE", t.getMessage());
                        view.toast("Terjadi Kesalahan");
                        view.loading(false);
                    }
                });
    }

    @Override
    public void destroy() {
        this.view = null;
    }
}
