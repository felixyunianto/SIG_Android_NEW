package com.dwiky.sigpertanian.presenters;

import com.dwiky.sigpertanian.contracts.AgricultureContracts;
import com.dwiky.sigpertanian.models.Agriculture;
import com.dwiky.sigpertanian.responses.WrappedListResponse;
import com.dwiky.sigpertanian.utilities.APIClient;
import com.dwiky.sigpertanian.webservices.APIServices;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AgriculturePresenter implements AgricultureContracts.AgriculturePresenter {
    private AgricultureContracts.AgricultureView view;
    private APIServices apiServices = new APIClient().apiServices();

    public AgriculturePresenter(AgricultureContracts.AgricultureView v) {
        this.view = v;
    }

    @Override
    public void fetchAgriculture() {
        view.loading(true);
        apiServices.fetchAgriculture(null)
                .enqueue(new Callback<WrappedListResponse<Agriculture>>() {
                    @Override
                    public void onResponse(Call<WrappedListResponse<Agriculture>> call, Response<WrappedListResponse<Agriculture>> response) {
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
                    public void onFailure(Call<WrappedListResponse<Agriculture>> call, Throwable t) {
                        System.out.println("Terjadi kesalahan " + t.getMessage());
                        view.loading(false);
                    }
                });
    }

    @Override
    public void fetchDetail(String id) {
        view.loading(true);
        apiServices.fetchAgriculture(id)
                .enqueue(new Callback<WrappedListResponse<Agriculture>>() {
                    @Override
                    public void onResponse(Call<WrappedListResponse<Agriculture>> call, Response<WrappedListResponse<Agriculture>> response) {
                        if(response.isSuccessful()){
                            WrappedListResponse body = response.body();
                            if(body != null){
                                view.toast(body.getPesan());
                                view.attachDetailView((Agriculture) body.getData().get(0));
                            }else{
                                view.toast("Error");
                            }
                        }
                        view.loading(false);
                    }

                    @Override
                    public void onFailure(Call<WrappedListResponse<Agriculture>> call, Throwable t) {
                        System.out.println("Terjadi kesalahan " + t.getMessage());
                        view.loading(false);
                    }
                });
    }

    @Override
    public void delete(String id_lahan) {

    }

    @Override
    public void destroy() {
        this.view = null;
    }
}
