package vn.datsan.datsan.serverdata;

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
    private final String _Database_Url = "https://social-sport-b1cff.firebaseio.com/app/users";
    private DatabaseReference userLocation;

    private  UserManager() {
        userLocation = FirebaseDatabase.getInstance().getReference("app/users");
    }

    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    private String genIden(User user) {
        return user.getPhone();
    }

    public void addUser(User user) {
        //Map<String, User> map = new HashMap<>();
        //map.put(genIden(user), user);

        userLocation.child(genIden(user)).setValue(user);
    }

//    public void updateUser(User user) {
//        Map<String, User> map = new HashMap<>();
//        map.put(genIden(user), user);
//        userNode.updateChildren(map);
//    }

    public void getUser(String id, final CallBack.OnResultReceivedListener callBack) {
        userLocation.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                AppLog.log(AppLog.LogType.LOG_DEBUG, TAG, "Hello " + user.getName());

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
}
