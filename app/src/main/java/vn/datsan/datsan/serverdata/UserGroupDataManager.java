package vn.datsan.datsan.serverdata;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import vn.datsan.datsan.models.Group;

/**
 * Created by xuanpham on 7/29/16.
 */
public class UserGroupDataManager {
    private static final String TAG = FieldDataManager.class.getName();
    private static UserGroupDataManager instance;
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference("app/usergroups");
    public static UserGroupDataManager getInstance() {
        if (instance == null) {
            instance = new UserGroupDataManager();
        }
        return instance;
    }

    public void addGroup(Group group) {
//        ref.child(group.getId()).setValue(group);

        String key = ref.push().getKey();
        group.setId(key);
        ref.child(key).setValue(group);
        int a = 1;
    }
}
