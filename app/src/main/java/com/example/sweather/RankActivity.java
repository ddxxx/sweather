package com.example.sweather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sweather.db.HotCityDB;
import com.example.sweather.gson.HotCity;
import com.example.sweather.util.HttpUtil;
import com.example.sweather.util.HttpUtil2;
import com.example.sweather.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
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

    private boolean maxFlag=true;
    private boolean minFlag=true;



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
       requestHotCity();//读取热门城市列表，存入数据库
        Log.d("1-","是否执行完");
        //获取各城市温度并存入数据库

        //显示城市数据




        //-----需更改图标颜色，四个图标不要冲突
        mMaxTmpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(maxFlag){
                    mMaxTmpButton.setCompoundDrawablesWithIntrinsicBounds(null,null,drawable_down,null);
                    maxFlag=false;
                }
                else{
                    mMaxTmpButton.setCompoundDrawablesWithIntrinsicBounds(null,null,drawable_up,null);
                    maxFlag=true;
                }
            }
        });
        mMinTmpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(minFlag){
                    mMinTmpButton.setCompoundDrawables(null,null,drawable_down,null);
                    minFlag=false;
                }
                else{
                    mMinTmpButton.setCompoundDrawables(null,null,drawable_up,null);
                    minFlag=true;
                }
            }
        });



    }
    private void requestHotCity(){
        Log.d("1-","11");
        final String hotCityUrl="https://search.heweather.net/top?group=cn&key=2c1ed2821caf4572a05c26380bdf5863&number=30";
        Log.d("1-","22");
        HttpUtil2.sendOkHttpRequest(hotCityUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {   e.getMessage();
            Log.d("1-","33");       }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText=response.body().string();
                final HotCity hotCity=(HotCity) Utility.handleHotCityResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(hotCity!=null && "ok".equals(hotCity.status)){
                            //成功获得数据
                            Log.d("1-","1");
                            HotCityDB dbHotCity=new HotCityDB();
                            dbHotCity.setCityId(hotCity.basic.cid);
                            dbHotCity.setCityName(hotCity.basic.location);
                            dbHotCity.save();
                            Log.d("1-cid:",hotCity.basic.cid);
                            Log.d("1-location:",hotCity.basic.location);
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
    }
    private void requestTmp(String weatherId){
        String nowWeatherUrl="https://free-api.heweather.net/s6/weather/now?location=" +
                weatherId+"&key=2c1ed2821caf4572a05c26380bdf5863";
        HttpUtil2.sendOkHttpRequest(nowWeatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
    }
}


