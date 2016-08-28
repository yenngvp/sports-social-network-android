package vn.datsan.datsan.serverdata.storage;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import vn.datsan.datsan.utils.AppLog;

/**
 * Created by xuanpham on 7/20/16.
 */
public class CloudDataStorage {
    private final String bucketUrl = "gs://social-sport-b1cff.appspot.com";
    private StorageReference storageReference = FirebaseStorage.getInstance()
            .getReferenceFromUrl(bucketUrl);//FirebaseStorage.getInstance().getReference();

    public void uploadFile(byte[] data, String saveToPath, OnFailureListener failureListener,
                           OnSuccessListener successListener) {
        StorageReference fileRef = storageReference.child(saveToPath);
        UploadTask uploadTask = fileRef.putBytes(data);
        if (failureListener != null) {
            uploadTask.addOnFailureListener(failureListener);
        }
        if (successListener != null) {
            uploadTask.addOnSuccessListener(successListener);
        }
    }

    public void uploadFile(InputStream stream, String saveToPath) {

    }

    public void getDownloadUrl(String fileRef, OnSuccessListener onSuccessListener,
                               OnFailureListener onFailureListener) {
        Task<Uri> task = storageReference.child(fileRef).getDownloadUrl();
        if (onSuccessListener != null)
            task.addOnSuccessListener(onSuccessListener);
        if (onFailureListener != null)
            task.addOnFailureListener(onFailureListener);
    }

}
