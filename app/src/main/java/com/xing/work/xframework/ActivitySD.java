package com.xing.work.xframework;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by wangxing on 16/10/8.
 */

public class ActivitySD extends ActivityBase implements View.OnClickListener{

    private Button button,button1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sd);
    }

    @Override
    public void findViews() {
        button = (Button) findViewById(R.id.button1);
        button1 = (Button) findViewById(R.id.button2);

        button.setOnClickListener(this);
        button1.setOnClickListener(this);
    }

    @Override
    public void init() {
        setTitle("测试SlideDialog");

        setRightVisible(View.GONE);
        setLeftText("返回");
        setLeftOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button1:
                SlideDialog slideDialog = new SlideDialog(getContext());

                slideDialog.setContentView(SlideDialog.PROGRESS);

                slideDialog.setTitles("进度条对话框");

                slideDialog.setMaxProgress(100);
                slideDialog.setProgress(87);

                slideDialog.show();
                break;

            case R.id.button2:

                final SlideDialog slideDialog1 = new SlideDialog(getContext());

                slideDialog1.setContentView(SlideDialog.DEFAULT);

                slideDialog1.setTitles("默认的对话框");
                slideDialog1.setMessage("默认情况下弹出的对话框");

                slideDialog1.setCenterButtonVisible(View.GONE);

                slideDialog1.setRightButton("CANCEL", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        slideDialog1.dismiss();
                    }
                });

                slideDialog1.show();

                break;
        }
    }
}
