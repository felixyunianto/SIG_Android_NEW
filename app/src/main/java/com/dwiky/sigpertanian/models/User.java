package com.dwiky.sigpertanian.models;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("id") private String id;
    @SerializedName("username") private String username;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @SerializedName("fullname") private String fullname;
    @SerializedName("email") private String email;
}
