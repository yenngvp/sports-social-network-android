package vn.datsan.datsan.serverdata.storage;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import vn.datsan.datsan.utils.AppLog;

/**
 * Created by xuanpham on 7/20/16.
 */
public class CloudDataStorage {
    private final String bucketUrl = "gs://social-sport-b1cff.appspot.com";
    private static CloudDataStorage instance;
    private StorageReference storageReference = FirebaseStorage.getInstance()
            .getReferenceFromUrl(bucketUrl);//FirebaseStorage.getInstance().getReference();

    public static CloudDataStorage getInstance() {
        if (instance == null)
            instance = new CloudDataStorage();
        return instance;
    }

    public String genUniqFileName() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String prefix = dateFormat.format(new Date());
        String surfix = ((new Date()).getTime() % 1000000) + "";
        String fileName = prefix + "_" + surfix;
        return fileName;
    }

    public void uploadPhoto(Bitmap bitmap, String fileName) {
        StorageReference imageRef = storageReference.child("images/" + fileName);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imageRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                exception.printStackTrace();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                AppLog.log(AppLog.LogType.LOG_ERROR, "Photo", "Success");
            }
        });
    }
}
