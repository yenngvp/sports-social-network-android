package vn.datsan.datsan.serverdata.storage;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;

import vn.datsan.datsan.serverdata.CallBack;
import vn.datsan.datsan.serverdata.DataType;
import vn.datsan.datsan.utils.AppLog;

/**
 * Created by xuanpham on 8/24/16.
 */
public class AppCloudDataService {
    private static AppCloudDataService instance;
    private CloudDataStorage cloudDataStorage;

    private AppCloudDataService() {
        cloudDataStorage = new CloudDataStorage();
    }

    public static AppCloudDataService getInstance() {
        if (instance == null) {
            instance = new AppCloudDataService();
        }
        return instance;
    }

    public void uploadImage(Bitmap bitmap, String fileName, final CallBack.OnResultReceivedListener callBack) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);

//        String arr[] = fileName.split(".");
//        String fileExtension = arr[arr.length - 1];

        byte[] data = baos.toByteArray();
        String filePath = DataType.IMAGE.getName() + File.separator + fileName;
        cloudDataStorage.uploadFile(data, filePath, null, new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                if (callBack != null) {
                    callBack.onResultReceived(taskSnapshot.getDownloadUrl().toString());
                }
            }
        });
    }

    public void getFileUrl(String filePath, DataType type, final CallBack.OnResultReceivedListener callBack) {
        String folder = type.getName();
        AppLog.log(AppLog.LogType.LOG_ERROR, "url", folder + "/" + filePath);
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
