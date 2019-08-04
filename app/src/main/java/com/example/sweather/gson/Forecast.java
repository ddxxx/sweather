//daily_forecast
package com.example.sweather.gson;

import com.google.gson.annotations.SerializedName;

public class Forecast {

    public String date;

    public String tmp_max;
    public String tmp_min;

    public String cond_code_d;
    public String hum;
    public String pres;

    public String wind_dir;
    public String wind_sc;//风力

    public String pop;//降水概率
}
