package com.dwiky.sigpertanian.presenters;

import com.dwiky.sigpertanian.contracts.EmailContract;
import com.dwiky.sigpertanian.responses.WrappedResponse;
import com.dwiky.sigpertanian.utilities.APIClient;
import com.dwiky.sigpertanian.webservices.APIServices;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmailPresenter implements EmailContract.EmailPresenter {
    private EmailContract.EmailView view;
    private APIServices apiService = new APIClient().apiServices();

    public EmailPresenter(EmailContract.EmailView v)
    {
        this.view = v;
    }

    @Override
    public void sendEmail(String email) {
        view.loading(true);
        apiService.sendEmail(email)
                .enqueue(new Callback<WrappedResponse<String>>() {
                    @Override
                    public void onResponse(Call<WrappedResponse<String>> call, Response<WrappedResponse<String>> response) {
                        System.out.println(response);
                        if(response.isSuccessful()){
                            view.success();
                            view.loading(false);
                            view.toast("Code telah dikirim email, harap cek email anda");
                        }
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
        this.view = null;
    }
}
