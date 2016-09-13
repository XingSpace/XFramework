package com.xing.app.xframework;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;


/**
 * Created by wangxing on 16/9/11.
 */
public class HeartProgress extends View {

    private Paint mPaint,paintBG,paintCircle,mPaintText;

    private float angle = 0;

    private final int DEFAULT_WIDTH = 500;

    private final int DEFAULT_HEIGHT = 500;

    private int MAX_WIDTH;

    private int MAX_HEIGHT;

    private int RectSize = 200;

    private int CircleRadius = 100;

    private float[] leftCenter,rightCenter,leftCenterEnd,rightCenterEnd,leftCenterAct,rightCenterAct;

    private float moveUnit;

    private int mDuration = 80;

    private boolean leftR = true,rightR = true,flag=true;

    private String text = "0%";

    private int Max = 100;

    private int progress = 0;

    public HeartProgress(Context context){
        this(context,null);
    }

    public HeartProgress(Context context, AttributeSet attributeSet){
        this(context,attributeSet,0);
    }

    public HeartProgress(Context context, AttributeSet attributeSet, int defStyleAttr){
        super(context,attributeSet,defStyleAttr);
        init(attributeSet);
    }

    private void init(AttributeSet attributeSet){

        if (attributeSet!=null){
            String s1 = attributeSet.getAttributeValue("http://schemas.android.com/apk/res/android","layout_width");
            String s2 = attributeSet.getAttributeValue("http://schemas.android.com/apk/res/android","layout_height");

            Log.d("layout",s1+" "+s2);

            if (s1.equals("-2") && s2.equals("-2")){
                MAX_WIDTH = DEFAULT_WIDTH;
                MAX_HEIGHT = DEFAULT_HEIGHT;
            }else {
                s1 = s1.replaceAll("dip","");
                s2 = s2.replaceAll("dip","");

                MAX_WIDTH = sp2px( Float.parseFloat(s1) );
                MAX_HEIGHT = sp2px( Float.parseFloat(s2) );
            }

            CircleRadius = MAX_WIDTH/5;
            RectSize = CircleRadius*2;
        }

        setData();

        final Handler handler = new Handler();

        final Runnable runnable = new Runnable(){
            @Override
            public void run() {
                if(flag){
                    moveRightCircle();
                }else {
                    moveLeftCircle();
                }
                handler.postDelayed(this,1);
            }
        };

        handler.postDelayed(runnable,1);

    }

    /**
     * @param size 传入一个边长
     */
    public void setSize(int size){
        MAX_WIDTH = size;
        MAX_HEIGHT = size;
        setData();
    }

    /**
     * 数据填充。。。差不多这样理解吧
     */
    private void setData(){
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(10);

        paintBG = new Paint();
        paintBG.setAntiAlias(true);
        paintBG.setColor(Color.argb(255,247,175,62));

        paintCircle = new Paint();
        paintCircle.setAntiAlias(true);
        paintCircle.setColor(Color.BLUE);

        mPaintText = new Paint();
        mPaintText.setAntiAlias(true);
        mPaintText.setColor(Color.WHITE);
        mPaintText.setTextSize(CircleRadius/2);

        final double r = Math.PI/180;

        leftCenter = new float[]{MAX_WIDTH/2- (float)(Math.cos(45*r)*CircleRadius),
                MAX_HEIGHT/2-(float)(Math.cos(45*r)*CircleRadius)};

        rightCenter = new float[]{MAX_WIDTH/2 + (float)(Math.cos(45*r)*CircleRadius),
                MAX_HEIGHT/2 - (float)(Math.cos(45*r)*CircleRadius)};

        leftCenterAct = new float[]{MAX_WIDTH/2- (float)(Math.cos(45*r)*CircleRadius),
                MAX_HEIGHT/2-(float)(Math.cos(45*r)*CircleRadius)};

        leftCenterEnd = new float[]{MAX_WIDTH/2 + (float)(Math.cos(45*r)*CircleRadius),
                MAX_HEIGHT/2 + (float)(Math.cos(45*r)*CircleRadius)};

        rightCenterAct = new float[]{MAX_WIDTH/2 + (float)(Math.cos(45*r)*CircleRadius),
                MAX_HEIGHT/2 - (float)(Math.cos(45*r)*CircleRadius)};

        rightCenterEnd = new float[]{MAX_WIDTH/2 - (float)(Math.cos(45*r)*CircleRadius),
                MAX_HEIGHT/2 + (float)(Math.cos(45*r)*CircleRadius)};

        calcMove();//计算出没一毫秒需要移动多少
    }

    public void setMax(int max){
        this.Max = max;
    }

    public void setProgress(int progress){
        this.progress = progress;

        int i = (int) ((float)this.progress/(float)this.Max * 100.0f);

        Log.d("progress",i+" "+progress);

        text = i+"%";

    }

    private void calcMove(){
        float temp = Math.abs(leftCenter[0]-leftCenterEnd[0]);

        moveUnit = temp / mDuration;

        Log.d("moveUnit",""+moveUnit);
    }

    private void moveLeftCircle(){
        if (leftR){
            if (leftCenter[0] < leftCenterEnd[0]){
                leftCenter[0] = leftCenter[0]+moveUnit;
                leftCenter[1] = leftCenter[1]+moveUnit;
            }else {
                leftCenter[0] = leftCenterEnd[0];
                leftCenter[1] = leftCenterEnd[1];
                leftR = false;
                flag = true;
            }
        }else {
            if (leftCenter[0] > leftCenterAct[0]){
                leftCenter[0] = leftCenter[0]-moveUnit;
                leftCenter[1] = leftCenter[1]-moveUnit;
            }else {
                leftCenter[0] = leftCenterAct[0];
                leftCenter[1] = leftCenterAct[1];
                leftR = true;
                flag = true;
            }
        }
        invalidate();
    }

    private void moveRightCircle(){
        if (rightR){
            if (rightCenter[0] > rightCenterEnd[0]){
                rightCenter[0] = rightCenter[0]-moveUnit;
                rightCenter[1] = rightCenter[1]+moveUnit;
            }else {
                rightCenter[0] = rightCenterEnd[0];
                rightCenter[1] = rightCenterEnd[1];
                rightR = false;
                flag = false;
            }
        }else {
            if (rightCenter[0] < rightCenterAct[0]){
                rightCenter[0] = rightCenter[0]+moveUnit;
                rightCenter[1] = rightCenter[1]-moveUnit;
            }else {
                rightCenter[0] = rightCenterAct[0];
                rightCenter[1] = rightCenterAct[1];
                rightR = true;
                flag = false;
            }
        }
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(MAX_WIDTH,MAX_HEIGHT);
//        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
//        if (getParent()!=null){
//            Log.d("onMeasure Parent","不为空");
//            ViewGroup viewGroup = (ViewGroup) getParent();
//            Log.d("onMeasure Size",""+viewGroup.getWidth());
//        }else {
//            Log.d("onMeasure Parent","为空");
//        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawBackG(canvas);
        drawLeftCircle(canvas);
        drawRightCircle(canvas);
        drawRect(canvas);
        drawText(canvas);
    }

    private void drawText(Canvas canvas){
        Rect rect = new Rect();
        float[] f = new float[text.length()];

        int width = 0;

        mPaintText.getTextBounds(text,0,text.length(),rect);
        mPaintText.getTextWidths(text,f);

        int height = rect.height();

        for (float temp:f){
            width += (int) Math.ceil(temp);
        }

        canvas.drawText(text,
                MAX_WIDTH/2 - width/2,
                MAX_HEIGHT/2 + height/2,
                mPaintText);
    }

    private void drawBackG(Canvas canvas){
        canvas.drawRect(0,0,MAX_WIDTH,MAX_HEIGHT,paintBG);
    }

    private void drawRect(Canvas canvas){
        canvas.rotate(45,MAX_WIDTH/2,MAX_HEIGHT/2);
        canvas.drawRect(MAX_WIDTH/2-RectSize/2,
                MAX_HEIGHT/2-RectSize/2,
                MAX_WIDTH/2+RectSize/2,
                MAX_HEIGHT/2+RectSize/2,
                mPaint);
        canvas.rotate(-45,MAX_WIDTH/2,MAX_HEIGHT/2);
    }

    private void drawLeftCircle(Canvas canvas){
        canvas.drawCircle(leftCenter[0],
                leftCenter[1],
                CircleRadius,
                mPaint);

    }

    private void drawRightCircle(Canvas canvas){
        canvas.drawCircle(rightCenter[0],
                rightCenter[1],
                CircleRadius,
                mPaint);
    }

    /**
     * 吧像素转换为sp？（是这个意思吧
     */
    private int sp2px(float spValue) {
        final float fontScale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
}
