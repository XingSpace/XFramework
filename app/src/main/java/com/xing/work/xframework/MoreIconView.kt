package com.xing.work.xframework

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View

class MoreIconView:View{

    var mWeight:Float? = null

    var mHeight:Int? = null
    var mWidth:Int? = null

    constructor(context:Context?):this(context,null)
    constructor(context:Context?,attrs:AttributeSet?):super(context,attrs){
        initAttr(attrs)
    }

    /**
     * 解析封装在xml中属性数据
     */
    fun initAttr(attrs: AttributeSet?){
        if (attrs!=null){
            val typedArray = context.obtainStyledAttributes(attrs,R.styleable.MoreIconView)

            var s1 = attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "layout_width")
            var s2 = attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "layout_height")

            if ((s1 == "-2" && s2 == "-2") or (s1 == "-1" && s2 == "-1")) {
                mHeight = sp2px(24f).toInt()
                mWidth = sp2px(16f).toInt()
            } else if ((s1 == "wrap_content") or (s2 == "wrap_content")) {
                //为了适配android Studio的脑残反优化而添加的代码
                mHeight = sp2px(24f).toInt()
                mWidth = sp2px(16f).toInt()
            } else {
                s1 = s1.replace("d","")
                s1 = s1.replace("i", "")
                s1 = s1.replace("p", "")
//
                s2 = s2.replace("d", "")
                s2 = s2.replace("i", "")
                s2 = s2.replace("p", "")

                mWidth = sp2px(java.lang.Float.parseFloat(s1)).toInt()
                mHeight = sp2px(java.lang.Float.parseFloat(s2)).toInt()
            }

        }

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
    }

    /**
     * 将sp值转换为px值
     */
    private fun sp2px(spValue: Float): Float {
        return spValue * context.resources.displayMetrics.scaledDensity + 0.5f
    }
}