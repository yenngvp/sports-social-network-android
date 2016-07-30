package vn.datsan.datsan.serverdata;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import vn.datsan.datsan.models.Group;
import vn.datsan.datsan.utils.listeners.FirebaseChildEventListener;

/**
 * Created by xuanpham on 7/29/16.
 */
public class UserGroupDataManager {
    private static final String TAG = FieldDataManager.class.getName();
    private static UserGroupDataManager instance;
    private DatabaseReference groupDatabaseRef = FirebaseDatabase.getInstance().getReference("app/usergroups");

    public UserGroupDataManager() {

        groupDatabaseRef.addChildEventListener(new FirebaseChildEventListener(Group.class));
    }

    public static UserGroupDataManager getInstance() {
        if (instance == null) {
            instance = new UserGroupDataManager();
        }
        return instance;
    }

    public void addGroup(Group group) {
//        groupDatabaseRef.child(group.getId()).setValue(group);

        String key = groupDatabaseRef.push().getKey();
        group.setId(key);
        groupDatabaseRef.child(key).setValue(group);
        int a = 1;
    }
}
