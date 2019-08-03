package com.example.sweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeatherFore {

    public String status;

    @SerializedName("daily_forecast")
    public List<Forecast> forecastList;
}
