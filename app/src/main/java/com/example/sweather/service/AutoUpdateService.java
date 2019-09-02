package com.example.sweather.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;

import com.example.sweather.gson.Weather;
import com.example.sweather.gson.WeatherAqi;
import com.example.sweather.gson.WeatherFore;
import com.example.sweather.gson.WeatherLife;
import com.example.sweather.util.HttpUtil;
import com.example.sweather.util.HttpUtil2;
import com.example.sweather.util.Utility;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AutoUpdateService extends Service {


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public int onStartCommand(Intent intent,int flags,int startId){
        updateWeather();//更新天气
        updateBingPic();//更新背景图
        AlarmManager manager=(AlarmManager)getSystemService(ALARM_SERVICE);

        int update_freq=4;
        SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(this);
        if(prefs.getString("update_freq",null)!=null){
            String inte=prefs.getString("update_freq",null);
            update_freq=Integer.valueOf(inte);
        }
        else{//默认更新频率为4小时，传入
            SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit();
            editor.putString("update_freq","4");
            editor.apply();
        }
        int anHour= update_freq*60*60*1000;//默认每4小时更新
        //从设置自动更新频率中传入参数
        long triggerAtTime= SystemClock.elapsedRealtime()+anHour;
        Intent i=new Intent(this,AutoUpdateService.class);
        PendingIntent pi=PendingIntent.getService(this,0,i,0);
        manager.cancel(pi);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pi);
        return super.onStartCommand(intent,flags,startId);
    }
    //更新天气信息

    private void updateWeather() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = prefs.getString("weather", null);

        SharedPreferences prefs2= PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString2=prefs2.getString("weatherAqi",null);

        SharedPreferences prefs3= PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString3=prefs3.getString("weatherFore",null);

        SharedPreferences prefs4= PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString4=prefs4.getString("weatherLife",null);
        if (weatherString != null) {
            // 有缓存时直接解析天气数据
            Weather weather = Utility.handleWeatherResponse(weatherString);
            WeatherAqi weatherAqi = Utility.handleWeatherAqiResponse(weatherString2);
            final WeatherFore weatherFore = Utility.handleWeatherForeResponse(weatherString3);
            WeatherLife weatherLife = Utility.handleWeatherLifeResponse(weatherString4);
            String weatherId = weather.basic.weatherId;

            String nowWeatherUrl="https://free-api.heweather.net/s6/weather/now?location=" +
                    weatherId+"&key=2c1ed2821caf4572a05c26380bdf5863";//拼装出接口地址
            String weatherAqiUrl="https://free-api.heweather.net/s6/air/now?location="+
                    weatherId+"&key=2c1ed2821caf4572a05c26380bdf5863";
            String weatherForeUrl="https://free-api.heweather.net/s6/weather/forecast?location="+
                    weatherId+"&key=2c1ed2821caf4572a05c26380bdf5863";
            String weatherLifeUrl="https://free-api.heweather.net/s6/weather/lifestyle?location="+
                    weatherId+"&key=2c1ed2821caf4572a05c26380bdf5863";

            HttpUtil.sendOkHttpRequest(nowWeatherUrl, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseText = response.body().string();//服务器返回的内容
                    Weather weather = Utility.handleWeatherResponse(responseText);//JSON
                    if (weather != null && "ok".equals(weather.status)) {
                        SharedPreferences.Editor editor =
                                PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit();
                        editor.putString("weather", responseText);
                        editor.apply();
                    }
                }
            },weatherAqiUrl, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseText = response.body().string();//服务器返回的内容
                    WeatherAqi weatherAqi = Utility.handleWeatherAqiResponse(responseText);//JSON
                    if (weatherAqi != null && "ok".equals(weatherAqi.status)) {
                        SharedPreferences.Editor editor =
                                PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit();
                        editor.putString("weatherAqi", responseText);
                        editor.apply();
                    }
                }
            },weatherForeUrl, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseText = response.body().string();//服务器返回的内容
                    WeatherFore weatherFore1 = Utility.handleWeatherForeResponse(responseText);//JSON
                    if (weatherFore != null && "ok".equals(weatherFore.status)) {
                        SharedPreferences.Editor editor =
                                PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit();
                        editor.putString("weatherFore", responseText);
                        editor.apply();
                    }
                }
            },weatherLifeUrl, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseText = response.body().string();//服务器返回的内容
                    WeatherLife weatherLife = Utility.handleWeatherLifeResponse(responseText);//JSON
                    if (weatherLife != null && "ok".equals(weatherLife.status)) {
                        SharedPreferences.Editor editor =
                                PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit();
                        editor.putString("weatherLife", responseText);
                        editor.apply();
                    }
                }
            });
        }
    }

    //更新必应每日一图
    private void updateBingPic(){
        String requestBingPic="http://guolin.tech/api/bing_pic";
        HttpUtil2.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String bingPic=response.body().string();
                SharedPreferences.Editor editor=
                        PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit();
                editor.putString("bing_pic",bingPic);
                editor.apply();
            }
        });
    }


}