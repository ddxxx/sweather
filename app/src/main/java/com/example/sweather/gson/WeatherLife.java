package com.example.sweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeatherLife {

    public String status;
    @SerializedName("lifestyle")
    public List<Suggestion> suggestionList;
}
