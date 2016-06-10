package vn.datsan.datsan.utils;

import android.content.Context;

import java.text.SimpleDateFormat;

import vn.datsan.datsan.BuildConfig;

/**
 * Created by yennguyen on 10/06/2016.
 */
public class Utils {

    /**
     * Format the timestamp with SimpleDateFormat
     */
    public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private Context mContext = null;

    /**
     * Public constructor that takes mContext for later use
     */
    public Utils(Context context) {
        mContext = context;
    }


}
