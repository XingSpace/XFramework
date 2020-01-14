package com.xing.work.xframework;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by wangxing on 17/1/3.
 * 自定义的SeekBar
 */
public class XSeekBar extends View {

    private Paint mPaint;

    private float mBlank;//留白

    private float[] mCenter = new float[2];//圆心坐标

    private float mRadius;//控制球的半径

    private float mMaxWidth, mProgressWeight;//进度条的高度

    private float mMaxWeight;//进度条的宽度;

    private float mMaxTop,mMaxLeft,mMaxBottom,mMaxRight;//Max进度条的大小,属于始终不变的
    private float mProgressTop,mProgressLeft,mProgressBottom,mProgressRight;//Progress进度条的大小,其Right值跟随中心控制球改变

    private int mMaxColor;
    private int mProgressColor;
    private int mBCircleColor;

    private int mSCircleColor;//控件绘制过程中需要的色彩参数

    private int mProgress;//表示已完成的进度

    private int mMax;//表示进度条的最大值

    private float mUnit;//两个Progress单位的中间距离是多少

    private int mWidth,mHeight;//控件的实际大小值,以像素为单位

    private int weight;//用来记录理想的控件高度

    private boolean isTouch;//用户是否触碰到控制球

    private boolean isVertical;//是否为垂直控制条,true表示垂直,false表示水平

    public static final int Vertical = 0x0002;

    public static final int Horizontal = 0x0001;

    private OnXSeekBarListener onXSeekBarListener;

    private AttributeSet attributeSet;

    private TypedArray typedArray;

    public XSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        attributeSet = attrs;
        init();
    }

    public XSeekBar(Context context) {
        super(context);
        init();
    }

    public void init(){
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mBlank = sp2px(2);

        if (attributeSet!=null){
            typedArray = getContext().obtainStyledAttributes(attributeSet, R.styleable.XSeekBar);

            mMax = typedArray.getInteger(R.styleable.XSeekBar_xs_max,100);

            mProgress = typedArray.getInteger(R.styleable.XSeekBar_xs_progress,0);

            mRadius = typedArray.getDimension(R.styleable.XSeekBar_xs_radius,sp2px(14f));

            mMaxWeight = typedArray.getDimension(R.styleable.XSeekBar_xs_MaxHeight,sp2px(7f));

            mProgressWeight = typedArray.getDimension(R.styleable.XSeekBar_xs_ProgressHeight,sp2px(7f));

            mMaxColor = typedArray.getColor(R.styleable.XSeekBar_xs_MaxColor,Color.LTGRAY);

            mProgressColor = typedArray.getColor(R.styleable.XSeekBar_xs_ProgressColor,Color.argb(255,0x5a,0xc2,0xdd));

            mBCircleColor = typedArray.getColor(R.styleable.XSeekBar_xs_BCircleColor,Color.argb(255,0x5a,0xc2,0xdd));

            mSCircleColor = typedArray.getColor(R.styleable.XSeekBar_xs_SCircleColor,Color.argb(255,0xff,0xff,0xff));

            int temp = typedArray.getInt(R.styleable.XSeekBar_xs_orientation,1);
            isVertical = temp == 0;

            typedArray.recycle();
        }else {
            //万一没有数据可以用以填充,就要动用默认值了
            mMax = 100;
            mProgress = 0;

            mRadius = sp2px(10f);
            mMaxWeight = sp2px(7f);
            mProgressWeight = sp2px(7f);

            mMaxColor = Color.LTGRAY;
            mProgressColor = Color.argb(255,0x5a,0xc2,0xdd);
            mBCircleColor = Color.argb(255,0x5a,0xc2,0xdd);
            mSCircleColor = Color.argb(255,0xff,0xff,0xff);

            isVertical = false;
        }

        weight = (int) ((mBlank + mRadius)*2);//设置好理想高度

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mWidth = this.getMeasuredSize(widthMeasureSpec, true);
        mHeight = this.getMeasuredSize(heightMeasureSpec, false);

        if (isVertical){
            setMeasureVertical();
        }else {
            setMeasureHorizontal();
        }

        setMeasuredDimension(mWidth,mHeight);
    }

    /**
     * 按照水平绘制
     */
    private void setMeasureHorizontal(){
        mMaxWidth = mWidth - getPaddingLeft() - getPaddingRight() - (mRadius+mBlank)*2;
        //设置好进度条的总长度

        mUnit = mMaxWidth / mMax;//设置单位距离

        float[] f = new float[2];
        f[1] = mBlank+mRadius;//设置圆心的Y轴坐标

        f[0] = f[1] + mUnit*mProgress;

        setCenter(f);

        mMaxTop = (mBlank + mRadius) - mMaxWeight /2;
        mMaxLeft = (mBlank + mRadius);
        mMaxBottom = (mBlank + mRadius) + mMaxWeight /2;
        mMaxRight = mMaxWidth + (mRadius+mBlank);

        mProgressBottom = (mBlank + mRadius) + mProgressWeight /2;
        mProgressRight = f[0];//把已完成进度条设置为与圆心的X轴一致
        mProgressTop = (mBlank + mRadius) - mProgressWeight /2;
        mProgressLeft = (mBlank + mRadius);
    }

    /**
     * 按照垂直绘制
     */
    private void setMeasureVertical(){
        mMaxWidth = mHeight - getPaddingTop() - getPaddingBottom() - (mRadius+mBlank)*2;
        //设置好进度条的总长度

        mUnit = mMaxWidth / mMax;//设置单位距离

        float[] f = new float[2];
        f[0] = mBlank+mRadius;//设置圆心的X轴坐标
        f[1] = mMaxWidth + f[0] - mUnit*mProgress;

        setCenter(f);

        mMaxTop = (mBlank + mRadius);
        mMaxLeft = (mBlank + mRadius) - mMaxWeight / 2;
        mMaxBottom = (mBlank + mRadius) + mMaxWidth;
        mMaxRight = (mRadius+mBlank) + mMaxWeight / 2;

        mProgressBottom = (mBlank + mRadius) + mMaxWidth;//把已完成进度条设置为与圆心的Y轴一致
        mProgressRight = (mRadius+mBlank) + mProgressWeight / 2;
        mProgressTop = f[1];
        mProgressLeft = (mBlank + mRadius) - mProgressWeight / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawMax(canvas);
        drawProgress(canvas);
        drawCircle(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (isVertical){
            onTouchForVertical(event);
        }else {
            onTouchForHorizontal(event);
        }

        return true;
    }

    private void onTouchForHorizontal(MotionEvent event){
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                float startX = event.getX();
                float startY = event.getY();
                if (isCollsion(mCenter[0]+getPaddingLeft(),mCenter[1]+getPaddingTop(),startX,startY,mRadius+mBlank,0)){
                    isTouch = true;
                }else {
                    isTouch = false;
                    int temp = (int) ((event.getX()-mBlank-mRadius-getPaddingLeft())/mUnit+0.5f);
                    if(temp >= mMax){
                        temp = mMax;
                    }else if (temp <= 0){
                        temp = 0;
                    }
                    update(temp);
                }
                break;

            case MotionEvent.ACTION_MOVE:
                if (isTouch){
                    int temp = (int) ((event.getX()-mBlank-mRadius-getPaddingLeft())/mUnit+0.5f);

                    if(temp >= mMax){
                        temp = mMax;
                    }else if (temp <= 0){
                        temp = 0;
                    }
                    update(temp);
                }
                break;

            case MotionEvent.ACTION_UP:
                isTouch = false;
                break;
        }
    }

    private void onTouchForVertical(MotionEvent event){
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                float startX = event.getX();
                float startY = event.getY();
                if (isCollsion(mCenter[0]+getPaddingLeft(), mCenter[1]+getPaddingTop(), startX, startY, mRadius + mBlank, 0)) {
                    isTouch = true;
                } else {
                    isTouch = false;
                    int temp = (int) ((mMaxWidth+mBlank+mRadius - event.getY()+getPaddingTop()) / mUnit + 0.5f);

                    if (temp >= mMax) {
                        temp = mMax;
                    } else if (temp <= 0) {
                        temp = 0;
                    }
                    update(temp);
                }
                break;

            case MotionEvent.ACTION_MOVE:
                if (isTouch) {
                    int temp = (int) ((mMaxWidth+mBlank+mRadius - event.getY()+getPaddingTop()) / mUnit + 0.5f);

                    if (temp >= mMax) {
                        temp = mMax;
                    } else if (temp <= 0) {
                        temp = 0;
                    }
                    update(temp);
                }
                break;

            case MotionEvent.ACTION_UP:
                isTouch = false;
                break;
        }
    }

    /**
     * 刷新界面
     */
    private synchronized void update(int temp){
        this.mProgress = temp;
        setCenter(mProgress);
        invalidate();//
        if (onXSeekBarListener!=null){
            onXSeekBarListener.onProgressChange(this,mProgress,true);
        }
    }

    private void drawMax(Canvas canvas){
        //绘制进度总体长度
        mPaint.setColor(mMaxColor);
        canvas.drawRect(mMaxLeft + getPaddingLeft(),mMaxTop + getPaddingTop(),mMaxRight + getPaddingLeft(),mMaxBottom + getPaddingTop(),mPaint);
    }

    private void drawProgress(Canvas canvas){
        //绘制已完成进度
        mPaint.setColor(mProgressColor);
        canvas.drawRect(mProgressLeft + getPaddingLeft(),mProgressTop + getPaddingTop(),mProgressRight + getPaddingLeft(),mProgressBottom + getPaddingTop(),mPaint);
    }

    private void drawCircle(Canvas canvas){
        //使用圆形渐变阴影
        //Shader shader = new RadialGradient(mCenter[0]+getPaddingLeft(),mCenter[1]+getPaddingTop(),mRadius+mBlank,new int[]{Color.BLACK,Color.argb(0,0xff,0xff,0xff)},new float[]{0.9f,1.0f}, Shader.TileMode.CLAMP);
        //mPaint.setShader(shader);
        //canvas.drawCircle(mCenter[0]+getPaddingLeft(),mCenter[1]+getPaddingTop(),mRadius+mBlank,mPaint);

        //绘制主要的蓝色圆点
        mPaint.setColor(mBCircleColor);
        mPaint.setShader(null);
        canvas.drawCircle(mCenter[0]+getPaddingLeft(),mCenter[1]+getPaddingTop(),mRadius,mPaint);

        //绘制中心白色圆点
        mPaint.setColor(mSCircleColor);
        canvas.drawCircle(mCenter[0]+getPaddingLeft(),mCenter[1]+getPaddingTop(),mRadius/3,mPaint);
    }

    public int getProgress(){
        return mProgress;
    }

    public int getMax(){
        return mMax;
    }

    public synchronized void setProgress(int progress){
        this.mProgress = progress;
        setCenter(this.mProgress);
        invalidate();
        if (onXSeekBarListener!=null){
            onXSeekBarListener.onProgressChange(this,mProgress,false);
        }
    }

    public void setMax(int max){
        this.mMax = max;
        requestLayout();
    }
    /**
     * 通过本方法来设置圆心的位置
     * @param f
     */
    private synchronized void setCenter(float[] f){
        if (isVertical){
            mCenter[0] = f[0];
            mCenter[1] = f[1];
            mProgressTop = f[1];
        }else {
            mCenter[0] = f[0];
            mCenter[1] = f[1];
            mProgressRight = f[0];
        }

    }

    /**
     * 通过本方法来设置圆心的X轴位置,以及mProgressRight
     * @param i
     */
    private synchronized void setCenter(int i){
        if (isVertical){
            mCenter[1] = mBlank+ mRadius +mMaxWidth - i*mUnit;
            mProgressTop = mCenter[1];
        }else {
            mCenter[0] = mBlank+mRadius+i*mUnit;
            mProgressRight = mCenter[0];
        }
    }

    public int getMaxColor() {
        return mMaxColor;
    }

    public void setMaxColor(int mMaxColor) {
        this.mMaxColor = mMaxColor;
        invalidate();
    }

    public int getProgressColor() {
        return mProgressColor;
    }

    public void setProgressColor(int mProgressColor) {
        this.mProgressColor = mProgressColor;
        invalidate();
    }

    public int getBCircleColor() {
        return mBCircleColor;
    }

    /**
     * @param mBCircleColor 这里可以使用Color.argb(alpha , r , g , b);来设置
     *                      第一个参数为透明度，其余三个为rgb的色彩值，使用十六进制来配置（例如0xff，0x32）；
     */
    public void setBCircleColor(int mBCircleColor) {
        this.mBCircleColor = mBCircleColor;
        invalidate();
    }

    public int getSCircleColor() {
        return mSCircleColor;
    }

    public void setSCircleColor(int mSCircleColor) {
        this.mSCircleColor = mSCircleColor;
        invalidate();
    }

    /**
     * @param weight 表示进度条的肥胖度
     */
    public void setMaxWeight(int weight){
        this.mMaxWeight = weight;
        requestLayout();
    }

    /**
     * @param weight 表示进度条的肥胖度
     */
    public void setProgressWeight(int weight){
        this.mProgressWeight = weight;
        requestLayout();
    }

    /**
     * @param radius 设置控制球的半径
     */
    public void setRadius(int radius){
        this.mRadius = radius;
        requestLayout();
    }

    /**
     * @param orientation 参数可选本类自带的Vertical，Horizontal参数。
     */
    public void setOrientation(int orientation){
        switch (orientation){
            case Vertical:
                isVertical = true;
                break;
            case Horizontal:
                isVertical = false;
                break;
        }
        requestLayout();
    }

    public void setOnSeekBarListener(OnXSeekBarListener onSeekBarListener){
        this.onXSeekBarListener = onSeekBarListener;
    }

    public interface OnXSeekBarListener{
        void onProgressChange(XSeekBar xSeekBar, int progress, boolean isUser);
    }

    /**
     * 计算控件的实际大小
     * @param length onMeasure方法的参数，widthMeasureSpec或者heightMeasureSpec
     * @param isWidth 是宽度还是高度
     * @return int 计算后的实际大小
     */
    private int getMeasuredSize(int length, boolean isWidth){
        // 模式
        int specMode = MeasureSpec.getMode(length);
        // 尺寸
        int specSize = MeasureSpec.getSize(length);
        // 计算所得的实际尺寸，要被返回
        int retSize = 0;
        // 得到两侧的padding（留边）
        int padding = (isWidth? getPaddingLeft()+getPaddingRight():getPaddingTop()+getPaddingBottom());


        // 对不同的指定模式进行判断
        if(specMode==MeasureSpec.EXACTLY){  // 显式指定大小，如40dp或fill_parent
            retSize = specSize;
        }else{                              // 如使用wrap_content
            if (isVertical){
                retSize = (isWidth? weight + padding : specSize + padding);
            }else {
                retSize = (isWidth? specSize + padding : weight + padding);
            }
            if(specMode==MeasureSpec.AT_MOST){
                retSize = Math.min(retSize, specSize);

            }
        }

        return retSize;
    }

    /**
     * 将sp值转换为px值
     */
    private int sp2px(float spValue) {
        final float fontScale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     *
     * @param ballCenterX 第一个圆的X坐标
     * @param ballCenterY 第一个圆的Y坐标
     * @param TballCenterX 第二个圆的X坐标
     * @param TballCenterY 第二个圆的Y坐标
     * @param r1 第一个圆的半径
     * @param r2 第二个圆的半径
     * @return 返回一个布尔数，true为发生了碰撞，false为没有发生碰撞
     */
    public static boolean isCollsion(float ballCenterX
            ,float ballCenterY
            ,float TballCenterX
            ,float TballCenterY
            ,float r1
            ,float r2
    ){

        float rightAngle = Math.abs(ballCenterX - TballCenterX) * Math.abs(ballCenterX - TballCenterX);
        float rightAngle2 = Math.abs(ballCenterY - TballCenterY) * Math.abs(ballCenterY - TballCenterY);
        float hypot = (r1+r2)*(r1+r2);

        return rightAngle + rightAngle2 < hypot;
    }

}
