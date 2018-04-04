package cn.liudp.wifisignalstrength;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;


import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.CrashUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.Utils;
import com.facebook.stetho.Stetho;
import com.hugo.watcher.Watcher;
import com.blankj.utilcode.util.NetworkUtils;
import cn.liudp.wifisignalstrength.R;
import com.squareup.leakcanary.LeakCanary;
import cn.liudp.wifisignalstrength.base.BaseApplication;
import im.fir.sdk.FIR;

/**
 * @author dongpoliu on 2018-03-15.
 */

public class MyApplication extends BaseApplication {

    private static MyApplication mApplication;
    private static String mAppCacheDir;
    private int mActivityCount;
    public static String mAppName = "MyApplication";
    public final static boolean enableGuide = true;

    public static MyApplication getApplication() {
        return mApplication;
    }

    public static void setApplication(MyApplication application) {
        MyApplication.mApplication = application;
    }

    public static int getGridSpanCount(Activity activity) {

        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        float screenWidth  = displayMetrics.widthPixels;
        float cellWidth = activity.getResources().getDimension(R.dimen.item_size);
        return Math.round(screenWidth / cellWidth);
    }

    public static int getStickersGridSpanCount(Activity activity) {

        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        float screenWidth  = displayMetrics.widthPixels;
        float cellWidth = activity.getResources().getDimension(R.dimen.sticker_item_size);
        return Math.round(screenWidth / cellWidth);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mAppName = this.getPackageManager().getNameForUid(Binder.getCallingUid());
        mApplication = this;
        Utils.init(this);
//        initLog();
//        initCrash();
        if (!BuildConfig.DEBUG) {
            // App crash analysis and auto-detect upgrading
            FIR.init(this);
        } else {
            // watch the fps and used memory of the app.
            Watcher.getInstance().start(this);
            // debug bridge for Android applications, enabling the powerful Chrome Developer Tools and much more.
            Stetho.initializeWithDefaults(this);
            initLeakCanary();
        }
    }

    public int getActivityCount() {
        return mActivityCount;
    }

    public boolean isConnected() {
        return NetworkUtils.isConnected();
    }

    public void setActivityCount(int activityCount) {
        mActivityCount = activityCount;
    }

    public static String getAppCacheDir() {
        return mAppCacheDir;
    }


    private void initLeakCanary() {
        // 内存泄露检查工具
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
    }

    public void initLog() {
        final LogUtils.Config config = LogUtils.getConfig()
            .setLogSwitch(BuildConfig.DEBUG)// 设置 log 总开关，包括输出到控制台和文件，默认开
            .setConsoleSwitch(BuildConfig.DEBUG)// 设置是否输出到控制台开关，默认开
            .setGlobalTag(null)// 设置 log 全局标签，默认为空
            // 当全局标签不为空时，我们输出的 log 全部为该 tag，
            // 为空时，如果传入的 tag 为空那就显示类名，否则显示 tag
            .setLogHeadSwitch(true)// 设置 log 头信息开关，默认为开
            .setLog2FileSwitch(false)// 打印 log 时是否存到文件的开关，默认关
            .setDir("")// 当自定义路径为空时，写入应用的/cache/log/目录中
            .setFilePrefix("")// 当文件前缀为空时，默认为"util"，即写入文件为"util-MM-dd.txt"
            .setBorderSwitch(true)// 输出日志是否带边框开关，默认开
            .setConsoleFilter(LogUtils.V)// log 的控制台过滤器，和 logcat 过滤器同理，默认 Verbose
            .setFileFilter(LogUtils.V)// log 文件过滤器，和 logcat 过滤器同理，默认 Verbose
            .setStackDeep(1);// log 栈深度，默认为 1
        new Thread(new Runnable() {
            @Override
            public void run() {
                LogUtils.d(config.toString());
            }
        }).start();
    }

    @SuppressLint("MissingPermission")
    private void initCrash() {
        CrashUtils.init(new CrashUtils.OnCrashListener() {
            @Override
            public void onCrash(Throwable e) {
                e.printStackTrace();
                restartApp();
            }
        });
    }

    private void restartApp() {
        Intent intent = new Intent();
        intent.setClassName("cn.liudp.wifisignalstrength", "cn.liudp.wifisignalstrength.MainActivity");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent restartIntent = PendingIntent.getActivity(this, 0, intent, 0);
        AlarmManager manager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        if (manager == null) return;
        manager.set(AlarmManager.RTC, System.currentTimeMillis() + 1, restartIntent);
        ActivityUtils.finishAllActivities();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }
}