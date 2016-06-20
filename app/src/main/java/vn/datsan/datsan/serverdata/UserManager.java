package vn.datsan.datsan.serverdata;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import vn.datsan.datsan.models.User;
import vn.datsan.datsan.utils.XLog;

/**
 * Created by xuanpham on 6/20/16.
 */
public class UserManager {
    private static UserManager instance;
    private final String databaseUrl = "https://social-sport-b1cff.firebaseio.com/app/users";
    private Firebase userNode; // location

    private  UserManager() {
        userNode = new Firebase(databaseUrl);
    }

    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    public void addUser(User user) {
        Map<String, User> map = new HashMap<>();
        map.put(user.getPhone(), user);

        userNode.setValue(map);
    }

    public void getUser(String id) {
        userNode.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                XLog.log(user.getEmail());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                XLog.log(firebaseError.getMessage());
            }
        });
    }
}
