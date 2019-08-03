//daily_forecast
package com.example.sweather.gson;

import com.google.gson.annotations.SerializedName;

public class Forecast {

    public String date;

    public String tmp_max;
    public String tmp_min;

    public String cond_txt_d;
    public String cond_txt_n;

    public String wind_sc;//风力

    public String pop;//降水概率
}
