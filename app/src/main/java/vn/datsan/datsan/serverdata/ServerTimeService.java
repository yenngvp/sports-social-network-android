package vn.datsan.datsan.serverdata;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.DateTime;

import vn.datsan.datsan.models.ServerTime;
import vn.datsan.datsan.utils.AppConstants;

/**
 * Created by yennguyen on 9/18/16.
 */
public class ServerTimeService {

    public static DateTime todayAtMidnightServerTime;

    private static final String TAG = ServerTimeService.class.getSimpleName();
    private static ServerTimeService instance;

    private DatabaseReference serverTimeDatabaseRef;

    private  ServerTimeService() {
        serverTimeDatabaseRef = FirebaseDatabase.getInstance().getReference(AppConstants.FIREBASE_SERVERTIME);
    }

    public synchronized static ServerTimeService getInstance() {
        if (instance == null) {
            instance = new ServerTimeService();
        }
        return instance;
    }

    public void getServerTime(final String userId, final CallBack.OnResultReceivedListener callback) {
        serverTimeDatabaseRef.child(userId).setValue(new ServerTime()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                serverTimeDatabaseRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ServerTime serverTime = dataSnapshot.getValue(ServerTime.class);
                        callback.onResultReceived(serverTime);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        callback.onResultReceived(null);
                    }
                });
            }
        });
    }
}
