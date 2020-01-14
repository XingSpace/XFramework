package com.xing.work.xframework.PlaneWar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * 敌人的飞机哦
 */
public class Enemy extends View {

    private Paint paint;

    private int width,height;

    private boolean isFirst = true;

    private FrameLayout.LayoutParams fl;

    private boolean isFlying = true;

    private OnEnemyFly onEnemyFly;

    public Enemy(Context context) {
        this(context,null);
    }

    public Enemy(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(1f);
        paint.setColor(Color.GREEN);
    }

    /**
     * 控制敌机的位置
     */
    public void setLocation(int x,int y){
        fl = getFl();
        if (isFirst){
            fl.topMargin = y;
            fl.leftMargin = x;
            isFirst = false;
            handler.sendEmptyMessage(0);
        }else{

            if (fl.topMargin > ((ViewGroup)getParent()).getHeight()){
                stop();
                if (onEnemyFly != null){
                    onEnemyFly.onOutOfScreen(this);
                }
            }

            fl.topMargin = fl.topMargin + (int) sp2px(5);
        }
        setLayoutParams(fl);
        if (onEnemyFly!= null){
            onEnemyFly.onFlying(this,getFl());
        }
    }
    public void setLocation(){
        setLocation(0,0);
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (isFlying){
                setLocation();
                handler.sendEmptyMessageDelayed(0,30);
            }
        }
    };

    public void stop(){
        isFlying = false;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        width = (int) sp2px(30f);
        height = (int) sp2px(30f);
        setMeasuredDimension(width,height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(0,0,width,height,paint);
    }

    public FrameLayout.LayoutParams getFl(){
        return (FrameLayout.LayoutParams) getLayoutParams();
    }

    public interface OnEnemyFly{
        void onFlying(Enemy enemy,FrameLayout.LayoutParams fl);
        void onOutOfScreen(Enemy enemy);
    }

    public void setOnEnemyFly(OnEnemyFly onEnemyFly){
        this.onEnemyFly = onEnemyFly;
    }

    /**
     * 将sp值转换为px值
     */
    private float sp2px(float spValue) {
        final float fontScale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return spValue * fontScale + 0.5f;
    }
}
