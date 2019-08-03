package com.example.sweather.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class HttpUtil2 {
    public static void sendOkHttpRequest(String address,okhttp3.Callback callback){
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }
}
