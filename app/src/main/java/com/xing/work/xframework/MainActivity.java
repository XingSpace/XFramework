package com.xing.work.xframework;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends ActivityBase implements View.OnClickListener{
    private Button button,button2,button3,button4,button5,button6;

    private int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void findViews() {
        button = (Button)findViewById(R.id.hp_button);
        button2 = (Button)findViewById(R.id.sd_button);
        button3 = (Button)findViewById(R.id.xlv_button);
        button4 = (Button)findViewById(R.id.xsb_button);
        button5 = (Button)findViewById(R.id.iv_button);
        button6 = (Button)findViewById(R.id.shadow_button);
        button.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        button6.setOnClickListener(this);

    }

    @Override
    public void init() {

        setLeftVisible(View.GONE);

        setRightVisible(View.GONE);

    }

    private Intent intent = new Intent();

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.hp_button:
                intent.setClass(getContext(), ActivityHP.class);
                startActivity(intent);
                break;

            case R.id.sd_button:
                intent.setClass(getContext(), ActivitySD.class);
                startActivity(intent);
                break;

            case R.id.xlv_button:
                intent.setClass(getContext(), ActivityXLV.class);
                startActivity(intent);
                break;

            case R.id.xsb_button:
                intent.setClass(getContext(),ActivityXsb.class);
                startActivity(intent);
                break;

            case R.id.iv_button:
                intent.setClass(getContext(),ActivityIV.class);
                startActivity(intent);
                break;

            case R.id.shadow_button:
                intent.setClass(getContext(),ActivityShadow.class);
                startActivity(intent);
                break;
        }
    }

}
