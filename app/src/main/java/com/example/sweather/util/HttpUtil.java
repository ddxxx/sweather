package com.example.sweather.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class HttpUtil {
    public static void sendOkHttpRequest(String address,okhttp3.Callback callback,
                                         String address2,okhttp3.Callback callback2,
                                         String address3,okhttp3.Callback callback3,
                                         String address4,okhttp3.Callback callback4){
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);

        OkHttpClient client2=new OkHttpClient();
        Request request2=new Request.Builder().url(address2).build();
        client2.newCall(request2).enqueue(callback2);

        OkHttpClient client3=new OkHttpClient();
        Request request3=new Request.Builder().url(address3).build();
        client.newCall(request3).enqueue(callback3);

        OkHttpClient client4=new OkHttpClient();
        Request request4=new Request.Builder().url(address4).build();
        client2.newCall(request4).enqueue(callback4);
    }
}
