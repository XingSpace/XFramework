package com.xing.work.xframework.PlaneWar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 游戏背景
 */
public class WarBackground extends View implements Runnable{

    private int height,width;//背景的宽高

    private List<Coor> list;

    private Paint paint;

    private Random random;

    private Coor coordinate;

    private boolean isGetdata = false;

    private boolean isCycle = true;

    public WarBackground(Context context) {
        this(context,null);
    }

    public WarBackground(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(1f);
        list = new ArrayList<Coor>();
        random = new Random();
        coordinate = new Coor();
    }

    private Handler handler = new Handler();

    public void start(){
        handler.postDelayed(this,30);
    }

    //没什么卵用的方法
    public void stop(){
        isCycle = false;
    }

    @Override
    public void run() {
        if (isCycle) {
            for (int i = 0; i < list.size(); i++) {

                if (list.get(i).getY() < height) {
                    list.get(i).setY(list.get(i).getY() + (int) sp2px(1));
                } else {
                    list.get(i).setY(0);
                }

            }
            invalidate();
            handler.postDelayed(this, 30);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (isGetdata == false){
            height = getMeasuredHeight();
            width = getMeasuredWidth();
            for (int i = 0; i< 30;i++){
//            coordinate = new Coordinate(random.nextInt(width), random.nextInt(height));
//                coordinate.setX(random.nextInt(width));
//                coordinate.setY(random.nextInt(height));
                Coor coor = new Coor();
                coor.setY(random.nextInt(height));
                coor.setX(random.nextInt(width));
                list.add(coor);
            }
            isGetdata = true;
            start();
//            Log.d("wocao","线程已经启动");
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (int i = 0;i<list.size();i++){
            drawStar(canvas,list.get(i));
        }
//        Log.d("wocao","绘制已经完成");
    }

    private void drawStar(Canvas canvas,Coor coordinate){
        canvas.drawCircle(coordinate.getX(),coordinate.getY(),random.nextInt(3)+1,paint);
    }

    /**
     * 将sp值转换为px值
     */
    private float sp2px(float spValue) {
        final float fontScale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return spValue * fontScale + 0.5f;
    }

}
