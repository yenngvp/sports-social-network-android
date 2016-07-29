package vn.datsan.datsan.utils.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import vn.datsan.datsan.utils.AppLog;
import vn.datsan.datsan.utils.Constants;
import vn.datsan.datsan.search.Elasticsearch;
import vn.datsan.datsan.search.ElasticsearchEvent;
import vn.datsan.datsan.search.ElasticsearchParam;
import vn.datsan.datsan.search.interfaces.Searchable;

/**
 * Created by yennguyen on 7/22/16.
 */
public class FirebaseChildEventListener implements ChildEventListener {

    private static final String TAG = FirebaseChildEventListener.class.getName();
    private Class indexedClazz;

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        buildElasticsearch(ElasticsearchEvent.ADD, dataSnapshot);
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        buildElasticsearch(ElasticsearchEvent.UPDATE, dataSnapshot);
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        buildElasticsearch(ElasticsearchEvent.DELETE, dataSnapshot);
    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    private void buildElasticsearch(ElasticsearchEvent eventType, DataSnapshot dataSnapshot) {
        AppLog.log(AppLog.LogType.LOG_DEBUG, TAG,
                eventType.toString() + " a document type" + indexedClazz.getName());

        ElasticsearchParam param = new ElasticsearchParam();
        param.setEventType(eventType);
        param.setIndexName(Constants.ELASTICSEARCH_INDEX);
        param.setIndexType(indexedClazz.getName());

        final ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper
        Object object = mapper.convertValue(dataSnapshot.getValue(), getIndexedClazz());

        if (object != null && object instanceof Searchable) {
            param.setSource((Searchable) object);
            new Elasticsearch().execute(param);
        } else {
            AppLog.log(AppLog.LogType.LOG_ERROR, "Elasticsearch", "Unknown object for indexing");
        }
    }

    public Class getIndexedClazz() {
        return indexedClazz;
    }

    public void setEventClazz(Class indexedClazz) {
        this.indexedClazz = indexedClazz;
    }
}
