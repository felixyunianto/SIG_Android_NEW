package com.dwiky.sigpertanian.presenters;

import com.dwiky.sigpertanian.contracts.EmailContract;
import com.dwiky.sigpertanian.responses.WrappedResponse;
import com.dwiky.sigpertanian.utilities.APIClient;
import com.dwiky.sigpertanian.webservices.APIServices;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PasswordPresenter implements EmailContract.PasswordPresenter {
    private EmailContract.PasswordView view;
    private APIServices apiServices = new APIClient().apiServices();

    public PasswordPresenter(EmailContract.PasswordView v) {
        this.view = v;
    }

    @Override
    public void changePassword(String token, String password, String cPassword) {
        view.loading(true);
        apiServices.resetPassword(token, password, cPassword)
                .enqueue(new Callback<WrappedResponse<String>>() {
                    @Override
                    public void onResponse(Call<WrappedResponse<String>> call, Response<WrappedResponse<String>> response) {
                        if(response.isSuccessful()){
                            WrappedResponse body = response.body();
                            System.out.println("BODY "+ body.isStatus());
                            if(body != null && body.isStatus()){
                                view.toast("Berhasil mengganti password");
                                view.success();
                            }else{
                                view.toast("Password dan konfirmasi password tidak sama");
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
