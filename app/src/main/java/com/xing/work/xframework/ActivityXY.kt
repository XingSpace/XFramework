package com.xing.work.xframework

import android.os.Bundle

class ActivityXY: ActivityBase() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_xy_view)
        setBottomBarVisible(false)
    }

    override fun findViews() {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun init() {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}