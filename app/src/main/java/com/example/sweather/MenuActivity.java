package com.example.sweather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends AppCompatActivity {

    private Button menuBackButton;
    private Button viewHistoryButton;
    private Button viewRankButton;
    private Button autoRefreshButton;
    private Button authorButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        menuBackButton=(Button)findViewById(R.id.menu_back_button);
        viewHistoryButton=(Button)findViewById(R.id.view_history_button);
        viewRankButton=(Button)findViewById(R.id.view_rank_button);
        authorButton=(Button)findViewById(R.id.author_button);
        autoRefreshButton=(Button)findViewById(R.id.auto_refresh_button);

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

            }
        });
    }
}
