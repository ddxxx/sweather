//now实况天气
package com.example.sweather.gson;

import com.google.gson.annotations.SerializedName;

public class Now {
    @SerializedName("tmp")
    public String temperature;

    public String cond_tx;

    public String fl;//体感温度
    public String hum;//相对湿度
    public String wind_dir;//风向
    public String wind_sc;//风力
    public String pres;//大气压强
    public String vis;//能见度
}
