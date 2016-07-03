package vn.datsan.datsan.utils;

import android.os.AsyncTask;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;

import io.searchbox.client.JestClient;
import io.searchbox.indices.CreateIndex;
import io.searchbox.indices.DeleteIndex;
import io.searchbox.indices.IndicesExists;
import io.searchbox.indices.mapping.PutMapping;

/**
 * Created by yennguyen on 6/27/16.
 */
public class Elasticsearch extends AsyncTask<Void, Void, Long> {

    private static final String TAG = Elasticsearch.class.getName();

    private JestClient jestClient;
    private FirebaseDatabase firebaseDatabase;

    @Override
    protected Long doInBackground(Void... voids) {
        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("app");

        // Build connection factory to the Elasticsearch server
        JestClientFactory factory = new JestClientFactory();
        factory.setDroidClientConfig(
                new DroidClientConfig.Builder(Constants.ELASTICSEARCH_SERVER_URL)
                        .defaultCredentials("po0j5nr3", "l4xxk8xp2z77890t")
                        .multiThreaded(true)
                        .build());
        jestClient = factory.getObject();

        // Creates search index if it not exist
        createIndexIfNotExists();

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

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

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    /**
     * Create or update elasticsearch index
     */
    private void createIndexIfNotExists() {
        try {
            boolean indexExists = jestClient.execute(new IndicesExists.Builder(Constants.APP_INDEX).build()).isSucceeded();
            if (!indexExists) {
                jestClient.execute(new CreateIndex.Builder(Constants.APP_INDEX).build());
            }
        } catch (Exception e) {
            AppLog.log(AppLog.LogType.LOG_ERROR, TAG, e.getMessage());
        }
    }

    private void removeIndex() {
        try {
            boolean indexExists = jestClient.execute(new IndicesExists.Builder(Constants.APP_INDEX).build()).isSucceeded();
            if (indexExists) {
                jestClient.execute(new DeleteIndex.Builder(Constants.APP_INDEX).build());
            }
        } catch (Exception e) {
            AppLog.log(AppLog.LogType.LOG_ERROR, TAG, e.getMessage());
        }
    }

}
