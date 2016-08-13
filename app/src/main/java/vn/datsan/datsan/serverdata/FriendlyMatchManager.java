package vn.datsan.datsan.serverdata;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import vn.datsan.datsan.models.Field;
import vn.datsan.datsan.models.FriendlyMatch;
import vn.datsan.datsan.search.ElasticsearchService;
import vn.datsan.datsan.utils.AppLog;
import vn.datsan.datsan.utils.Constants;
import vn.datsan.datsan.utils.listeners.FirebaseChildEventListener;

/**
 * Created by xuanpham on 8/12/16.
 */
public class FriendlyMatchManager {
    private static final String TAG = FieldManager.class.getName();
    private static FriendlyMatchManager instance;
    private DatabaseReference matchDatabaseRef;
    private List<FriendlyMatch> friendlyMatches;

    private FirebaseChildEventListener firebaseChildEventListener;

    private FriendlyMatchManager() {
        matchDatabaseRef = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_MATCHS);

        // Enable 'Searchable' put mapping for the managed underline User
        ElasticsearchService.getInstance().putMapping(Field.getPutMapping(), Field.class);
        // Listening on the field object change
        firebaseChildEventListener = new FirebaseChildEventListener(Field.class);
        matchDatabaseRef.addChildEventListener(firebaseChildEventListener);
    }

    @Override
    public void finalize() throws Throwable {

        AppLog.i(TAG, "Going finalize(). Detaching firebaseChildEventListener");
        // Detach the FirebaseChildEventListener when the database reference detached
        matchDatabaseRef.removeEventListener(firebaseChildEventListener);

        super.finalize();
    }

    public static FriendlyMatchManager getInstance() {
        if (instance == null) {
            instance = new FriendlyMatchManager();
        }
        return instance;
    }

    public void addMatch(FriendlyMatch match) {
        String key = matchDatabaseRef.push().getKey();
        match.setId(key);
        matchDatabaseRef.child(key).setValue(match);
    }

    public void getMatch(String id, final CallBack.OnResultReceivedListener callBack) {
        matchDatabaseRef.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Field match = dataSnapshot.getValue(Field.class);
                if (callBack != null)
                    callBack.onResultReceived(match);
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
                AppLog.log(AppLog.LogType.LOG_DEBUG, TAG, firebaseError.getMessage());
                if (callBack != null)
                    callBack.onResultReceived(null);
            }
        });
    }

    public List<FriendlyMatch> getMaths(final CallBack.OnResultReceivedListener callBack) {

        if (friendlyMatches != null && !friendlyMatches.isEmpty()) {
            return friendlyMatches;
        }

        matchDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                friendlyMatches = new ArrayList<>();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    FriendlyMatch match = postSnapshot.getValue(FriendlyMatch.class);
                    if (match != null)
                        friendlyMatches.add(match);
                }
                if (callBack != null)
                    callBack.onResultReceived(friendlyMatches);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        return friendlyMatches;
    }
}
