package com.example.sweather.db;

import org.litepal.crud.LitePalSupport;

public class Province extends LitePalSupport {
    private int id;
    private String provinceName;
    private int ProvinceCode;

    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id=id;
    }

    public String getProvinceName(){
        return provinceName;
    }
    public void setProvinceName(String provinceName){
        this.provinceName=provinceName;
    }

    public int getProvinceCode(){
        return ProvinceCode;
    }
    public void setProvinceCode(int provinceCode){
        this.ProvinceCode=provinceCode;
    }
}
