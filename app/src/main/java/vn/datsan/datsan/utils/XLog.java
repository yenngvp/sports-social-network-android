package vn.datsan.datsan.utils;

import android.util.Log;

/**
 * Created by Xuan PhD on 1/29/16.
 */
public class XLog {
    public static void log(String s) {
        Log.e("Log", s);
    }
    public static void log(int i) {
        Log.e("Log", i + "");
    }
}
