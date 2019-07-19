package com.dataflow.deliverytalk.util;

import android.content.SharedPreferences;

public class AppDataControlService {
    SharedPreferences appData;
    SharedPreferences.Editor edit;

    public AppDataControlService(SharedPreferences appData){
        this.appData = appData;
        this.edit = appData.edit();
    }

    public boolean getPushFlag(){
        return appData.getBoolean("pushFlag", false);
    }

    public boolean getSmsFlag(){
        return appData.getBoolean("smsFlag", false);
    }

    public void changeFlag(String key, boolean value){
        edit.putBoolean(key, value);
        edit.apply();
    }

    public String getPhoneNumber(){
        return appData.getString("phoneNumber","정보없음");
    }

    public void setPhoneNumber(String phoneNumber){
        edit.putString("phoneNumber",phoneNumber);
        edit.apply();
    }

}
