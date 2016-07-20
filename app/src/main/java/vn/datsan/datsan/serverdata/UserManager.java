package vn.datsan.datsan.serverdata;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import vn.datsan.datsan.models.User;
import vn.datsan.datsan.utils.AppLog;

/**
 * Created by xuanpham on 6/20/16.
 */
public class UserManager {
    private static final String TAG = UserManager.class.getName();
    private static UserManager instance;
    private DatabaseReference userDatabaseRef;
    private User userInfo;

    private  UserManager() {
        userDatabaseRef = FirebaseDatabase.getInstance().getReference("app/users");
    }

    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    public void addUser(User user) {
        userDatabaseRef.child(user.getId()).setValue(user);

//        String key = userLocation.push().getKey();
//        user.setId(key);
//        userLocation.child(key).setValue(user);
    }

    public void updateUser(User user) {
        userDatabaseRef.child(user.getId()).setValue(user);
    }

    public void getCurrentUserInfo(CallBack.OnResultReceivedListener callBack) {
        getUserInfo(FirebaseAuth.getInstance().getCurrentUser().getUid(), callBack);
    }

    public void getUserInfo(String id, final CallBack.OnResultReceivedListener callBack) {
        AppLog.log(AppLog.LogType.LOG_ERROR, TAG, "Get for " + id);
        userDatabaseRef.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user != null)
                    AppLog.log(AppLog.LogType.LOG_ERROR, TAG, "Hello " + user.getName());
                userInfo = user;

                if (callBack != null)
                    callBack.onResultReceived(user);
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
                AppLog.log(AppLog.LogType.LOG_DEBUG, TAG, firebaseError.getMessage());
                if (callBack != null)
                    callBack.onResultReceived(null);
            }
        });
    }

    public User getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(User userInfo) {
        this.userInfo = userInfo;
    }
}
