package com.xing.work.xframework.PlaneWar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * 为了实现子弹带功能加上的类
 */
public class WarBack extends FrameLayout implements RemoveChildView {

    public WarBack(@NonNull Context context) {
        super(context);
    }

    public WarBack(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public WarBack(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @SuppressLint("NewApi")
    public WarBack(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void removeChild(View view) {
        removeView(view);
    }

}
