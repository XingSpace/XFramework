package com.xing.work.xframework;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.xing.app.myutils.Utils.PermissionUtil;
import com.xing.work.xframework.PlaneWar.ActivityPlaneWar;

public class MainActivity extends ActivityBase implements View.OnClickListener{
    private Button button,button2,button3,button4,button5,button6,button7,button8,button9,button10;

    private int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void findViews() {
        button = findViewById(R.id.hp_button);
        button2 = findViewById(R.id.sd_button);
        button3 = findViewById(R.id.xlv_button);
        button4 = findViewById(R.id.xsb_button);
        button5 = findViewById(R.id.iv_button);
        button6 = findViewById(R.id.shadow_button);
        button7 = findViewById(R.id.xsp_button);
        button8 = findViewById(R.id.riv);
        button9 = findViewById(R.id.socket);
        button10 = findViewById(R.id.table);
        button.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        button6.setOnClickListener(this);
        button7.setOnClickListener(this);
        button8.setOnClickListener(this);
        button9.setOnClickListener(this);
        button10.setOnClickListener(this);
        findViewById(R.id.xy_button).setOnClickListener(this);
        findViewById(R.id.fj_button).setOnClickListener(this);
    }

    @Override
    public void init() {

        setLeftVisible(View.GONE);

        setRightVisible(View.GONE);

    }

    private Intent intent = new Intent();

    @Override
    public void onClick(View v) {

        if(!PermissionUtil.permissionEntry(MainActivity.this,getApplicationContext(),true,299)){
            return;
        }

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

            case R.id.xy_button:
                intent.setClass(getContext(),ActivityXY.class);
                startActivity(intent);
                break;

            case R.id.xsp_button:
                intent.setClass(getContext(),ActivityXSP.class);
                startActivity(intent);
                break;

            case R.id.fj_button:
                intent.setClass(getContext(), ActivityPlaneWar.class);
                startActivity(intent);
                break;

            case R.id.riv:
                intent.setClass(getContext(), ActivityRoundImage.class);
                startActivity(intent);
                break;

            case R.id.socket:
                intent.setClass(getContext(),ActivitySocket.class);
                startActivity(intent);
                break;

            case R.id.table:
                intent.setClass(getContext(),ActivitySTV.class);
                startActivity(intent);
                break;
        }
    }

}
