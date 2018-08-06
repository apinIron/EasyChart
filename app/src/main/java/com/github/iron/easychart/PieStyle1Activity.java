package com.github.iron.easychart;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.github.iron.chart.pie.BasePieView;
import com.github.iron.chart.pie.PieDataView;
import com.github.iron.chart.pie.PieView1;

import java.util.Random;

/**
 * @author iron
 *         created at 2018/7/18
 */
public class PieStyle1Activity extends AppCompatActivity implements View.OnClickListener {

    private PieView1 pieView;
    private SeekBar seekBar;
    private CheckBox cbAnim;
    private RadioButton rb1;

    private int color[] = new int[]{Color.rgb(70, 236, 249), Color.rgb(251, 128, 88),
            Color.rgb(66, 164, 231),Color.rgb(86, 123, 231), Color.rgb(250, 203, 70),
            Color.rgb(161, 190, 36),Color.rgb(250, 50, 50), Color.rgb(180, 108, 56),
            Color.rgb(190, 240, 140), Color.rgb(100, 160, 135)};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_style1);

        pieView = findViewById(R.id.pie1);
        pieView.setTitle("总额");
        pieView.setDisplayDigits(2);

        PieDataView pdv = findViewById(R.id.pdv);
        pdv.setColumnNumber(2);
        pieView.attachPieDataView(pdv);

        findViewById(R.id.btn_set).setOnClickListener(this);
        final TextView textView = findViewById(R.id.tv1);
        cbAnim = findViewById(R.id.cb_anim);
        rb1 = findViewById(R.id.rb1);
        seekBar = findViewById(R.id.sb);
        seekBar.setProgress(5);
        textView.setText("5");
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textView.setText(progress + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //设置数据
        setData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_set:
                setData();
                break;
        }
    }

    private void setData(){
        //数量
        int count = seekBar.getProgress();
        float[] value = new float[count];
        String[] name = new String[count];
        int[] color = new int[count];

        Random random = new Random();

        for (int i = 0; i < count; i++) {
            value[i] = random.nextInt(2000);
            name[i] = "金额:" + i;
            color[i] = this.color[i];
        }
        //设置
        pieView.setAnimStyle(rb1.isChecked() ? BasePieView.ANIM_STYLE_SCALE : BasePieView.ANIM_STYLE_TRANSLATE);
        pieView.setData(value, name, color, cbAnim.isChecked());
    }

}
