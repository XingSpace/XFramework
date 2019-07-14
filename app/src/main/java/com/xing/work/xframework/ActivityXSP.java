package com.xing.work.xframework;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.cjt2325.cameralibrary.JCameraView;
import com.cjt2325.cameralibrary.lisenter.JCameraLisenter;

public class ActivityXSP extends ActivityBase {

    private JCameraView mJCameraView;

    private String url;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        } else {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(option);
        }
        setContentView(R.layout.activity_xsp);
        setBottomBarVisible(false);
    }

    @Override
    public void findViews() {
        mJCameraView = (JCameraView) findViewById(R.id.cameraview);
        mJCameraView.setJCameraLisenter(new JCameraLisenter() {
            @Override
            public void captureSuccess(Bitmap bitmap) {

            }

            @Override
            public void recordSuccess(String url) {
                ActivityXSP.this.url = url;
            }

            @Override
            public void quit() {
                ActivityXSP.this.finish();
            }
        });
//        mJCameraView.setCameraViewListener(new JCameraView.CameraViewListener() {
//            @Override
//            public void quit() {
//                //返回按钮的点击时间监听
//                ActivityXSP.this.finish();
//            }
//            @Override
//            public void captureSuccess(Bitmap bitmap) {
//                //获取到拍照成功后返回的Bitmap
//            }
//            @Override
//            public void recordSuccess(String url) {
//                //获取成功录像后的视频路径
//            }
//        });
    }

    @Override
    public void init() {

    }
}
