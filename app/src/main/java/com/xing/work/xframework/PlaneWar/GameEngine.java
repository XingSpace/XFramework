package com.xing.work.xframework.PlaneWar;

/**
 *
 *----------Dragon be here!----------/
 * ┏┛   ┻━━━┛    ┻┓
 * ┃ ｜｜｜｜｜｜｜ ┃
 * ┃       -      ┃
 * ┃   ┳┛    ┗┳   ┃
 * ┃              ┃
 * ┃       ┻      ┃
 * ┃              ┃
 * ┗━┓          ┏━┛
 *   丨    神   丨
 *   丨    兽   丨
 *   丨    保   丨
 *   丨    佑   丨
 *   丨　　　    ┗━━━┓
 *　　┃代码无ＢＵＧ　　┣┓
 *　　┃！！！！！！　　┃
 *　　┗┓ ┓　　　┏━┳┓┏┛
 *　 　┃┫┫　　　┃┫┫
 *　　 ┗┻┛　　　┗┻┛
 * ━━━━━━神兽出没━━━━━━by:Xing
 */

import android.view.View;

/**
 * Created by wangxing on 15/12/26.
 * 本游戏的物理引擎，所有的物理碰撞都在本类中判断
 */
public class GameEngine {

    /**
     * 静态方法供全局使用，调用本方法判断圆形与矩形是否发生碰撞
     * @param ballCenterX 圆的中心X坐标
     * @param ballCenterY 圆的中心Y坐标
     * @param ballR 圆的半径
     * @param sH 矩形的高
     * @param sW 矩形的宽
     * @param sX 矩形的X坐标
     * @param sY 矩形的Y坐标
     * @return
     */
    public static boolean isCollsion(float ballCenterX,//圆的X坐标
                                     float ballCenterY,//圆的Y坐标
                                     float ballR,//圆的半径
                                     float sH,//矩形的高
                                     float sW,//矩形的宽
                                     float sX,//矩形的X坐标
                                     float sY//矩形的Y坐标
                                     ){
        boolean bb = false;

        //在判断矩形与圆形碰撞时的特殊情况，四个角
        //圆心的坐标与矩形的各个角的距离是否小于圆形的半径，如果小于，即发生碰撞，否则不发生碰撞
        //而且，由于特殊性，当圆形进入矩形顶角的可碰撞范围时，无论是否发生碰撞，都会先触发第一种判断方式，所以顶角判断必须优先于第一种方式
        //计算方式：圆心与各个顶角的坐标差值，可以视为直角三角形的两条直角边，通过勾股定律可以得到圆心与顶角的距离，再判断这个距离是否大于半径

        //矩形左上角计算方式
        float a1 = Math.abs(ballCenterX - sX);
        float b1 = Math.abs(ballCenterY - sY);
        float c1 = (float) Math.sqrt(a1*a1 + b1*b1);

        //矩形右上角计算方式
        float a2 = Math.abs(ballCenterX - (sX+sW));
        float b2 = Math.abs(ballCenterY - sY);
        float c2 = (float) Math.sqrt(a2*a2 + b2*b2);

        //矩形左下角计算方式
        float a3 = Math.abs(ballCenterX - sX);
        float b3 = Math.abs(ballCenterY - (sY+sH));
        float c3 = (float) Math.sqrt(a3*a3 + b3*b3);

        //矩形右下角计算方式
        float a4 = Math.abs(ballCenterX - (sX+sW));
        float b4 = Math.abs(ballCenterY - (sY+sH));
        float c4 = (float) Math.sqrt(a4*a4 + b4*b4);

        //首先判断圆心是否在矩形的常规碰撞区域内
        //判断圆心是否在矩形内部
        if( sX < ballCenterX && ballCenterX < (sX+sW)
                || sY < ballCenterY && ballCenterY < (sY+sH)){

            //再判断边界的一般碰撞
            if( Math.abs(ballCenterX - (sX + sW/2)) < (ballR+(sW/2)) &&
                    Math.abs(ballCenterY - (sY + sH/2)) < (ballR+(sH/2))){
                bb = true;
            }

        }else {
            if( c1<ballR
                    || c2 < ballR
                    || c3 < ballR
                    || c4 < ballR){
                bb = true;
            }
        }
        return bb;
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

    public static boolean isCollsionWithRect(int x1, int y1, int w1, int h1, int x2, int
            y2, int w2, int h2) {
        return (x1 <= x2 + w2 && x1 + w1 >= x2 && y1 <= y2 + h2 && y1 + h1 >= y2 );
    }

    public static boolean isCollsionWithRect(View view1,View view2){
        return isCollsionWithRect((int)view1.getX(),(int)view1.getY(),view1.getWidth(),view1.getHeight(),
                (int)view2.getX(),(int)view2.getY(),view2.getWidth(),view2.getHeight());
    }

}
