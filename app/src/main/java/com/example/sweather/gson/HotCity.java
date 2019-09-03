package com.example.sweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HotCity {
    @SerializedName("basic")
    public List<HotCityBasic> hotCityBasics;
    public String status;
}
