package com.xing.work.xframework.PlaneWar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * 游戏中的子弹
 */
public class Bullet extends View {

    private int height,width;

    private Paint paint;

    private FrameLayout.LayoutParams fl;

    private boolean isFirst = true;

    private OnBulletShotOut onBulletShotOut;

    private boolean isShotting = true;

    public Bullet(Context context) {
        this(context,null);
    }

    public Bullet(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(1f);
        paint.setColor(Color.RED);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        width = (int) sp2px(5);
        height = (int) sp2px(5);
        setMeasuredDimension(width,height);
    }

    /**
     * 设置子弹的位置
     * @param x
     * @param y
     */
    public void setLocation(int x,int y){
        fl = getFl();

        if (isFirst){
            fl.topMargin = y;
            fl.leftMargin = x - (int) sp2px(5)/2;
            //当代码执行到这一块的时候，还没有对宽高赋值，所以拿不到数据的
            //于是我开启了骚操作，直接写死
//            Log.d("wocao",x+" . "+width);
            isFirst = false;
            if (onBulletShotOut!=null){
                onBulletShotOut.onShotBegin(Bullet.this);
            }
            handler.sendEmptyMessage(0);
        }else {

            if (fl.topMargin + getHeight() <= 0){
                if (onBulletShotOut!=null){
                    onBulletShotOut.onOutofScreen(Bullet.this);
                }
                stop();
                return;
            }

            fl.topMargin = (int) (fl.topMargin - sp2px(10));
//            Log.d("wocao",toString()+" "+fl.topMargin+ " . "+sp2px(10));

            if (onBulletShotOut!=null){
                onBulletShotOut.onShotting(Bullet.this,fl);
            }

        }
        setLayoutParams(fl);
    }

    /**
     * 子弹停掉自己的运动线程
     */
    public void stop(){
        isShotting = false;
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (isShotting){
                setLocation(0,0);
                handler.sendEmptyMessageDelayed(0,5);
            }
        }
    };

    public FrameLayout.LayoutParams getFl(){
        return (FrameLayout.LayoutParams) getLayoutParams();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBullet(canvas);
    }

    private void drawBullet(Canvas c){
//        c.drawCircle((float)width/2f,(float) height/2f,sp2px(2.5f),paint);
        c.drawRect(0,0,width,height,paint);
    }

    public void setOnBulletShotOut(OnBulletShotOut onBulletShotOut){
        this.onBulletShotOut = onBulletShotOut;
    }

    public interface OnBulletShotOut{
        void onOutofScreen(Bullet bullet);//当子弹飞出屏幕了

        void onShotBegin(Bullet bullet);//当子弹刚刚出膛

        void onShotting(Bullet bullet,FrameLayout.LayoutParams fl);//当子弹还在飞行过程中，每一次更新，调用一次
    }

    /**
     * 将sp值转换为px值
     */
    private float sp2px(float spValue) {
        final float fontScale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return spValue * fontScale + 0.5f;
    }
}
