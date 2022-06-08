package com.dwiky.sigpertanian.presenters;

import com.dwiky.sigpertanian.contracts.ComodityContracts;
import com.dwiky.sigpertanian.contracts.LoginContract;
import com.dwiky.sigpertanian.models.User;
import com.dwiky.sigpertanian.responses.WrappedListResponse;
import com.dwiky.sigpertanian.responses.WrappedResponse;
import com.dwiky.sigpertanian.utilities.APIClient;
import com.dwiky.sigpertanian.webservices.APIServices;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginPresenter implements LoginContract.LoginPresenter {
    private LoginContract.LoginView view;
    private APIServices apiService = new APIClient().apiServicesAuth();

    public LoginPresenter(LoginContract.LoginView v){
        this.view = v;
    }

    @Override
    public void login(String username, String password) {
        view.loading(true);
        apiService.login(username, password)
                .enqueue(new Callback<WrappedResponse<User>>() {
                    @Override
                    public void onResponse(Call<WrappedResponse<User>> call, Response<WrappedResponse<User>> response) {
                        if(response.isSuccessful()){
                            WrappedResponse body = response.body();
                            if(body != null){
                                if(body.isStatus()){
                                    view.toast(body.getPesan());
                                    view.success((User) body.getData());
                                }else{
                                    view.toast(body.getMessage());
                                }
                            }else{
                                System.out.println(body);
                                view.toast("Error");
                            }
                        }
                        view.loading(false);
                    }

                    @Override
                    public void onFailure(Call<WrappedResponse<User>> call, Throwable t) {
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
