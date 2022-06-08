package com.dwiky.sigpertanian.responses;

import com.google.gson.annotations.SerializedName;

import kotlinx.coroutines.flow.internal.AbortFlowException;

public class WrappedResponse<T> {
    public WrappedResponse(){}

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getPesan() {
        return pesan;
    }

    public void setPesan(String pesan) {
        this.pesan = pesan;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        if(data.getClass().getName() == "String"){
            return;
        }
        this.data = data;
    }

    @SerializedName("error") private boolean error;
    @SerializedName("pesan") private String pesan;
    @SerializedName("data") private T data = null;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @SerializedName("status") private boolean status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @SerializedName("message") private String message;
}
