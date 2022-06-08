package com.dwiky.sigpertanian.contracts;

import com.dwiky.sigpertanian.models.Comoditas;
import com.dwiky.sigpertanian.models.User;

import java.util.List;

public interface LoginContract {
    interface LoginView{
        void toast(String message);
        boolean loading(boolean loading);
        void success(User user);
    }

    interface LoginPresenter{
        void login(String username, String password);
        void destroy();
    }
}
