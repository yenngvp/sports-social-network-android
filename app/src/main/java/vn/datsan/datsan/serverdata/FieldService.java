package vn.datsan.datsan.serverdata;

import android.content.Context;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import vn.datsan.datsan.R;
import vn.datsan.datsan.models.FakeStadium;
import vn.datsan.datsan.models.Field;
import vn.datsan.datsan.utils.AppLog;
import vn.datsan.datsan.utils.AppConstants;
import vn.datsan.datsan.utils.RawIO;

/**
 * Created by xuanpham on 6/20/16.
 */
public class FieldService {
    private static final String TAG = FieldService.class.getName();
    private static FieldService instance;
    private DatabaseReference fieldDatabaseRef;
    private List<Field> fields;

    private FieldService() {
        fieldDatabaseRef = FirebaseDatabase.getInstance().getReference(AppConstants.FIREBASE_FIELDS);
    }

    public static FieldService getInstance() {
        if (instance == null) {
            instance = new FieldService();
        }
        return instance;
    }

    public void addField(Field field) {
        String key = fieldDatabaseRef.push().getKey();
        field.setId(key);
        fieldDatabaseRef.child(key).setValue(field);
    }

    public void getField(String id, final CallBack.OnResultReceivedListener callBack) {
        fieldDatabaseRef.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Field field = dataSnapshot.getValue(Field.class);
                if (callBack != null)
                    callBack.onResultReceived(field);
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
                AppLog.log(AppLog.LogType.LOG_DEBUG, TAG, firebaseError.getMessage());
                if (callBack != null)
                    callBack.onResultReceived(null);
            }
        });
    }

    public List<Field> getFields() {
        return fields;
    }

    public void getFields(final CallBack.OnResultReceivedListener callBack) {

        fieldDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                fields = new ArrayList<>();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Field field = postSnapshot.getValue(Field.class);
                    if (field != null) {
                        fields.add(field);
                    }
                }
                if (callBack != null) {
                    callBack.onResultReceived(fields);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
