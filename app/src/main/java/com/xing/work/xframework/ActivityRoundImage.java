package com.xing.work.xframework;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.makeramen.roundedimageview.Corner;
import com.makeramen.roundedimageview.RoundedImageView;

public class ActivityRoundImage extends ActivityBase {

    private RoundedImageView imageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riv);
    }

    @Override
    public void findViews() {
        imageView = (RoundedImageView) findViewById(R.id.riv);
//        imageView.setCornerRadius(0f,50f,0f,50f);

        float radius = imageView.getCornerRadius(Corner.TOP_LEFT);
        Log.d("我草草"," "+radius);

    }

    @Override
    public void init() {

    }
}
