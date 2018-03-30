package cn.liudp.wifisignalstrength.utils;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.liudp.wifisignalstrength.MyApplication;

import static android.content.Context.ACTIVITY_SERVICE;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.widget.Toast;

import com.blankj.utilcode.util.Utils;


/**
 * @author dongpoliu on 2018-03-28.
 */

public class CommonUtils {

    /**
     * 打开应用具体设置
     */
    public static void openAppSettings() {
        Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.parse("package:" + Utils.getApp().getPackageName()));
        Utils.getApp().startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

   /**
     * Log输出标识
     **/
    private static final String TAG = "CommonUtils";
    /**
     * 日期格式：yyyy-MM-dd HH:mm:ss
     **/
    public static final String DF_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    /**
     * 日期格式：yyyy-MM-dd HH:mm
     */
    public static final String DF_YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    public static final String DF_CN_YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";

    /**
     * 日期格式：yyyy-MM-dd
     */
    public static final String DF_YYYY_MM_DD = "yyyy-MM-dd";

    /**
     * 日期格式：yyyy/MM/dd
     */
    public static final String DF_YYYY_MM_DD_SEC = "yyyy/MM/dd";

    /**
     * 日期格式：HH:mm:ss
     **/
    public static final String DF_HH_MM_SS = "HH:mm:ss";

    /**
     * 日期格式：HH:mm
     */
    public static final String DF_HH_MM = "HH:mm";

    private final static long minute = 60 * 1000;// 1分钟
    private final static long hour = 60 * minute;// 1小时
    private final static long day = 24 * hour;// 1天
    private final static long month = 31 * day;// 月
    private final static long year = 12 * month;// 年
    public final static long FOUR_HOUR = 14400000;

    /**
     * @param ctx 上下文
     * @param px  像素
     * @return 返回值单位是dp
     */
    public static int px2dp(Context ctx, float px) {
        float scale = ctx.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    /**
     * @param ctx 上下文
     * @param dp  dp值
     * @return 返回像素值
     */
    public static int dp2px(Context ctx, float dp) {
        float scale = ctx.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    /**
     * @param ctx 上下文
     * @param px  像素
     * @return 返回值单位是dp
     */
    public static int px2sp(Context ctx, float px) {
        final float fontScale = ctx.getResources().getDisplayMetrics().scaledDensity;
        return (int) (px / fontScale + 0.5f);
    }

    /**
     * @param ctx 上下文
     * @param sp  字体大小
     * @return 返回字体像素值
     */
    public static int sp2px(Context ctx, float sp) {
        final float fontScale = ctx.getResources().getDisplayMetrics().scaledDensity;
        return (int) (fontScale * sp + 0.5);
    }

    /**
     * 获取屏幕的宽度
     *
     * @param ctx 上下文
     * @return 屏幕宽度
     */
    public static int getDisplayWidth(Context ctx) {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) ctx).getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    /**
     * 获取屏幕的高度
     *
     * @param ctx 上下文
     * @return 屏幕高度
     */
    public static int getDisplayHeight(Context ctx) {
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) ctx).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.heightPixels;
    }

    /**
     * 判断手机号是否合法
     *
     * @param mobiles 手机号
     * @return
     */
    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern.compile("^((14[5-7])|(17[0-9])|(13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * 对字符串复制
     *
     * @param act
     * @param str
     */
    public static void copyString(Activity act, String str) {
        ClipboardManager clipboardManager = (ClipboardManager) act.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData data = ClipData.newPlainText("tmp", str);
        clipboardManager.setPrimaryClip(data);
    }

    /**
     * 将日期格式化成友好的字符串：几分钟前、几小时前、几天前、几月前、几年前、刚刚
     *
     * @param date
     * @return
     */
    public static String formatTimeFriendly(Date date) {
        if (date == null) {
            return null;
        }
        long diff = new Date().getTime() - date.getTime();
        long r = 0;
        if (diff > year) {
            r = (diff / year);
            return r + "年前";
        }
        if (diff > month) {
            r = (diff / month);
            return r + "个月前";
        }
        if (diff > day) {
            r = (diff / day);
            return r + "天前";
        }
        if (diff > hour) {
            r = (diff / hour);
            return r + "个小时前";
        }
        if (diff > minute) {
            r = (diff / minute);
            return r + "分钟前";
        }
        return "刚刚";
    }

    /**
     * 将日期字符串转成日期
     *
     * @param strDate 字符串日期
     * @param format  格式化的样式
     * @return java.util.date 日期类型
     */
    public static Date parseDate(String strDate, String format) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        Date returnDate = null;
        try {
            returnDate = dateFormat.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return returnDate;
    }

    /**
     * 将日期以yyyy-MM-dd HH:mm:ss格式化
     *
     * @param date 日期
     * @return
     */
    public static String formatDateTime(long date) {
        return formatDateTime(date, DF_YYYY_MM_DD_HH_MM_SS);
    }

    /**
     * 将日期以给定的格式格式化
     *
     * @param time 日期
     * @return
     */
    public static String formatDateTime(long time, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(time));
    }

    /**
     * 检查是否已挂载SD卡镜像（是否存在SD卡）
     *
     * @return
     */
    public static boolean isMountedSDCard() {
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            return true;
        } else {
            Log.w(TAG, "SDCARD is not MOUNTED !");
            return false;
        }
    }

    /**
     * 获取应用缓存目录的绝对路径，优先使用外部SD卡的缓存路径
     *
     * @param context
     * @return 缓存目录的绝对路径
     */
    public static String getDiskCacheDir(Context context) {
        File cacheDir = context.getCacheDir();
        if (isMountedSDCard()) {
            File nCacheDir = context.getExternalCacheDir();
            // 部分手机有卡不可写
            if (nCacheDir != null) {
                if (nCacheDir.canWrite()) {
                    cacheDir = nCacheDir;
                }
            }
        }
        return cacheDir.getAbsolutePath();
    }

    /**
     * 获取SD卡剩余容量（单位Byte）
     *
     * @return
     */
    public static long gainSDFreeSize() {
        if (isMountedSDCard()) {
            // 取得SD卡文件路径
            File path = Environment.getExternalStorageDirectory();
            StatFs sf = new StatFs(path.getPath());
            // 获取单个数据块的大小(Byte)
            long blockSize = sf.getBlockSize();
            // 空闲的数据块的数量
            long freeBlocks = sf.getAvailableBlocks();

            // 返回SD卡空闲大小
            return freeBlocks * blockSize; // 单位Byte
        } else {
            return 0;
        }
    }

    /**
     * 获取SD卡总容量（单位Byte）
     *
     * @return
     */
    public static long gainSDAllSize() {
        if (isMountedSDCard()) {
            // 取得SD卡文件路径
            File path = Environment.getExternalStorageDirectory();
            StatFs sf = new StatFs(path.getPath());
            // 获取单个数据块的大小(Byte)
            long blockSize = sf.getBlockSize();
            // 获取所有数据块数
            long allBlocks = sf.getBlockCount();
            // 返回SD卡大小（Byte）
            return allBlocks * blockSize;
        } else {
            return 0;
        }
    }

    /**
     * 获取可用的SD卡路径（若SD卡不没有挂载则返回""）
     *
     * @return
     */
    public static String getSDCardRootPath() {
        if (isMountedSDCard()) {
            File sdcardDir = Environment.getExternalStorageDirectory();
            if (!sdcardDir.canWrite()) {
                Log.w(TAG, "SDCARD can not write !");
            }
            return sdcardDir.getPath();
        }
        return "";
    }

    /**
     * 获取可用的SD卡文件实例
     * @return SD卡文件实例
     */
    public static File getSDCardRootFile(){
        if (isMountedSDCard()){
            return Environment.getExternalStorageDirectory();
        }
        return null;
    }

    /**
     * 返回指定的文件路径
     * @param type 文件类型
     * @return
     */
    public static String getSDCardDirectory(String type){

        if (isMountedSDCard()){
            return Environment.getExternalStoragePublicDirectory(type).getAbsolutePath();
        }
        return null;
    }

    /**
     * 创建文件夹
     * @param filePath 文件夹路径
     * @return
     */
    public static boolean createSDCardDirectory(String filePath)
    {
        if (isMountedSDCard()){
            String sdCardRootPath = getSDCardRootPath();
            File file = new File(sdCardRootPath, filePath);
            if (!file.exists())
            {
                return file.mkdirs();
            }
        }
        return false;
    }

    /**
     * 创建文件
     * @param path 文件路径
     * @return
     */
    public static boolean createSDCardFile(String path){
        if (isMountedSDCard()){
            File file = new File(path);
            if (file.exists()){
                return true;
            } else {
                File parentFile = file.getParentFile();
                boolean mkdirs = parentFile.mkdirs();
                if (mkdirs){
                    try {
                        return file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    return false;
                }
            }
        }

        return false;
    }

}
