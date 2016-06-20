package vn.datsan.datsan.utils;

import java.security.MessageDigest;

/**
 * Created by xuanpham on 6/20/16.
 */
public class Cipher {
    public static String hash(String text) {
        try {
            byte[] bytes = text.getBytes("UTF-8");
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] thedigest = md.digest(bytes);
            String result = new String(thedigest);
            XLog.log(result);
            return result;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
