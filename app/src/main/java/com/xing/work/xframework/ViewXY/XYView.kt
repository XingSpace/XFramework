package com.xing.work.xframework.ViewXY

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.xing.work.xframework.R

/**
 * 一个直角坐标系的view
 */
class XYView:View{

    private var width:Int? = null
    private var height:Int? = null

    //十字坐标轴的起始位置
    private var crossStartY:Int? = null
    private var crossStartX:Int? = null
    private var oC:Coordinate? = null//记录原点的绘制坐标

    private var moveX:Float? = 0f
    private var moveY:Float? = 0f
    var startX:Float? = null
    var startY:Float? = null

    var startXForUp:Float? = null
    var startYForUp:Float? = null


    private val listPoint:MutableList<Coordinate> = mutableListOf()

    private val paint:Paint = Paint()

    /**
     * 每一个格子的单位距离
     */
    private val distance:Float = sp2px(24f).toFloat()

    constructor(context: Context):this(context,null)

    constructor(context: Context?,attributeSet: AttributeSet?):super(context,attributeSet)

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        when(event!!.action){
            MotionEvent.ACTION_DOWN -> {
                startX = event.x
                startY = event.y

                startXForUp = event.x
                startYForUp = event.y
//                return true//表示这个时候，touch事件交给本view来处理了
            }

            MotionEvent.ACTION_MOVE -> {
                moveX = event.x - startX!!
                moveY = event.y - startY!!
                crossStartX = crossStartX!! + moveX!!.toInt()
                crossStartY = crossStartY!! + moveY!!.toInt()

                startX = event.x
                startY = event.y
//                oC!!.x = oC!!.x + moveX!!.toInt()
//                oC!!.y = oC!!.y + moveY!!.toInt()
                invalidate()
            }

            MotionEvent.ACTION_UP -> {
                //当手指离开屏幕时，所触碰到的点，把它标记出来
                if (Math.abs(event.x - startXForUp!!) > 10 || Math.abs(event.y - startYForUp!!) > 10){
                    return true
                }

                var temp = findPointByXY(event.x,event.y)
                if (listPoint.contains(temp)){
                    Log.d("XYtest","该点已经被标记")
                    listPoint.remove(temp)
                    Log.d("XYtest","该点已经被移除标记")
                }else{
                    listPoint.add(temp)
                }
                invalidate()
//                findPointByXY(event.x,event.y)
            }
        }

        return true
    }

    /** 根据触摸的位置，得到一个坐标点  返回值是数轴值乘以单位距离*/
    private fun findPointByXY(x:Float,y:Float):Coordinate{
        var temp:Float?

        temp = x - crossStartX!!
        var a:Int = (temp/distance).toInt()
        if (Math.abs(temp)%distance > distance/2){
            if(temp<0){
                a--
            }else{
                a++
            }
        }

        temp = y - crossStartY!!
        var b:Int = (temp/distance).toInt()
        if (Math.abs(temp)%distance > distance/2){
            if(temp<0){
                b--
            }else{
                b++
            }
        }

        var coordinate = Coordinate(a*distance,b*distance)
        coordinate.xR = a
        coordinate.yR = b
        return coordinate
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        height = getMeasuredHeight()
        width = getMeasuredWidth()
        crossStartY = height!!/2 - sp2px(1.5f)
        crossStartX = width!!/2 - sp2px(1.5f)
        oC = Coordinate(crossStartX!!.toFloat(),crossStartY!!.toFloat())
        Log.d("XYtest","执行完了onMeasure")
        Log.d("XYtest",height.toString()+" "+width)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawCoordinate(canvas!!)//先绘制坐标轴中的网格
        drawCross(canvas!!)//再绘制X和Y轴

        if (listPoint.size!=0){
            //绘制被点击的点
            drawPoint(canvas!!)
        }

    }


    private fun drawPoint(canvas: Canvas){
        paint.strokeWidth = sp2px(0f).toFloat()
        for (item:Coordinate in listPoint){
            //绘制被标记的点
            paint.color = resources.getColor(R.color.colorPrimary)
            canvas.drawCircle(item.x+crossStartX!!,item.y+crossStartY!!,sp2px(3f).toFloat(),paint)
            //绘制被标记点的坐标
            paint.color = Color.DKGRAY
            paint.textSize = sp2px(16f).toFloat()
            var str = "("+item.xR+","+item.yR+")"
            var str_width = paint.measureText(str)
            canvas.drawText(str,item.x+crossStartX!!-str_width/2,item.y+crossStartY!!+sp2px(16f),paint)
        }
    }

    /**
     * 绘制主要的xy轴
     */
    private fun drawCross(canvas:Canvas){
        paint.color = resources.getColor(R.color.colorAccent)
        paint.strokeWidth = sp2px(3f).toFloat()
        canvas.drawLine(0F,crossStartY!!.toFloat(),width!!.toFloat(),crossStartY!!.toFloat(),paint)
        canvas.drawLine(crossStartX!!.toFloat(),0f,crossStartX!!.toFloat(),height!!.toFloat(),paint)
    }

    /**
     * 绘制坐标系中的网格
     */
    private fun drawCoordinate(canvas: Canvas){
        var tempX:Int? = crossStartX
        while (tempX!! < width!!){
            tempX = tempX + distance.toInt()
            drawCoordinateX(canvas,tempX.toFloat())
        }
        tempX = crossStartX
        while (tempX!! > 0){
            tempX = tempX - distance.toInt()
            drawCoordinateX(canvas,tempX.toFloat())
        }

        var tempY:Int? = crossStartY
        while (tempY!!<height!!){
            tempY = tempY + distance.toInt()
            drawCoordinateY(canvas,tempY.toFloat())
        }
        tempY = crossStartY
        while (tempY!!>0){
            tempY = tempY - distance.toInt()
            drawCoordinateY(canvas,tempY.toFloat())
        }
    }

    private fun drawCoordinateY(canvas: Canvas,tempY:Float){
        paint.color = Color.LTGRAY
        paint.strokeWidth = sp2px(0.5f).toFloat()

        canvas.drawLine(0F,tempY,width!!.toFloat(),tempY,paint)
//        canvas.drawLine(crossStartX!!.toFloat()+tempX,0f,crossStartX!!.toFloat()+tempX,height!!.toFloat(),paint)
    }

    private fun drawCoordinateX(canvas: Canvas,tempX: Float){
        paint.color = Color.LTGRAY
        paint.strokeWidth = sp2px(0.5f).toFloat()
        canvas.drawLine(tempX,0f,tempX,height!!.toFloat(),paint)
    }

    private fun sp2px(spValue: Float): Int {
        return (spValue * context.resources.displayMetrics.scaledDensity + 0.5f).toInt()
    }

}