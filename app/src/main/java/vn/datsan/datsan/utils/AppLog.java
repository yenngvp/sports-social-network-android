package vn.datsan.datsan.utils;

import android.util.Log;

/**
 * Created by Xuan PhD on 1/29/16.
 */
public class AppLog {

    public enum LogType {
        LOG_INFO,
        LOG_WARN,
        LOG_DEBUG,
        LOG_ERROR
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
}
