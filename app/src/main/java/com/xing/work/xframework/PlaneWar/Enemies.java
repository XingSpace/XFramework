package com.xing.work.xframework.PlaneWar;

import android.widget.FrameLayout;

import java.util.ArrayList;

public class Enemies extends ArrayList implements Enemy.OnEnemyFly {

    private RemoveChildView removeChildView;

    @Override
    public void onFlying(Enemy enemy, FrameLayout.LayoutParams fl) {

    }

    @Override
    public void onOutOfScreen(Enemy enemy) {
        remove(enemy);
        if (removeChildView!=null){
            removeChildView.removeChild(enemy);
        }
        enemy.stop();
        enemy = null;
    }

    public void setRemoveChildView(RemoveChildView removeChildView){
        this.removeChildView = removeChildView;
    }
}
