package com.github.iron.easychart;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.github.iron.chart.dashboard.DashboardView1;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private DashboardView1 dv1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_style1).setOnClickListener(this);
        findViewById(R.id.btn_style2).setOnClickListener(this);
        findViewById(R.id.btn_style3).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.btn_style1:
                intent = new Intent(this, Style1Activity.class);
                break;
            case R.id.btn_style2:
                intent = new Intent(this, Style2Activity.class);
                break;
            case R.id.btn_style3:
                intent = new Intent(this, Style3Activity.class);
                break;
        }
        if(intent != null) {
            startActivity(intent);
        }
    }
}
