package vn.datsan.datsan.serverdata;

import com.google.firebase.database.DatabaseReference;

/**
 * Created by yennguyen on 8/21/16.
 */
public class FirebaseService {

    protected int startAt;
    protected int endAt;

    private DatabaseReference databaseReference;

    public FirebaseService() {
    }

    public Object getObjectsForRange(int startAt, int endAt) {
        return null;
    }

    public DatabaseReference getDatabaseReference() {
        return databaseReference;
    }

    public void setDatabaseReference(DatabaseReference databaseReference) {
        this.databaseReference = databaseReference;
    }

    public int getStartAt() {
        return startAt;
    }

    public void setStartAt(int startAt) {
        this.startAt = startAt;
    }

    public int getEndAt() {
        return endAt;
    }

    public void setEndAt(int endAt) {
        this.endAt = endAt;
    }
}
