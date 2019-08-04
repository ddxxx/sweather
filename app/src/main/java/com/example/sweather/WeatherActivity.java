package com.example.sweather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.sweather.gson.Forecast;
import com.example.sweather.gson.Suggestion;
import com.example.sweather.gson.Weather;
import com.example.sweather.gson.WeatherAqi;
import com.example.sweather.gson.WeatherFore;
import com.example.sweather.gson.WeatherLife;
import com.example.sweather.service.AutoUpdateService;
import com.example.sweather.util.HttpUtil;
import com.example.sweather.util.HttpUtil2;
import com.example.sweather.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {

    public SwipeRefreshLayout swipeRefresh;
    public DrawerLayout drawerLayout;

    private String mWeatherId;//记录城市的Id
    private ScrollView weatherLayout;
    //title
    private TextView titleCity;
    private Button navButton;//选址
    private Button mulMenu;
    //now
    private TextView updateTime;
    private TextView degreeText;
    private TextView weatherInfoText;
    //forecast
    private LinearLayout forecastLayout;
    //aqi
    private TextView aqiText;
    private TextView qltyText;
    //suggestion
    private TextView comfortText;
    private TextView catWashText;
    private TextView sportText;

    private ImageView bingPicImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT>=21){
            View decorView=getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_weather);

        //初始化各控件
        drawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);

        bingPicImg=(ImageView)findViewById(R.id.bing_pic_img);
        weatherLayout=(ScrollView)findViewById(R.id.weather_layout);
        titleCity=(TextView)findViewById(R.id.title_city);
        navButton=(Button)findViewById(R.id.nav_button);
        mulMenu=(Button)findViewById(R.id.mul_menu) ;
        updateTime=(TextView)findViewById(R.id.update_time);
        degreeText=(TextView)findViewById(R.id.degree_text);
        weatherInfoText=(TextView) findViewById(R.id.weather_info_text);
        forecastLayout=(LinearLayout) findViewById(R.id.forecast_layout);
        aqiText=(TextView)findViewById(R.id.aqi_text);
        qltyText=(TextView)findViewById(R.id.qlty_text);
        comfortText=(TextView)findViewById(R.id.comfort_text);
        catWashText=(TextView)findViewById(R.id.car_wash_text);
        sportText=(TextView)findViewById(R.id.sport_text);
        swipeRefresh=(SwipeRefreshLayout)findViewById(R.id.swipe_refresh);//获取实例，下拉刷新
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);//设置进度条颜色

        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString=prefs.getString("weather",null);

        SharedPreferences prefs2= PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString2=prefs2.getString("weatherAqi",null);

        SharedPreferences prefs3= PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString3=prefs3.getString("weatherFore",null);

        SharedPreferences prefs4= PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString4=prefs4.getString("weatherLife",null);

        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        if((weatherString!=null) ){
            //选择区域之后会自动更新一次，之后，有缓存时直接解析天气数据
            Weather weather= Utility.handleWeatherResponse(weatherString);
            showWeatherInfo(weather);
            mWeatherId=weather.basic.weatherId;

            WeatherAqi weatherAqi=Utility.handleWeatherAqiResponse(weatherString2);
            showWeatherAqiInfo(weatherAqi);

            WeatherFore weatherFore=Utility.handleWeatherForeResponse(weatherString3);
            showWeatherForeInfo(weatherFore);

            WeatherLife weatherLife=Utility.handleWeatherLifeResponse(weatherString4);
            showWeatherLifeInfo(weatherLife);
        }
        else{
            //无缓存时去服务器查询天气，首次启动选择得到的区域
            Intent intent=getIntent();
            mWeatherId=intent.getStringExtra("weather_id");//从Intent获取地区id
            weatherLayout.setVisibility(View.INVISIBLE);
            requestWeather(mWeatherId);
        }
        //下拉刷新监听器
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                requestWeatherId();
                requestWeather(mWeatherId);//请求天气
            }
        });
        String bingPic=prefs.getString("bing_pic",null);
        if(bingPic!=null){
            Glide.with(this).load(bingPic).into(bingPicImg);
        }
        else{
            loadBingPic();
        }
    }

    //用于下拉刷新监听器获取缓存中的天气id
    public void requestWeatherId(){
        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString=prefs.getString("weather",null);

        SharedPreferences prefs2= PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString2=prefs2.getString("weatherAqi",null);

        SharedPreferences prefs3= PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString3=prefs3.getString("weatherFore",null);

        SharedPreferences prefs4= PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString4=prefs4.getString("weatherLife",null);
        if((weatherString!=null) ){
            //选择区域之后会自动更新一次，之后，有缓存时直接解析天气数据
            Weather weather= Utility.handleWeatherResponse(weatherString);
            showWeatherInfo(weather);
            mWeatherId=weather.basic.weatherId;
        }
        else{
            //无缓存时去服务器查询天气，首次启动选择得到的区域
            Intent intent=getIntent();
            mWeatherId=intent.getStringExtra("weather_id");//从Intent获取地区id
        }
    }
    //根据天气id请求城市天气信息,&&&&
    public void requestWeather(final String weatherId){

        String nowWeatherUrl="https://free-api.heweather.net/s6/weather/now?location=" +
                weatherId+"&key=2c1ed2821caf4572a05c26380bdf5863";//拼装出接口地址
        String weatherAqiUrl="https://free-api.heweather.net/s6/air/now?location="+
                weatherId+"&key=2c1ed2821caf4572a05c26380bdf5863";
        String weatherForeUrl="https://free-api.heweather.net/s6/weather/forecast?location="+
                weatherId+"&key=2c1ed2821caf4572a05c26380bdf5863";
        String weatherLifeUrl="https://free-api.heweather.net/s6/weather/lifestyle?location="+
                weatherId+"&key=2c1ed2821caf4572a05c26380bdf5863";

        //向weatherUrl发出请求,,,,,,
        HttpUtil.sendOkHttpRequest(nowWeatherUrl, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText=response.body().string();//得到服务器返回的内容
                final Weather weather=(Weather)Utility.handleWeatherResponse(responseText);//JSON转换成Weather对象
                runOnUiThread(new Runnable() {//因为要进行UI操作，必须将线程转换至主线程
                    @Override
                    public void run() {
                        if(weather!=null && "ok".equals(weather.status)){
                            //请求成功，数据缓存到SharedPreferences中
                            SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences
                                    (WeatherActivity.this).edit();
                            editor.putString("weather",responseText);//添加数据
                            editor.apply();//提交
                            showWeatherInfo(weather);
                        }
                        else{
                            Toast.makeText(WeatherActivity.this,"获取实时天气信息失败",
                                    Toast.LENGTH_SHORT).show();
                        }
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this,"获取实时天气信息失败",
                                Toast.LENGTH_SHORT).show();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        },weatherAqiUrl, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText=response.body().string();//得到服务器返回的内容
                final WeatherAqi weatherAqi=(WeatherAqi) Utility.handleWeatherAqiResponse(responseText);//JSON转换成Weather对象
                runOnUiThread(new Runnable() {//因为要进行UI操作，必须将线程转换至主线程
                    @Override
                    public void run() {
                        if(weatherAqi!=null && "ok".equals(weatherAqi.status)){
                            //请求成功，数据缓存到SharedPreferences中
                            SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences
                                    (WeatherActivity.this).edit();
                            editor.putString("weatherAqi",responseText);//添加数据
                            editor.apply();//提交
                            showWeatherAqiInfo(weatherAqi);
                        }
                        else{
                            Toast.makeText(WeatherActivity.this,"无法获取空气质量信息",
                                    Toast.LENGTH_SHORT).show();
                        }
                  //      swipeRefresh.setRefreshing(false);
                    }
                });
            }
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this,"获取空气质量信息失败",
                                Toast.LENGTH_SHORT).show();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        },weatherForeUrl, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText=response.body().string();//得到服务器返回的内容
                final WeatherFore weatherFore=(WeatherFore) Utility.handleWeatherForeResponse(responseText);//JSON转换成Weather对象
                runOnUiThread(new Runnable() {//因为要进行UI操作，必须将线程转换至主线程
                    @Override
                    public void run() {
                        if(weatherFore!=null && "ok".equals(weatherFore.status)){
                            //请求成功，数据缓存到SharedPreferences中
                            SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences
                                    (WeatherActivity.this).edit();
                            editor.putString("weatherFore",responseText);//添加数据
                            editor.apply();//提交
                            showWeatherForeInfo(weatherFore);
                        }
                        else{
                            Toast.makeText(WeatherActivity.this,"获取预报信息失败fore1",
                                    Toast.LENGTH_SHORT).show();
                        }
                   //     swipeRefresh.setRefreshing(false);
                    }
                });
            }
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this,"获取预报信息失败",
                                Toast.LENGTH_SHORT).show();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        },weatherLifeUrl, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText=response.body().string();//得到服务器返回的内容
                final WeatherLife weatherLife=(WeatherLife) Utility.handleWeatherLifeResponse(responseText);//JSON转换成Weather对象
                runOnUiThread(new Runnable() {//因为要进行UI操作，必须将线程转换至主线程
                    @Override
                    public void run() {
                        if(weatherLife!=null && "ok".equals(weatherLife.status)){
                            //请求成功，数据缓存到SharedPreferences中
                            SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences
                                    (WeatherActivity.this).edit();
                            editor.putString("weatherLife",responseText);//添加数据
                            editor.apply();//提交
                            showWeatherLifeInfo(weatherLife);
                        }
                        else{
                            Toast.makeText(WeatherActivity.this,"获取生活建议失败",
                                    Toast.LENGTH_SHORT).show();
                        }
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this,"获取生活建议失败",
                                Toast.LENGTH_SHORT).show();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });
        loadBingPic();
    }

    private void showWeatherInfo(Weather weather){//Weather相关控件更新
        String cityName=weather.basic.cityName;
        String weatherId=weather.basic.weatherId;
        String s_updateTime=weather.update.localTime;
        String degree=weather.now.temperature;
        String weatherInfo=weather.now.cond_tx;
        titleCity.setText(cityName);
        updateTime.setText(s_updateTime);
        degreeText.setText(degree);
        weatherInfoText.setText(weatherInfo);

        Intent intent=new Intent(this,AutoUpdateService.class);
        startService(intent);//激活自动更新服务，选中城市成功更新后，该服务会一直在后台运行
    }

    private void showWeatherAqiInfo(WeatherAqi weatherAqi){
        aqiText.setText(weatherAqi.air_now_city.aqIndex);
        qltyText.setText(weatherAqi.air_now_city.qlty);
    }

    private void showWeatherForeInfo(WeatherFore weatherFore){
        forecastLayout.removeAllViews();
        for(Forecast forecast:weatherFore.forecastList){
            View view=LayoutInflater.from(this).inflate(R.layout.forecast_item,forecastLayout,false);
            TextView dateText=(TextView)view.findViewById(R.id.date_text);

            TextView infoText=(TextView)view.findViewById(R.id.info_text);
            TextView tmpText=(TextView)view.findViewById(R.id.tmp_text);
            TextView popText=(TextView)view.findViewById(R.id.pop_text);

            dateText.setText(forecast.date);
            infoText.setText(forecast.cond_txt_d);
            String s_tmpText=forecast.tmp_min+"~"+forecast.tmp_max+"℃";
            tmpText.setText(s_tmpText);
            popText.setText(forecast.pop);
            forecastLayout.addView(view);
        }
        weatherLayout.setVisibility(View.VISIBLE);
    }

    private void showWeatherLifeInfo(WeatherLife weatherLife){
        for(Suggestion suggestion:weatherLife.suggestionList){
            if("comf".equals(suggestion.type)){
                String s_comfortText="舒适度："+suggestion.brf+"\n"+suggestion.txt;
                comfortText.setText(s_comfortText);
            }
            else if("cw".equals(suggestion.type)){
                String s_carWashText="洗车："+suggestion.brf+"\n"+suggestion.txt;
                catWashText.setText(s_carWashText);
            }
            else if("sport".equals(suggestion.type)){
                String s_sportText="运动："+suggestion.brf+"\n"+suggestion.txt;
                sportText.setText(s_sportText);
            }
        }
    }
    //加载必应每日一图
    private void loadBingPic(){
        final String requestBingPic="http://guolin.tech/api/bing_pic";
        HttpUtil2.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic=response.body().string();
                SharedPreferences.Editor editor=
                        PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                editor.putString("bing_pic",bingPic);
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherActivity.this).load(bingPic).into(bingPicImg);
                    }
                });
            }
        });
    }
}
