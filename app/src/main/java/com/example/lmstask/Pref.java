package com.example.lmstask;

import android.content.Context;
import android.content.SharedPreferences;




public class Pref {
    private SharedPreferences preferences;
    private Context context;

    public Pref(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    }



    public void setPass(String pass) {
        preferences.edit().putString("pass", pass).apply();

    }

    public void setUserName(String userName) {
        preferences.edit().putString("username", userName).apply();

    }

    public void setToken(String token) {
        preferences.edit().putString("token", token).apply();

    }

    public void setAccountId(String accountid) {
        preferences.edit().putString("accountid", accountid).apply();

    }

    public void removePass() {
        preferences.edit().remove("pass").apply();

    }

    public void removeUsername() {
        preferences.edit().remove("username").apply();

    }

    public String getUserName() {
        return preferences.getString("username", "");
    }

    public String getPass() {
        return preferences.getString("pass", "");
    }

    public String getToken(){
        return preferences.getString("token","");
    }

    public String getAccountId(){
        return preferences.getString("accountid","");
    }



}
