package vn.datsan.datsan.serverdata;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import vn.datsan.datsan.models.User;
import vn.datsan.datsan.search.ElasticsearchService;
import vn.datsan.datsan.search.interfaces.Searchable;
import vn.datsan.datsan.utils.AppLog;
import vn.datsan.datsan.utils.listeners.FirebaseChildEventListener;

/**
 * Created by xuanpham on 6/20/16.
 */
public class UserManager {
    private static final String TAG = UserManager.class.getName();
    private static UserManager instance;
    private DatabaseReference userDatabaseRef;
    private User userInfo;

    private FirebaseChildEventListener firebaseChildEventListener;

    private  UserManager() {
        userDatabaseRef = FirebaseDatabase.getInstance().getReference("app/users");

        // Enable 'Searchable' put mapping for the managed underline User
        ElasticsearchService.getInstance().putMapping(User.getPutMapping(), User.class);
        // Listening on user object change
        firebaseChildEventListener = new FirebaseChildEventListener(User.class);
        userDatabaseRef.addChildEventListener(firebaseChildEventListener);
    }

    @Override
    public void finalize() throws Throwable {

        AppLog.i(TAG, "Going finalize(). Detaching firebaseChildEventListener");
        // Detach the FirebaseChildEventListener when the database reference detached
        userDatabaseRef.removeEventListener(firebaseChildEventListener);

        super.finalize();
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
