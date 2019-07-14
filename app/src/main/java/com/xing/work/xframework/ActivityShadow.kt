package com.xing.work.xframework

import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

/**
 * Created by wangxing on 2017/2/12.
 */

class ActivityShadow : ActivityBase(),View.OnClickListener{

    private var button2:Button? = null

    override fun onClick(v: View?) {
        setScale(findViewById(R.id.frame),2f)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shadow)
    }

    override fun findViews() {
        val button = findViewById(R.id.button1)
        button.setOnClickListener(this)
        button2 = findViewById(R.id.button2) as Button?
        handler!!.sendEmptyMessage(0)
    }

    override fun init() {

    }

    var handler:Handler? = object : Handler(){
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            setScale(button2!!,2f)
        }
    }

    fun setScale(view: View, scale: Float) {
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                setScale(view.getChildAt(i), scale)
            }
            Log.d("setScale","这不是一个viewGroup")
        } else {
            val params = view.layoutParams as ViewGroup.MarginLayoutParams
            Log.d("setScale",params.width.toString()+" "+params.height.toString())
            params.width = (view.width * scale).toInt()
            params.height = (view.height * scale).toInt()
            params.leftMargin = (params.leftMargin * scale).toInt()
            params.rightMargin = (params.rightMargin * scale).toInt()
            params.topMargin = (params.topMargin * scale).toInt()
            params.bottomMargin = (params.bottomMargin * scale).toInt()
            view.layoutParams = params

            if (view is TextView){
//                view.textSize = px2sp(sp2px(view.textSize) * scale).toFloat()
                view.setTextSize(TypedValue.COMPLEX_UNIT_PX,view.textSize * scale)
            }

            Log.d("setScale","完成大小转换")
        }
    }

    /**
     * 吧像素转换为sp？（是这个意思吧
     */
    private fun sp2px(spValue: Float): Int {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (spValue * fontScale + 0.5f).toInt()
    }

    fun px2sp(pxValue: Float): Int {
        return (pxValue / context.resources.displayMetrics.scaledDensity + 0.5f).toInt()
    }
}
