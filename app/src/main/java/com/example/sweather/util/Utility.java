package com.example.sweather.util;

import android.text.TextUtils;

import com.example.sweather.WeatherActivity;
import com.example.sweather.db.City;
import com.example.sweather.db.County;
import com.example.sweather.db.Province;
import com.example.sweather.gson.Weather;
import com.example.sweather.gson.WeatherAqi;
import com.example.sweather.gson.WeatherFore;
import com.example.sweather.gson.WeatherHist;
import com.example.sweather.gson.WeatherLife;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Utility {

    //解析和处理服务器返回的省级数据
    public static boolean handleProvinceResponse(String response){
        if(!TextUtils.isEmpty(response)){
            try{
                JSONArray allProvinces=new JSONArray(response);
                for(int i=0;i<allProvinces.length();i++){
                    JSONObject provinceObject=allProvinces.getJSONObject(i);
                    Province province=new Province();
                    province.setProvinceName(provinceObject.getString("name"));
                    province.setProvinceCode(provinceObject.getInt("id"));
                    province.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    //解析和处理服务器返回的市级数据
    public static boolean handleCityResponse(String response,int provinceId){
        if(!TextUtils.isEmpty(response)){
            try{
                JSONArray allCities=new JSONArray(response);
                for(int i=0;i<allCities.length();i++){
                    JSONObject cityObject=allCities.getJSONObject(i);
                    City city=new City();
                    city.setCityName(((JSONObject) cityObject).getString("name"));
                    city.setCityCode(((JSONObject) cityObject).getInt("id"));
                    city.setProvinceId(provinceId);
                    city.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    //解析和处理服务器返回的县级数据
    public static boolean handleCountyResponse(String response,int cityId){
        if(!TextUtils.isEmpty(response)){
            try{
                JSONArray allCounties=new JSONArray(response);
                for(int i=0;i<allCounties.length();i++){
                    JSONObject countyObject=allCounties.getJSONObject(i);
                    County county=new County();
                    county.setCountyName(countyObject.getString("name"));
                    county.setWeatherId(((JSONObject) countyObject).getString("weather_id"));
                    county.setCityId(cityId);
                    county.save();
                }
                return true;
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return  false;
    }

    //将返回的JSON数据解析成Weather实体类
    public static Weather handleWeatherResponse(String response){
        try {
            JSONObject jsonObject=new JSONObject(response);
            JSONArray jsonArray=jsonObject.getJSONArray("HeWeather6");
            String weatherContent=jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(weatherContent, Weather.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    //解析成WeatherAqi实体类
    public static WeatherAqi handleWeatherAqiResponse(String response){
        try {
            JSONObject jsonObject=new JSONObject(response);
            JSONArray jsonArray=jsonObject.getJSONArray("HeWeather6");
            String weatherContent=jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(weatherContent, WeatherAqi.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    //解析成WeatherFore实体类
    public static WeatherFore handleWeatherForeResponse(String response){
        try {
            JSONObject jsonObject=new JSONObject(response);
            JSONArray jsonArray=jsonObject.getJSONArray("HeWeather6");
            String weatherContent=jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(weatherContent, WeatherFore.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    //解析历史
    public static WeatherHist handleWeatherHistResponse(String response){
        try{
            return new Gson().fromJson(response,WeatherHist.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    //WeatherLife
    public static WeatherLife handleWeatherLifeResponse(String response){
        try {
            JSONObject jsonObject=new JSONObject(response);
            JSONArray jsonArray=jsonObject.getJSONArray("HeWeather6");
            String weatherContent=jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(weatherContent, WeatherLife.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
