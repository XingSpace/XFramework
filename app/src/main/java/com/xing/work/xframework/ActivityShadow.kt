package com.xing.work.xframework

import android.os.Bundle

/**
 * Created by wangxing on 2017/2/12.
 */

class ActivityShadow : ActivityBase() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shadow)
    }

    override fun findViews() {

    }

    override fun init() {

    }
}
