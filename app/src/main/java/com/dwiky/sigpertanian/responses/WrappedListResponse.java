package com.dwiky.sigpertanian.responses;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class  WrappedListResponse <T> {
    @SerializedName("error") private boolean error;
    @SerializedName("pesan") private String pesan;
    @SerializedName("data") private List<T> data = new ArrayList<>();

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

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public WrappedListResponse() {
    }



}

