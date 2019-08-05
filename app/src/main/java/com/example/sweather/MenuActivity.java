package com.example.sweather;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

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
    }


}
