package com.xing.work.xframework;

import android.os.Bundle;
import android.view.View;

/**
 * Created by wangxing on 16/10/8.
 */

public class ActivityHP extends ActivityBase {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hp);
    }

    @Override
    public void findViews() {

    }

    @Override
    public void init() {
        setTitle("测试HeartProgress");

        setLeftText("返回");
        setLeftOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setRightVisible(View.GONE);
    }
}
