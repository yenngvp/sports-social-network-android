package vn.datsan.datsan.utils;

import android.util.Log;

/**
 * Created by Xuan PhD on 1/29/16.
 *
 */
public class AppLog {

    public enum LogType {
        LOG_INFO,
        LOG_WARN,
        LOG_DEBUG,
        LOG_ERROR,
    }

    public static void log(LogType type, String tag, String message) {

        switch (type) {
            case LOG_INFO:
                Log.i(tag, message);
                break;
            case LOG_WARN:
                Log.w(tag, message);
                break;
            case LOG_DEBUG:
                Log.d(tag, message);
                break;
            case LOG_ERROR:
                Log.e(tag, message);
                break;
            default:
                Log.i(tag, message);
        }
    }

    // Log VERBOSE
    public static void v(String message) {
        Log.v("", message);
    }

    public static void v(String tag, String message) {
        Log.v(tag, message);
    }

    // Log INFO
    public static void i(String message) {
        Log.i("", message);
    }

    public static void i(String tag, String message) {
        Log.i(tag, message);
    }

    // Log WARN
    public static void w(String message) {
        Log.w("", message);
    }

    public static void w(String tag, String message) {
        Log.w(tag, message);
    }

    // Log DEBUG
    public static void d(String message) {
        Log.d("", message);
    }

    public static void d(String tag, String message) {
        Log.d(tag, message);
    }

    // Log ERROR
    public static void e(String message) {
        Log.e("", message);
    }

    public static void e(String tag, String message) {
        Log.e(tag, message);
    }

    // Log Throwable
    public static void t(Throwable e) {
        Log.e("STACKTRACE", Log.getStackTraceString(e));
    }

    public static void e(String tag, Throwable e) {
        Log.e(tag, Log.getStackTraceString(e));
    }

    public static void log(Throwable e) {
        Log.e("STACKTRACE", Log.getStackTraceString(e));
    }
}
