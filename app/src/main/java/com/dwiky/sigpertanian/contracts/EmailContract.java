package com.dwiky.sigpertanian.contracts;

public interface EmailContract {
    interface EmailView {
        void success();
        void loading(boolean loading);
        void toast(String message);
    }

    interface EmailPresenter{
        void sendEmail(String email);
        void destroy();
    }

    interface PasswordView{
        void success();
        void loading(boolean loading);
        void toast(String message);
    }

    interface PasswordPresenter{
        void changePassword(String token, String password, String cPassword);
        void destroy();
    }

    interface CodeView{
        void success(String token);
        void loading(boolean loading);
        void toast(String message);
    }

    interface CodePresenter{
        void sendToken(String token);
        void destroy();
    }
}
