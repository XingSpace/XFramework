package com.xing.work.xframework;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;


/**
 * Created by wangxing on 16/9/11.
 * 关于本类内大家可以从图中看到，这是一个异形的Progress
 *
 * 首先来说下怎么使用：
 *          0.将本类拉到你自己工程目录下（这个是废话。。。
 *          1.随后，可以在你的布局文件中加入这个view（好像也是废话。。。
 *          2.好吧重点来了。。。在给本控件的大小赋值时需要注意几点，第一不能用match_parent,但可以使用wrap_content
 *            如果选择使用wrap_content，那么本控件就会使用默认的500px大小。如果默认大小无法满足需求，那么有两种方法可供修改
 *            第一种：在xml布局文件中将layout_width和layout_height设置以dp为单位的大小，而且尽量使长和宽相等
 *            第二种：在代码中修改大小。。。本控件提供了一个叫setSize的方法，传入一个int类型数字，这个数字将被用于设置长和宽（长和宽相等
 *          3.既然叫progress，肯定要有进度条功能啦对吧。本控件提供一个叫setMax和setProgress的方法来实现这两个功能
 *            而且内，还有一个叫OnProgressComplete的接口。。。用来向外界传达本宝宝已经加载好啦
 *            通过setOnProgressComplete来配置这个接口
 *
 * 实现的原理：
 *          String s1 = attributeSet.getAttributeValue("http://schemas.android.com/apk/res/android","layout_width");
 *          String s2 = attributeSet.getAttributeValue("http://schemas.android.com/apk/res/android","layout_height");
 *          上面两条可以解析出xml中的宽高设定，根据这两个设定，可以确定view的大小（具体参见init方法
 *
 *          setData方法,在init方法中有一次调用，在setSize中也有一次调用。
 *          这个方法的作用就是对本类中所需要的图像绘制数据进行一次更新（在使用过程中无需手动调用，反正private类型我们也调用不了
 *          其次，并不是更新完数据就万事大吉的，更新完还得应用到绘制过程才能算是生效
 *          我们都知道view的绘制是在onDraw方法中的（不知道的话。。。现在知道了吧），而setData方法并没有触发view的重绘
 *          那么重绘代码在哪内。。。嘿嘿嘿，就在init方法中的handler和runnable中，handler每隔一毫秒调用一次runnable
 *          而在runnable中会根据一个flag分别调用两个方法moveLeftCircle和moveRightCircle...这两个方法在结束时都调用了invalidate()
 *          用于强制重绘。。。
 *
 *          最后，仔细观察moveLeftCircle和moveRightCircle方法会发现。。。其中还改变了flag的值和两个分别叫leftR、rightR的值
 *          那么这几个boolean类型的变量有啥用内。。。
 *          其作用呢，是这样的。。。本控件中的两个圆，在同一时间只能移动一个，那么就需要一个值来判断当下应该移动谁
 *          其次，每个圆有两个移动方向，leftR和rightR的作用就是控制每一次移动的方向
 *
 *
 */
public class HeartProgress extends View {

    private Paint mPaint,paintBG,paintCircle,mPaintText;

    private final int DEFAULT_WIDTH = 500;

    private final int DEFAULT_HEIGHT = 500;

    private int MAX_WIDTH;

    private int MAX_HEIGHT;

    private int RectSize = 200;

    private int CircleRadius = 100;

    /**
     * 这里一共6个数组，其作用在于搞事！搞事！搞事！！！
     * 咳咳，其作用在于分别记录两个圆的固定坐标
     * 比如leftCenter就表示这个圆的当前位置，leftCenterEnd表示这个圆从左上角移动到右下角时的最终坐标（即为右下角的圆心坐标
     * leftCenterAct表示在初始化时，这个圆的起始位置
     */
    private float[] leftCenter,rightCenter,leftCenterEnd,rightCenterEnd,leftCenterAct,rightCenterAct;

    private float moveUnit;

    private int mDuration = 80;

    private boolean leftR = true,rightR = true,flag=true;

    private String text = "0%";

    private int Max = 100;

    private int progress = 0;

    private OnProgressComplete onProgressComplete;

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

        if (attributeSet!=null) {
            String s1 = attributeSet.getAttributeValue("http://schemas.android.com/apk/res/android", "layout_width");
            String s2 = attributeSet.getAttributeValue("http://schemas.android.com/apk/res/android", "layout_height");

            if ((s1.equals("-2") && s2.equals("-2"))
                    || (s1.equals("-1") && s2.equals("-1"))) {
                MAX_WIDTH = DEFAULT_WIDTH;
                MAX_HEIGHT = DEFAULT_HEIGHT;
            } else if (s1.equals("wrap_content") | s2.equals("wrap_content")) {
                //为了适配android Studio的脑残反优化而添加的代码
                MAX_WIDTH = DEFAULT_WIDTH;
                MAX_HEIGHT = DEFAULT_HEIGHT;
            } else {

                s1 = s1.replaceAll("[a-zA-Z]", "");
                s2 = s2.replaceAll("[a-zA-Z]", "");

                MAX_WIDTH = sp2px(Float.parseFloat(s1));
                MAX_HEIGHT = sp2px(Float.parseFloat(s2));

            }
            CircleRadius = MAX_WIDTH/5;//把圆的半径设置好
            RectSize = CircleRadius*2;//把正方形的边长设置好
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
                handler.postDelayed(this,3);
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

    public synchronized void setProgress(int progress){
        this.progress = progress;

        if (this.progress >= this.Max){
            if (this.onProgressComplete != null){
                this.onProgressComplete.onComplete();
            }
        }

        int i = (int) ((float)this.progress/(float)this.Max * 100.0f);

        text = i+"%";

    }

    public void setOnProgressComplete(OnProgressComplete onProgressComplete){
        this.onProgressComplete = onProgressComplete;
    }

    public interface OnProgressComplete{
        void onComplete();
    }

    private void calcMove(){
        float temp = Math.abs(leftCenter[0]-leftCenterEnd[0]);

        moveUnit = temp / mDuration;
    }

    private synchronized void moveLeftCircle(){
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

    private synchronized void moveRightCircle(){
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
