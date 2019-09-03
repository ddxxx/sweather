package com.example.sweather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sweather.db.HotCityDB;
import com.example.sweather.gson.HotCity;
import com.example.sweather.gson.HotCityBasic;
import com.example.sweather.gson.Weather;
import com.example.sweather.util.HttpUtil;
import com.example.sweather.util.HttpUtil2;
import com.example.sweather.util.Utility;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class RankActivity extends AppCompatActivity {

    private LinearLayout rankLayout;
    private ScrollView sRankLayout;

    private Button mMaxTmpButton;
    private Button mMinTmpButton;
    private TextView menuTitleText;
    private Button menuBackButton;

    private SQLiteDatabase db;

    private boolean maxFlag=true;
    private boolean minFlag=true;

    private List<HotCityDB> list;

    HotCityDB hcdb=new HotCityDB();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);
        //每点击一次按钮转换一次排序图标
        final Drawable drawable_up=getResources().getDrawable(R.drawable.goods_detail_sort_up);
        final Drawable drawable_down=getResources().getDrawable(R.drawable.goods_detail_sort_down);
        drawable_up.getMinimumWidth();
        drawable_up.getMinimumHeight();
        drawable_down.getMinimumHeight();
        drawable_down.getMinimumWidth();
        menuTitleText=(TextView)findViewById(R.id.menu_title_text);
        menuBackButton=(Button)findViewById(R.id.menu_back_button);
        mMaxTmpButton=(Button)findViewById(R.id.max_tmp_button);
        mMinTmpButton=(Button)findViewById(R.id.min_tmp_button);
        sRankLayout=(ScrollView)findViewById(R.id.s_rank_layout);
        rankLayout=(LinearLayout)findViewById(R.id.rank_layout);
        db=Connector.getDatabase();
        menuTitleText.setText("查看热门城市温度排名");
        menuBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rankLayout.removeAllViews();
                Intent intent=new Intent(RankActivity.this,MenuActivity.class);
                startActivity(intent);
                finish();
            }
        });
        Log.d("1-","执行request");
        requestTmp("111");//读取热门城市列表，存入数据库

        Log.d("1-","是否执行完");
        //获取各城市温度并存入数据库

        //显示城市数据




        //-----需更改图标颜色，四个图标不要冲突
        mMaxTmpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(maxFlag){//max，由大到小
                    mMaxTmpButton.setCompoundDrawablesWithIntrinsicBounds(null,null,drawable_down,null);
                    maxFlag=false;
                    List<HotCityDB> hotcities=DataSupport.findAll(HotCityDB.class);

                    for(HotCityDB h:hotcities){
                        Log.d("1-  读取",h.getCityName()+h.getMinTemperature());
/*
                        rankLayout.removeAllViews();
                        View view1= LayoutInflater.from(RankActivity.this).inflate(R.layout.rank_item,rankLayout,false);

                        TextView rankCityName=(TextView)view.findViewById(R.id.rank_city_name);
                        TextView rankMaxTmp=(TextView)view.findViewById(R.id.rank_max_tmp);
                        TextView ranMinTmp=(TextView)view.findViewById(R.id.rank_min_tmp);
                        rankCityName.setText(h.getCityName());
                        rankMaxTmp.setText(h.getMaxTemperature());
                        ranMinTmp.setText(h.getMinTemperature());
*/

                    }

                }
                else{//max，由小到大
                    mMaxTmpButton.setCompoundDrawablesWithIntrinsicBounds(null,null,drawable_up,null);
                    maxFlag=true;
                    /*
                    List<HotCityDB> hotcities=DataSupport.order("maxTemperature").find(HotCityDB.class);
                    for(HotCityDB h:hotcities){
                        rankLayout.removeAllViews();
                        View view1= LayoutInflater.from(RankActivity.this).inflate(R.layout.rank_item,rankLayout,false);

                        TextView rankCityName=(TextView)view.findViewById(R.id.rank_city_name);
                        TextView rankMaxTmp=(TextView)view.findViewById(R.id.rank_max_tmp);
                        TextView ranMinTmp=(TextView)view.findViewById(R.id.rank_min_tmp);
                        rankCityName.setText(h.getCityName());
                        rankMaxTmp.setText(h.getMaxTemperature());
                        ranMinTmp.setText(h.getMinTemperature());
                    }

                     */
                }
            }
        });
        mMinTmpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(minFlag){//min，由大到小
                    mMinTmpButton.setCompoundDrawables(null,null,drawable_down,null);
                    minFlag=false;
                }
                else{//max，由小到大
                    mMinTmpButton.setCompoundDrawables(null,null,drawable_up,null);
                    minFlag=true;
                }
            }
        });



    }
    private void requestHotCity(){
        //预编入hotcity而非网络获取
        String[]a={"北京","上海","温州","广州","深圳","松江","南京","武汉","成都","苏州","郑州","西安","宁波",
                "青岛","长沙","天津","济南","大连","长春","哈尔滨","石家庄","合肥","福州","沈阳","东莞"};


     //   Log.d("1-","11");
        /*
        final String hotCityUrl="https://search.heweather.net/top?group=cn&key=2c1ed2821caf4572a05c26380bdf5863&number=30";
    //    Log.d("1-","22");
        HttpUtil2.sendOkHttpRequest(hotCityUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {   e.getMessage(); }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText=response.body().string();
                final HotCity hotCity=(HotCity) Utility.handleHotCityResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(hotCity!=null && "ok".equals(hotCity.status)){
                            //成功获得数据


                            for(HotCityBasic hotCity1:hotCity.hotCityBasics){
                                requestTmp(hotCity1.cid,hotCity1.location);

                             //   hcdb.setCityId(hotCity1.cid);
                             //   hcdb.setCityName(hotCity1.location);
                            }

                        }
                        else{
                            Log.d("1-","0");
                            Toast.makeText(RankActivity.this,"城市信息获取失败",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

         */
    }
    private void requestTmp(final String weatherId) {
        String[] a = {"北京", "上海", "温州", "广州", "深圳", "松江", "南京", "武汉", "成都", "苏州", "郑州", "西安", "宁波",
                "青岛", "长沙", "天津", "济南", "大连", "长春", "哈尔滨", "石家庄", "合肥", "福州", "沈阳", "东莞"};
        for (final String s : a) {
            String nowWeatherUrl = "https://free-api.heweather.net/s6/weather/now?location=" +
                    s + "&key=2c1ed2821caf4572a05c26380bdf5863";
            HttpUtil2.sendOkHttpRequest(nowWeatherUrl, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RankActivity.this, "获取实时天气信息失败",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String responseText = response.body().string();
                    final Weather weather = (Weather) Utility.handleWeatherResponse(responseText);//JSON转换成Weather对象
                    if (weather != null && "ok".equals(weather.status)) {

                        hcdb.setCityName(s);
                        hcdb.setMinTemperature(weather.now.temperature);//当前温度
                        hcdb.setMaxTemperature(weather.now.cond_txt);//天气
                        hcdb.save();
                        Toast.makeText(RankActivity.this, "正在加载", Toast.LENGTH_SHORT).show();
                    }
                /*
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(weather!=null && "ok".equals(weather.status)){

                            hcdb.setCityName(cityName);
                            hcdb.setMinTemperature(weather.now.temperature);
                            hcdb.setMaxTemperature(weather.now.temperature_max);
                            hcdb.save();
                            Toast.makeText(RankActivity.this, "正在加载", Toast.LENGTH_SHORT).show();

                        }
                        else{           }

                    }
                });

                 */

                }
            });
        }
    }
}


