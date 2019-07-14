package com.xing.work.xframework.PlaneWar;

import android.widget.FrameLayout;

import java.util.ArrayList;

/**
 * 用来做"子弹带"
 */
public class BulletBelt extends ArrayList implements Bullet.OnBulletShotOut {

    private RemoveChildView removeChildView;

    @Override
    public void onOutofScreen(Bullet bullet) {
        remove(bullet);
        if (removeChildView!=null){
            removeChildView.removeChild(bullet);
        }
        bullet.stop();
        bullet = null;
    }

    @Override
    public void onShotBegin(Bullet bullet) {

    }

    @Override
    public void onShotting(Bullet bullet, FrameLayout.LayoutParams fl) {

    }

    public void setRemoveChildView(RemoveChildView removeChildView) {
        this.removeChildView = removeChildView;
    }
}
