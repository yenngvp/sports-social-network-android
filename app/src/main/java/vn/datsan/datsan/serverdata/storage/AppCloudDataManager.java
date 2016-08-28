package vn.datsan.datsan.serverdata.storage;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import java.io.ByteArrayOutputStream;
import java.io.File;

import vn.datsan.datsan.serverdata.CallBack;
import vn.datsan.datsan.serverdata.DataType;
import vn.datsan.datsan.utils.AppLog;

/**
 * Created by xuanpham on 8/24/16.
 */
public class AppCloudDataManager {
    private static AppCloudDataManager instance;
    private CloudDataStorage cloudDataStorage;

    private AppCloudDataManager() {
        cloudDataStorage = new CloudDataStorage();
    }

    public static AppCloudDataManager getInstance() {
        if (instance == null) {
            instance = new AppCloudDataManager();
        }
        return instance;
    }

    public void uploadImage(Bitmap bitmap, String fileName) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);

//        String arr[] = fileName.split(".");
//        String fileExtension = arr[arr.length - 1];

        byte[] data = baos.toByteArray();
        String filePath = DataType.IMAGE.toString() + File.pathSeparator + fileName;
        cloudDataStorage.uploadFile(data, filePath, null, null);
    }

    public void getFileUrl(String filePath, DataType type, final CallBack.OnResultReceivedListener callBack) {
        String folder = type.toString();
        AppLog.log(AppLog.LogType.LOG_WARN, "url", folder + "/" + filePath);
        cloudDataStorage.getDownloadUrl(folder + "/" + filePath,
                new OnSuccessListener<Uri>() {

                    @Override
                    public void onSuccess(Uri uri) {
                        AppLog.log(AppLog.LogType.LOG_ERROR, "Cloud", uri.toString());
                        if (callBack != null)
                            callBack.onResultReceived(uri);
                    }
                },
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (callBack != null)
                            callBack.onResultReceived(null);
                    }
                });
    }
}
