package com.dwiky.sigpertanian.presenters;

import com.dwiky.sigpertanian.contracts.EmailContract;
import com.dwiky.sigpertanian.responses.WrappedResponse;
import com.dwiky.sigpertanian.utilities.APIClient;
import com.dwiky.sigpertanian.webservices.APIServices;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CodePresenter implements EmailContract.CodePresenter {
    private EmailContract.CodeView view;
    private APIServices apiServices = new APIClient().apiServices();

    public CodePresenter(EmailContract.CodeView v){
        this.view = v;
    }

    @Override
    public void sendToken(String token) {
        view.loading(true);
        apiServices.validateToken(token)
                .enqueue(new Callback<WrappedResponse<String>>() {
                    @Override
                    public void onResponse(Call<WrappedResponse<String>> call, Response<WrappedResponse<String>> response) {
                        if(response.isSuccessful()){
                            WrappedResponse body = response.body();
                            if(body != null && body.isStatus()){
                                view.toast(body.getMessage());
                                view.success(token);
                            }else{
                                view.toast(body.getMessage());
                            }
                        }
                        view.loading(false);
                    }

                    @Override
                    public void onFailure(Call<WrappedResponse<String>> call, Throwable t) {
                        System.out.println("Terjadi kesalahan " + t.getMessage());
                        view.loading(false);
                    }
                });
    }

    @Override
    public void destroy() {
        view = null;
    }


}
