package com.example.sweather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sweather.gson.Result;
import com.example.sweather.gson.Weather;
import com.example.sweather.gson.WeatherHist;
import com.example.sweather.util.HttpUtil2;
import com.example.sweather.util.Utility;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class HistoryActivity extends AppCompatActivity {

    private LinearLayout historyLayout;
    private ScrollView sHistoryLayout;

    private String mCityName;
    private String mDate;
    private TextView menuTitleText;
    private Button menuBackButton;
    private String mWeatherId;
    private View dividerView;
    private TextView historyDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        menuTitleText=(TextView)findViewById(R.id.menu_title_text);
        menuBackButton=(Button)findViewById(R.id.menu_back_button);
        sHistoryLayout=(ScrollView)findViewById(R.id.s_history_layout);
        historyLayout=(LinearLayout) findViewById(R.id.history_layout);

        historyDate=(TextView)findViewById(R.id.history_date);
        dividerView=(View)findViewById(R.id.hdivider_view);

        menuTitleText.setText("查看历史天气数据");
        menuBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                historyLayout.removeAllViews();
                dividerView.setVisibility(View.INVISIBLE);
                historyDate.setVisibility(View.INVISIBLE);
                Intent intent=new Intent(HistoryActivity.this,MenuActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Calendar calendar=Calendar.getInstance();
        DatePickerDialog dialog=new DatePickerDialog(HistoryActivity.this,
                R.style.MyDatePickerDialogTheme,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year,
                                          int monthOfYear, int dayOfMonth) {

                        requestWeatherId();//获取城市id
                        String s_year=String.format("%04d",year);
                        String s_month=String.format("%02d",monthOfYear);
                        String s_day=String.format("%02d",dayOfMonth);
                        mDate=s_year+s_month+s_day;
                        historyDate.setText(mDate+"  "+mCityName);

                        requestHistory();

                    }
                },calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        //设置起始日期和结束日期
        DatePicker datePicker = dialog.getDatePicker();

        Date date = new Date("2015/07/08 00:00:01");//当前时间
        long time = date.getTime();
        datePicker.setMinDate(time);
        datePicker.setMaxDate(System.currentTimeMillis());
        dialog.show();


    }
    public void requestWeatherId(){
        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString=prefs.getString("weather",null);
        if((weatherString!=null) ){
            //选择区域之后会自动更新一次，之后，有缓存时直接解析天气数据
            Weather weather= Utility.handleWeatherResponse(weatherString);
            mWeatherId=weather.basic.weatherId;
            mCityName=weather.basic.cityName;
        }
        else{
            //无缓存时去服务器查询天气，首次启动选择得到的区域
            Intent intent=getIntent();
            mWeatherId=intent.getStringExtra("weather_id");//从Intent获取地区id
        }
    }

    private void requestHistory() {
        String s_weatherid=mWeatherId.substring(2,mWeatherId.length());
        final String requestHistory = "http://api.k780.com/?app=weather.history&weaid=" +
                s_weatherid+"&date="+mDate+"&appkey=44303&sign=8825b94df83576ebde4c12e7a4a45be4&format=json";
        HttpUtil2.sendOkHttpRequest(requestHistory, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(HistoryActivity.this,"获取历史数据失败",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String reponseText=response.body().string();
                final WeatherHist weatherHist=(WeatherHist)Utility.handleWeatherHistResponse(reponseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(weatherHist!=null && "1".equals(weatherHist.success)){
                            showWeatherHistInfo(weatherHist);
                        }
                        else{
                            Toast.makeText(HistoryActivity.this,"获取历史数据失败",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

    }
    private void showWeatherHistInfo(WeatherHist weatherHist){

        for(Result result:weatherHist.result){
            View view= LayoutInflater.from(this).inflate(R.layout.history_item,historyLayout,false);

            TextView huptimeText=(TextView)view.findViewById(R.id.huptime_text);
            TextView hweatherText=(TextView)view.findViewById(R.id.hweather_text);
            TextView htemperatureText=(TextView)view.findViewById(R.id.htemperature_text);
            TextView haqiText=(TextView)view.findViewById(R.id.haqi_text);

            String s=result.uptime;
            String s_uptime=s.substring(11,s.length());
            huptimeText.setText(s_uptime);
            hweatherText.setText(result.weather);
            String s_tmp="温度："+result.temperature;
            htemperatureText.setText(s_tmp);
            String s_aqi="AQI："+result.aqi;
            haqiText.setText(s_aqi);
            historyLayout.addView(view);
        }
    }
}
