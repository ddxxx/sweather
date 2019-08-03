package com.example.sweather.gson;

public class WeatherAqi {

    public String status;

    public Air air_now_city;//这里的名称要和JSON中一致，否则无法返回
    //和Forecast、suggestion原理不一样，它们是遍历赋值
}
