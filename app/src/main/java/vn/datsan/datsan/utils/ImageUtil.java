package vn.datsan.datsan.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by xuanpham on 6/26/16.
 */
public class ImageUtil {

    public static String imageToStringBase64(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] bytes = baos.toByteArray();
        String base64Image = Base64.encodeToString(bytes, Base64.DEFAULT);
        return base64Image;
    }

    public static Bitmap stringBase64ToImage(String stringBitmap) {
        byte[] imageAsBytes = Base64.decode(stringBitmap.getBytes(), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
        return bitmap;
    }

    public static String genUniqFileName() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String prefix = dateFormat.format(new Date());
        String surfix = ((new Date()).getTime() % 1000000) + "";
        String fileName = prefix + "_" + surfix;
        return fileName;
    }
}
