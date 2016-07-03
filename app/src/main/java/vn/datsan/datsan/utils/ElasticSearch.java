package vn.datsan.datsan.utils;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;

import io.searchbox.client.JestClient;
import io.searchbox.indices.mapping.PutMapping;

/**
 * Created by yennguyen on 6/27/16.
 */
public class Elasticsearch {

    private static final String TAG = Elasticsearch.class.getName();

    private JestClient jestClient;
    private FirebaseDatabase firebaseDatabase;

    public Elasticsearch() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("app");

        // Build connection factory to the Elasticsearch server
        JestClientFactory factory = new JestClientFactory();
        factory.setDroidClientConfig(
                new DroidClientConfig.Builder(Constants.ELASTICSEARCH_SERVER_URL)
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

    }


    /**
     * Create or update elasticsearch index
     */
    private void createIndexIfNotExists() {
        try {
            PutMapping putMapping = new PutMapping.Builder(
                    "my_index",
                    "my_type",
                    "{ \"document\" : { \"properties\" : { \"message\" : {\"type\" : \"string\", \"store\" : \"yes\"} } } }"
            ).build();
           // boolean indexExists = jestClient.execute(new CreateIndex.Builder("socialsport").build()).isSucceeded();
//            jestClient.execute(new CreateIndex.Builder("articles").build());
            jestClient.execute(putMapping);

//            if (!indexExists) {
//                //jestClient.execute(new CreateIndex.Builder("socialsport").build());
//            }
        } catch (Exception e) {
            AppLog.log(AppLog.LogType.LOG_ERROR, TAG, "Unable to execute the JestClient command! " + e.getMessage());

        }
    }

//    private void removeIndex(DataSnapshot snapshot) {
//
//    }


}
