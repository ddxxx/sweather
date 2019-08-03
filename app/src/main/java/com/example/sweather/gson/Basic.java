//basic
package com.example.sweather.gson;

import com.google.gson.annotations.SerializedName;
//使用注解使java字段和JSON字段建立联系

public class Basic{
    @SerializedName("location")
    public String cityName;//城市名称

    @SerializedName("cid")
    public String weatherId;//城市ID（是否需要？）
}

