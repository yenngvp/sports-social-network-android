package vn.datsan.datsan.utils;

import java.security.MessageDigest;

/**
 * Created by xuanpham on 6/20/16.
 */
public class Cipher {
    private static final String TAG = Cipher.class.getName();

    public static String hash(String text) {
        try {
            byte[] bytes = text.getBytes("UTF-8");
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] thedigest = md.digest(bytes);
            String result = new String(thedigest);
            AppLog.log(AppLog.LogType.LOG_DEBUG, TAG, result);
            return result;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
