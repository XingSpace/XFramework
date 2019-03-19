package com.xing.work.xframework

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

/**
 * Created by wangxing on 2017/2/12.
 */

class ShadowTest : View {

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context) : super(context) {
        init()
    }

    private fun init() {

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val mPaint = Paint()//测试的时候才能把new Paint放在onDraw里面
        mPaint.isAntiAlias = true
        mPaint.color = Color.rgb(0x00, 0xdd, 0xff)
        canvas.drawRect(0f, 0f, 720f, 1200f, mPaint)

        val paint = Paint()  //定义一个Paint
        paint.isAntiAlias = true
        val mShader = RadialGradient(300f, 300f, 122f, intArrayOf(Color.argb(100, 0x00, 0x00, 0x00), Color.argb(0, 0xff, 0xff, 0xff)), floatArrayOf(0.9f, 1f), Shader.TileMode.MIRROR)
        //新建一个线性渐变，前两个参数是渐变开始的点坐标，第三四个参数是渐变结束的点的坐标。连接这2个点就拉出一条渐变线了，玩过PS的都懂。然后那个数组是渐变的颜色。下一个参数是渐变颜色的分布，如果为空，每个颜色就是均匀分布的。最后是模式，这里设置的是循环渐变
        paint.shader = mShader
        //        paint.setShadowLayer(200,10,10,Color.GRAY);
        canvas.drawCircle(300f, 300f, 122f, paint)

        val m = Paint()
        m.isAntiAlias = true
        m.color = Color.rgb(0x00, 0xdd, 0xff)
        canvas.drawCircle(300f, 300f, 110f, m)
    }
}
