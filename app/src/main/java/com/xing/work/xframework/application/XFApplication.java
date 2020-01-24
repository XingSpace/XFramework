package com.xing.work.xframework.application;

import android.content.Context;

import androidx.multidex.MultiDexApplication;

import com.xing.app.myutils.Utils.LogUtil;

public class XFApplication extends MultiDexApplication {

    private static final String className = "XFApplication";

    private static Context mContext;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        mContext = base;
        LogUtil.i(className + " attachBaseContext->The Application has Context");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.i(className + " onCreate->The Application was create");
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        LogUtil.i(className + " onTerminate->The Application was shut");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        LogUtil.i(className + " onLowMemory->The Application will shut soon");
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        LogUtil.i(className + " onTrimMemory->The Application beginning trim memory with level:"+level);
    }

    public static Context getContext(){
        return mContext;
    }
}
