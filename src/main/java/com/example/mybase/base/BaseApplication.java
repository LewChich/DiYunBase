package com.example.mybase.base;

import android.app.Application;
import android.content.Context;

import com.bravin.btoast.BToast;
import com.example.mybase.utils.ACache;
import com.example.mybase.utils.CrashHandler;

/**
 * @author WZ
 * @date 2019/5/14.
 * 严格要求自己，对每一行代码负责
 * description：
 */
public class BaseApplication extends Application {
    private static BaseApplication Instance;
    public static ACache aCacheAPP;
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        aCacheAPP = ACache.get(this);
        context = this;
        CrashHandler.getInstance().init(this);
        BToast.Config.getInstance().apply(this);
    }

    public static BaseApplication getInstance() {
        return Instance;
    }

    public static Context getContext() {
        return context;
    }
}
