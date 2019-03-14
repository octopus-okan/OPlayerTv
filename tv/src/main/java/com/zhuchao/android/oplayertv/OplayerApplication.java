package com.zhuchao.android.oplayertv;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.zhuchao.android.playsession.OPlayerSession;
import com.zhuchao.android.playsession.OPlayerSessionManager;
import com.zhuchao.android.shapeloading.ShapeLoadingDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OplayerApplication extends Application {

    private static OPlayerSessionManager mOPlayerSessionManager = null;

    private static Context appContext;//需要使用的上下文对象

    public OplayerApplication() {
        super();

    }

    public static Context getAppContext() {
        return appContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this.getApplicationContext();
        getOpsM();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        mOPlayerSessionManager = null;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

    @Override
    public void registerActivityLifecycleCallbacks(ActivityLifecycleCallbacks callback) {
        super.registerActivityLifecycleCallbacks(callback);
    }

    private OPlayerSessionManager createOplayerApplication() {
        final OPlayerSessionManager OPSM = new OPlayerSessionManager(OplayerApplication.getAppContext(),null, null);
        return OPSM;

    }

    /*
    public static synchronized OPlayerSessionManager getmOPlayerSessionManager() {
        if (mOPlayerSessionManager == null)
            mOPlayerSessionManager = new OPlayerSessionManager(OplayerApplication.getAppContext(),null, null);
        return mOPlayerSessionManager;
    }*/

    public static OPlayerSessionManager getOpsM() {//通过上下文得到实例，用实例去调用非静态方法
        Log.d("OplayerApplication---------->","OPlayerSessionManager");
        OplayerApplication myApplication = (OplayerApplication) appContext;//.getApplicationContext();
        return OplayerApplication.mOPlayerSessionManager == null ? (OplayerApplication.mOPlayerSessionManager = myApplication.createOplayerApplication()) : OplayerApplication.mOPlayerSessionManager;
    }

    public static void ClearOpsM()
    {
        mOPlayerSessionManager = null;
    }

}
