package vn.datsan.datsan.utils;

import android.os.AsyncTask;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by yennguyen on 7/5/16.
 */
public class ElasticsearchTask  extends AsyncTask<Void, Void, Long> {

    private static final String TAG = ElasticsearchTask.class.getName();

    private FirebaseDatabase firebaseDatabase;

    @Override
    protected Long doInBackground(Void... voids) {
        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("app");

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                AppLog.log(AppLog.LogType.LOG_DEBUG, TAG, "onChildAdded");
                // Create index as object's key, such as: users, groups or fields
                ElasticsearchParam param = new ElasticsearchParam();
                param.setType(ElasticsearchEvent.ADD);
                param.setIndexName(Constants.ELASTICSEARCH_INDEX);
                param.setIndexType(dataSnapshot.getKey());
                param.setSourceObj(dataSnapshot.getValue());
                new Elasticsearch().execute(param);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                AppLog.log(AppLog.LogType.LOG_DEBUG, TAG, "onChildChanged");
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                AppLog.log(AppLog.LogType.LOG_DEBUG, TAG, "onChildRemoved");

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return null;
    }
}
