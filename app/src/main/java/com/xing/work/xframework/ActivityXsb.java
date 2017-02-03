package com.xing.work.xframework;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;


/**
 * Created by wangxing on 17/1/19.
 */

public class ActivityXsb extends ActivityBase implements XSeekBar.OnXSeekBarListener{

    private TextView textView;

    private XSeekBar xSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xsb);
    }

    @Override
    public void findViews() {
        textView = (TextView) findViewById(R.id.text);
        xSeekBar = (XSeekBar) findViewById(R.id.xsb3);
        xSeekBar.setMax(15);
        xSeekBar.setMaxWeight(5);
        xSeekBar.setRadius(20);
        xSeekBar.setBCircleColor(Color.argb(255,0xff,0x40,0x81));
    }

    @Override
    public void init() {
        xSeekBar.setOnSeekBarListener(this);
    }

    @Override
    public void onProgressChange(XSeekBar xSeekBar, int progress, boolean isUser) {
        textView.setText("当前的Progress值为:"+progress);
    }
}
