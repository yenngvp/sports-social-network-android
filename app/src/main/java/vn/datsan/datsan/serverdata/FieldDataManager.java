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
import vn.datsan.datsan.utils.RawIO;
import vn.datsan.datsan.utils.AppLog;

/**
 * Created by xuanpham on 6/20/16.
 */
public class FieldDataManager {
    private static final String TAG = FieldDataManager.class.getName();

    private static FieldDataManager instance;
    private final String _Database_Url = "https://social-sport-b1cff.firebaseio.com/app/fields";
    private DatabaseReference fieldLocation = FirebaseDatabase.getInstance().getReference("app/fields");

    private FieldDataManager() {
//        fieldLocation = new Firebase(_Database_Url);
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

    public void search(String text) {
//        fieldLocation.
    }

    public void genFakeFields(Context context) {
        String content = RawIO.loadStringFromRawResource(context.getResources(), R.raw.fields);
        Gson gson = new Gson();
        List<FakeStadium> stadiums = gson.fromJson(content, new TypeToken<ArrayList<FakeStadium>>(){}.getType());
        AppLog.log(AppLog.LogType.LOG_DEBUG, TAG, String.valueOf(stadiums.size()));

        for (FakeStadium stadium : stadiums) {
            if (stadium.getPhone() == null || stadium.getPhone().isEmpty())
                continue;
            Field field = new Field();
            field.setName(stadium.getName());
            field.setAddress(stadium.getAddress());
            String phone = stadium.getPhone().trim().replace(".", "");
            field.setPhone(phone);
            field.setDistrict(stadium.getDistrict());
            field.setLocation(stadium.getLocation());
            addField(field);
            AppLog.log(AppLog.LogType.LOG_DEBUG, TAG, "insert " + stadium.getName());
        }
    }
}
