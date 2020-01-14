package com.xing.work.xframework.PlaneWar;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.xing.work.xframework.ActivityBase;
import com.xing.work.xframework.R;

import java.util.Random;

public class ActivityPlaneWar extends ActivityBase implements View.OnTouchListener {

    private Plane plane;
    private WarBackground warBackground;

    private WarBack warback;

    private BulletBelt list_Bullet;//己方子弹集合

    private Enemies enemies;//敌机的集合

    private int bullet_startX,bullet_startY;

    private TextView textView;

    private boolean isGoing = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planewar);
        setBottomBarVisible(false);
    }

    @Override
    public void findViews() {
        plane = findViewById(R.id.plane);
        warBackground = findViewById(R.id.back);
        warback = findViewById(R.id.warback);
        textView = findViewById(R.id.text);

        list_Bullet = new BulletBelt();
        enemies = new Enemies();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void init() {

        list_Bullet.setRemoveChildView(warback);
        enemies.setRemoveChildView(warback);

        plane.setOnTouchListener(this);

        plane.post(new Runnable() {
            @Override
            public void run() {
                //第一次设置开火位置
                //第一次调用开火
                fire.sendEmptyMessage(0);
                setFireLoaction(plane);
                handler.sendEmptyMessage(0);
//                main.start();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 用古老的方法，终止线程
     */
    @Override
    protected void onStop() {
        super.onStop();
        stop();
    }

    private void stop(){
        isGoing = false;
        warBackground.stop();
    }

    private float startX,startY;
    @Override
    public boolean onTouch(final View v, MotionEvent event) {
        if (v.getId()!=R.id.plane){
            return false;
        }

        //让飞机跟随手指移动
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                startX = event.getRawX();
                startY = event.getRawY();
//                Log.d("changdu",event.getX()+" "+event.getRawX());
                break;

            case MotionEvent.ACTION_MOVE:
                float tempx = event.getRawX() - startX;
                float tempy = event.getRawY() - startY;
//                Log.d("changdu",tempx+" "+tempy);

                //以下代码用于让飞机跟随手指移动，并且将位置报告给activity
                Bundle b = new Bundle();
                b.putInt("left",(int)(tempx));
                b.putInt("top",(int)(tempy));
//                Log.d("wocao",plane.getLeft()+" "+plane.getTop());
//                Log.d("wocao",event.getRawX()+" "+event.getRawY());
                Message msg = new Message();
                msg.setData(b);
                movePlane.sendMessage(msg);

                startX = event.getRawX();
                startY = event.getRawY();
                break;

            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }

    /**
     * 根据飞机的位置，给出子弹的初始位置
     * 将子弹的初始位置设置为：飞机的顶部居中
     * @param view
     */
    private void setFireLoaction(View view){
        bullet_startX = view.getRight() - view.getWidth()/2;
        bullet_startY = view.getTop();
    }

    /**
     * 保证子弹每N秒发射一枚
     */
    private void onFire(){
        Bullet bullet = new Bullet(getContext());
        bullet.setVisibility(View.INVISIBLE);
        bullet.setOnBulletShotOut(list_Bullet);
        list_Bullet.add(bullet);
        warback.addView(bullet);
        bullet.setLocation(bullet_startX,bullet_startY);
        bullet.setVisibility(View.VISIBLE);
//        Log.d("wocao",warback.getChildCount()+" ");
    }

    @SuppressLint("HandlerLeak")
    private Handler fire = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (isGoing){
                onFire();
//            这里可以看到，将飞出屏幕外的子弹，移除子弹带，可以节约一大波内存
//            Log.d("wocao",list_Bullet.size()+" ");
                fire.sendEmptyMessageDelayed(0,100);
            }
        }
    };

    @SuppressLint("HandlerLeak")
    private Handler movePlane = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle b = msg.getData();
            setViewLocation(plane,b);
            setFireLoaction(plane);
        }
    };

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (isGoing){
                addEnemy();
                handler.sendEmptyMessageDelayed(0,new Random().nextInt(5000));
            }
        }
    };

    /**
     * 增加敌人
     */
    private void addEnemy(){
//        Log.d("wocao"," "+warback.getWidth()); 这里是测试，看看是否能够拿到容器的宽高数据
        Enemy enemy = new Enemy(getContext());
        enemy.setVisibility(View.INVISIBLE);
        warback.addView(enemy);
        enemies.add(enemy);
        enemy.setOnEnemyFly(enemies);
        enemy.setLocation(new Random().nextInt(warback.getWidth()) ,
                (int) (0-sp2px(30f)));
        enemy.setVisibility(View.VISIBLE);
    }

    /**
     * 移动某个View的方法(其实主要是自己的小飞机)
     * @param view
     * @param bundle
     */
    private void setViewLocation(View view,Bundle bundle){
        FrameLayout.LayoutParams fl = (FrameLayout.LayoutParams) view.getLayoutParams();
        if (fl.leftMargin == Gravity.NO_GRAVITY
                ||fl.topMargin == Gravity.NO_GRAVITY){
            fl.leftMargin = view.getLeft();
            fl.topMargin = view.getTop();
        }
        int left = fl.leftMargin + bundle.getInt("left");
        int top = fl.topMargin + bundle.getInt("top");

        fl.leftMargin = left;
        fl.topMargin = top;
        fl.bottomMargin = Gravity.NO_GRAVITY;
        fl.gravity = Gravity.NO_GRAVITY;
        //查询源代码之后发现，setlayoutParams之后，会自动调用requestLayout
        //而requestLayout会自动让父view进行布局刷新，执行mParent.requestlayout()
        view.setLayoutParams(fl);
//        view.requestLayout();
    }

    private Thread main = new Thread(){
        @Override
        public void run() {
            super.run();
            while (isGoing){
                Log.d("wocao","线程到啦");
//                Log.d("wodiu",((View) bullet).getX()+" "+((View)bullet).getWidth());
                for (Object bullet:list_Bullet){
                    for (Object enemy:enemies){
                        Log.d("wocao","线程到啦");
                        Log.d("wodiu",((View) bullet).getX()+" "+((View)bullet).getWidth());
                        if (GameEngine.isCollsionWithRect((View) bullet,(View)enemy)){
                            ActivityPlaneWar.this.stop();
                            break;
                        }
                    }
                }
                try {
                    sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    /**
     * 将sp值转换为px值
     */
    private float sp2px(float spValue) {
        final float fontScale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return spValue * fontScale + 0.5f;
    }

}
