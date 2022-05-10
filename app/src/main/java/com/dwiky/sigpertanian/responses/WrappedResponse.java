package com.dwiky.sigpertanian.responses;

import com.google.gson.annotations.SerializedName;

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
        this.data = data;
    }

    @SerializedName("error") private boolean error;
    @SerializedName("pesan") private String pesan;
    @SerializedName("data") private T data;
}
