package com.example.sweather.db;

import com.example.sweather.gson.Update;

public class UpdateFreq {
    private static int update_freq;

    public static int getUpdate_freq(){
        return update_freq;
    }

    public static void setUpdate_freq(int a){
        UpdateFreq.update_freq=a;
    }
}
