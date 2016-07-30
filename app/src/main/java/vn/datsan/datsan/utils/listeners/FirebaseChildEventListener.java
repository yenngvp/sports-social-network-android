package vn.datsan.datsan.utils.listeners;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import vn.datsan.datsan.search.ElasticsearchService;

/**
 * Created by yennguyen on 7/22/16.
 */
public class FirebaseChildEventListener implements ChildEventListener {

    private static final String TAG = FirebaseChildEventListener.class.getName();
    private Class eventClazz;

    public FirebaseChildEventListener() {
        super();
    }

    public FirebaseChildEventListener(Class eventClazz) {
        this.eventClazz = eventClazz;
    }


    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        ElasticsearchService.getInstance().add(dataSnapshot, eventClazz);
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        ElasticsearchService.getInstance().update(dataSnapshot, eventClazz);
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        ElasticsearchService.getInstance().delete(dataSnapshot, eventClazz);
    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    public Class getEventClazz() {
        return eventClazz;
    }

    public void setEventClazz(Class indexedClazz) {
        this.eventClazz = indexedClazz;
    }
}
