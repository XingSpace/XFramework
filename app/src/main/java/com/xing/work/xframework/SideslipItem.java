package com.xing.work.xframework;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by wangxing on 16/10/31.
 */

public class SideslipItem extends LinearLayout {

    private RelativeLayout rl_Left,rl_Right;
    private LinearLayout linearLayout;

    private int startX;//记录手指的起始位置

    private int linearStartX;

    public SideslipItem(Context context) {
        super(context);
        init();
    }

    public SideslipItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        LayoutInflater.from(getContext()).inflate(R.layout.sideslip_item,this,true);

        rl_Left = (RelativeLayout)findViewById(R.id.bottom_left);
        rl_Right = (RelativeLayout)findViewById(R.id.bottom_right);
        linearLayout = (LinearLayout)findViewById(R.id.main);
        Log.d("已经执行完毕","执行加载xml");
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                startX = (int) event.getX();
                linearStartX = (int) linearLayout.getX();
                Log.d("侧滑控件中","被点击到");
                break;

            case MotionEvent.ACTION_MOVE:
                int tempX = (int) event.getX();

                if (tempX > startX){
                    //这里的目的是区分到底是在往左滑动还是往右滑动
                    //如果tempX大于startX就是正在向右滑动
                    linearLayout.setX(linearStartX+(tempX-startX));
                    Log.d("linearLayout",linearStartX+(tempX-startX)+"右");
                }else {
                    linearLayout.setX(linearStartX-(startX-tempX));
                    Log.d("linearLayout",linearStartX-(startX-tempX)+"左");
                }


                break;

            case MotionEvent.ACTION_UP:
                break;
        }

        return false;
    }
}
