package vn.datsan.datsan.serverdata;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import vn.datsan.datsan.models.Field;
import vn.datsan.datsan.models.User;
import vn.datsan.datsan.utils.XLog;

/**
 * Created by xuanpham on 6/20/16.
 */
public class FieldDataManager {
    private static FieldDataManager instance;
    private final String _Database_Url = "https://social-sport-b1cff.firebaseio.com/app/fields";
    private Firebase fieldLocation;

    private FieldDataManager() {
        fieldLocation = new Firebase(_Database_Url);
    }

    public static FieldDataManager getInstance() {
        if (instance == null) {
            instance = new FieldDataManager();
        }
        return instance;
    }

    private String genIden(Field field) {
        return field.getPhone();
    }

    public void addField(Field field) {
        fieldLocation.child(genIden(field)).setValue(field);
    }

    public void getField(String id, final CallBack.OnResultReceivedListener callBack) {
        fieldLocation.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Field user = dataSnapshot.getValue(Field.class);
                XLog.log("Hello " + user.getName());
                if (callBack != null)
                    callBack.onResultReceived(user);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                XLog.log(firebaseError.getMessage());
                if (callBack != null)
                    callBack.onResultReceived(null);
            }
        });
    }

}
