package com.dwiky.sigpertanian.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Storage {
    public String getToken (Context context){
        return getSharedPreference(context).getString("USER",null);
    }

    public void setToken(Context context, String token){
        SharedPreferences.Editor editor =
                getSharedPreference(context).edit();
        editor.putString("USER", token);
        editor.apply();
    }

    public void clearToken(Context context){
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.remove("USER");
        editor.apply();
    }

    public String getCode (Context context){
        return getSharedPreference(context).getString("CODE",null);
    }

    public void setCode(Context context, String token){
        SharedPreferences.Editor editor =
                getSharedPreference(context).edit();
        editor.putString("CODE", token);
        editor.apply();
    }

    public void clearCode(Context context){
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.remove("CODE");
        editor.apply();
    }

    public static SharedPreferences getSharedPreference(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

}
