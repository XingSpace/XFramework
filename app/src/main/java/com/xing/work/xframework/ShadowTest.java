package com.xing.work.xframework;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by wangxing on 2017/2/12.
 */

public class ShadowTest extends View {

    public ShadowTest(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ShadowTest(Context context) {
        super(context);
        init();
    }

    private void init(){

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.rgb(0x00,0xdd,0xff));
        canvas.drawRect(0,0,720,1200,mPaint);

        Paint paint=new Paint();  //定义一个Paint
        paint.setAntiAlias(true);
        Shader mShader = new RadialGradient(300,300,120,new int[]{Color.argb(100,0x00,0x00,0x00),Color.argb(0,0xff,0xff,0xff)},new float[]{0.9f,1f},Shader.TileMode.MIRROR);
//新建一个线性渐变，前两个参数是渐变开始的点坐标，第三四个参数是渐变结束的点的坐标。连接这2个点就拉出一条渐变线了，玩过PS的都懂。然后那个数组是渐变的颜色。下一个参数是渐变颜色的分布，如果为空，每个颜色就是均匀分布的。最后是模式，这里设置的是循环渐变
        paint.setShader(mShader);
//        paint.setShadowLayer(200,10,10,Color.GRAY);
        canvas.drawCircle(300,300,120,paint);

        Paint m = new Paint();
        m.setAntiAlias(true);
        m.setColor(Color.rgb(0x00,0xdd,0xff));
        canvas.drawCircle(300,300,110,m);
    }
}
