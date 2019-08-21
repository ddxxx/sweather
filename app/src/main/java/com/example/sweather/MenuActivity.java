package com.example.sweather;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Presentation;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sweather.db.UpdateFreq;
import com.example.sweather.service.AutoUpdateService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MenuActivity extends AppCompatActivity {

    private Button menuBackButton;
    private Button viewHistoryButton;
    private Button viewRankButton;
    private Button autoRefreshButton;
    private Button authorButton;
    private TextView menuTitleText;
    private AlertDialog alertDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        menuBackButton=(Button)findViewById(R.id.menu_back_button);
        viewHistoryButton=(Button)findViewById(R.id.view_history_button);
        viewRankButton=(Button)findViewById(R.id.view_rank_button);
        authorButton=(Button)findViewById(R.id.author_button);
        autoRefreshButton=(Button)findViewById(R.id.auto_refresh_button);
        menuTitleText=(TextView)findViewById(R.id.menu_title_text) ;

        menuTitleText.setText("基本设置");
        String update_freq;
        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(this);
        update_freq=prefs.getString("update_freq",null);
        autoRefreshButton.setText("自动更新频率\n（"+update_freq+"小时）");

        menuBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MenuActivity.this,WeatherActivity.class);
                startActivity(intent);
                finish();
            }
        });
        viewHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MenuActivity.this,HistoryActivity.class);
                startActivity(intent);
                finish();
            }
        });
        viewRankButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MenuActivity.this,HistoryActivity.class);
                startActivity(intent);
                finish();
            }
        });
        autoRefreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String[] items={"1小时","2小时","4小时","8小时"};
                AlertDialog.Builder alertBuilder=new AlertDialog.Builder(MenuActivity.this);
                alertBuilder.setTitle("设定自动更新频率");
                alertBuilder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)    {
                        Toast.makeText(MenuActivity.this,"每"+items[i]+"自动更新",
                                Toast.LENGTH_SHORT).show();

                        //----SharedPreferences
                        SharedPreferences.Editor editor= PreferenceManager.getDefaultSharedPreferences(MenuActivity.this).edit();
                        editor.putString("update_freq",items[i].substring(0,1));
                        editor.apply();

                        //------------
                        /*  Intent intent_freq=new Intent(MenuActivity.this, Service.class);
                        intent_freq.putExtra("update_freq",items[i].substring(0,1));
                        startService(intent_freq);
                      不知道如何更新  */
                        UpdateFreq.setUpdate_freq(Integer.valueOf(items[i].substring(0,1)));
                        autoRefreshButton.setText("自动更新频率\n（"+items[i]+"）");
                        alertDialog.dismiss();
                    }
                });
                alertDialog=alertBuilder.create();
                alertDialog.show();
            }
        });

        authorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog=new AlertDialog.Builder(MenuActivity.this);
                dialog.setTitle("关于");
                dialog.setMessage("SWeather\n版本：v1.0\n作者：ddxxx");
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                dialog.show();
            }
        });
    }
}
