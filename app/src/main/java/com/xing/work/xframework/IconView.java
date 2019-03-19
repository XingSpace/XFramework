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
 * Created by wangxing on 2017/1/29.
 */
public class IconView extends View {

    private float mRadius;

    private float mWeight;

    private int mWidth,mHeight;//整个View的实际尺寸

    private float distance;

    private AttributeSet attributeSet;

    private TypedArray typedArray;

    private Paint mPaint;

    private OnClickListener onClickListener;

    public IconView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.attributeSet = attrs;
        init();
    }

    public IconView(Context context) {
        super(context);
        init();
    }

    private void init(){
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        //检查数据
        if (attributeSet!=null){
            typedArray = getContext().obtainStyledAttributes(attributeSet,R.styleable.IconView);

            String s1 = attributeSet.getAttributeValue("http://schemas.android.com/apk/res/android", "layout_width");
            String s2 = attributeSet.getAttributeValue("http://schemas.android.com/apk/res/android", "layout_height");
            if ((s1.equals("-2") && s2.equals("-2"))
                    | (s1.equals("-1") && s2.equals("-1"))) {
                mHeight = (int) sp2px(24);
                mWidth = (int) sp2px(16);
            } else if (s1.equals("wrap_content") | s2.equals("wrap_content")) {
                //为了适配android Studio的脑残反优化而添加的代码
                mHeight = (int) sp2px(24);
                mWidth = (int) sp2px(16);
            } else {
                s1 = s1.replaceAll("d", "");
                s1 = s1.replaceAll("i", "");
                s1 = s1.replaceAll("p", "");

                s2 = s2.replaceAll("d", "");
                s2 = s2.replaceAll("i", "");
                s2 = s2.replaceAll("p", "");

                mWidth = (int) sp2px(Float.parseFloat(s1));
                mHeight = (int) sp2px(Float.parseFloat(s2));
            }
            //上面这一搓代码用来解析高度。。。

            mWeight = typedArray.getDimension(R.styleable.IconView_iv_weight,sp2px(3f));
            mRadius = mWeight/2f;

            if (mHeight/2 - mRadius < mWidth - mRadius*2){
                distance = mHeight/2;
            }else {
                distance = mWidth - mRadius*2;
            }
            //上面的代码用来选择最大可绘制距离

        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int tempWidth = getMeasuredSize(widthMeasureSpec,true);
        int tempHeight = getMeasuredSize(heightMeasureSpec,false);
        setMeasuredDimension(tempWidth,tempHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(Color.rgb(0x00,0x00,0x00));
        mPaint.setStrokeWidth(2*mRadius);
        canvas.drawLine(mRadius+getPaddingLeft(),distance+getPaddingTop(),distance+getPaddingLeft(),mRadius+getPaddingTop(),mPaint);
        canvas.drawLine(mRadius+getPaddingLeft(),distance+getPaddingTop(),distance+getPaddingLeft(),distance*2-mRadius+getPaddingTop(),mPaint);

//        mPaint.setStrokeWidth(50);
//        mPaint.setColor(Color.rgb(0x91,0x1e,0x61));
        canvas.drawCircle(mRadius+getPaddingLeft(),distance+getPaddingTop(),mRadius,mPaint);
        canvas.drawCircle(distance+getPaddingLeft(),mRadius+getPaddingTop(),mRadius,mPaint);
        canvas.drawCircle(distance+getPaddingLeft(),distance*2-mRadius+getPaddingTop(),mRadius,mPaint);

    }

    public void setOnClickListener(OnClickListener onClickListener){
        this.onClickListener = onClickListener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if (onClickListener!=null){
                    onClickListener.onClick(this);
                }
                break;
        }

        return super.onTouchEvent(event);
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
            retSize = specSize + padding;
        }else{                              // 如果使用wrap_content
            retSize = (isWidth? mWidth + padding : mHeight + padding);
            if(specMode==MeasureSpec.AT_MOST){
                retSize = Math.min(retSize, specSize);
            }
        }
        return retSize;
    }

    /**
     * 将sp值转换为px值
     */
    private float sp2px(float spValue) {
        final float fontScale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return spValue * fontScale + 0.5f;
    }
}
