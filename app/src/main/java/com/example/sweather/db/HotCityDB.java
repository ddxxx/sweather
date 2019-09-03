package com.example.sweather.db;

import org.litepal.crud.DataSupport;
import org.litepal.crud.LitePalSupport;

public class HotCityDB extends DataSupport {
    private String cityId;
    private String cityName;
    private String maxTemperature;
    private String minTemperature;

    public String getCityId() {
        return cityId;
    }

    public String getMaxTemperature() {
        return maxTemperature;
    }

    public String getMinTemperature() {
        return minTemperature;
    }

    public void setMaxTemperature(String maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    public void setMinTemperature(String minTemperature) {
        this.minTemperature = minTemperature;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

}
