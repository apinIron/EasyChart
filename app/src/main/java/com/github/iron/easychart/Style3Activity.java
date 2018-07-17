package com.github.iron.easychart;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.github.iron.chart.dashboard.DashboardView3;

import java.util.Random;

/**
 * @author iron
 *         created at 2018/7/16
 */
public class Style3Activity extends AppCompatActivity implements View.OnClickListener {

    private EditText etValue,etLargeCalibration,etCalibrationBettew,etCalibrationNumber;
    private DashboardView3 dashboardView;
    private CheckBox cbAnim,cbReset;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_style3);

        dashboardView = findViewById(R.id.dv);
        etValue = findViewById(R.id.et_value);
        cbAnim = findViewById(R.id.cb_anim);
        cbReset = findViewById(R.id.cb_reset);
        etLargeCalibration = findViewById(R.id.et_large_calibration);
        etCalibrationBettew = findViewById(R.id.et_calibration_text);
        etCalibrationNumber = findViewById(R.id.et_calibration_number);
        findViewById(R.id.btn_random).setOnClickListener(this);
        findViewById(R.id.btn_set_value).setOnClickListener(this);
        findViewById(R.id.btn_set).setOnClickListener(this);
        //初始化
        etLargeCalibration.setText("350 550 600 650 700 950");
        etCalibrationBettew.setText("较差 中等 良好 优秀 极好");
        etCalibrationNumber.setEnabled(false);
        //可配置项
        dashboardView.setDateStrPattern("评估时间：{date}");
        dashboardView.setValueLevelPattern("信用{level}");
    }

    @Override
    public void onClick(View v) {
        boolean anim = cbAnim.isChecked();
        boolean reset = cbReset.isChecked();

        switch (v.getId()){
            case R.id.btn_random:
                int max = dashboardView.getMax();
                int min = dashboardView.getMin();
                dashboardView.setValue(new Random().nextInt(max - min) + min, anim, reset);
                break;
            case R.id.btn_set_value:
                if(etValue.length() > 0) {
                    dashboardView.setValue(Integer.parseInt(etValue.getText().toString()), anim, reset);
                }
                break;
            case R.id.btn_set:
                int[] arr = convert(etLargeCalibration.getText().toString());
                String str = etCalibrationBettew.getText().toString();
                String[] strs = TextUtils.isEmpty(str) ? null : str.split(" ");
                int number = etCalibrationNumber.length() > 0 ? Integer.valueOf(etCalibrationNumber.getText().toString()) : 0;

                dashboardView.setCalibration(arr,strs,number);
                break;
        }
    }

    private int[] convert(String s) {
        if(TextUtils.isEmpty(s)){
            return new int[]{};
        }
        String[] strs = s.split(" ");
        int[] arr = new int[strs.length];
        for (int i = 0; i < strs.length; i++) {
            arr[i] = Integer.valueOf(strs[i]);
        }
        return arr;
    }
}
