package vn.datsan.datsan.search;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.database.DataSnapshot;

import vn.datsan.datsan.search.interfaces.Searchable;
import vn.datsan.datsan.serverdata.CallBack;
import vn.datsan.datsan.utils.AppLog;
import vn.datsan.datsan.utils.Constants;

/**
 * Created by yennguyen on 7/29/16.
 */
public class ElasticsearchService {

    private static final String TAG = ElasticsearchService.class.getName();

    private static ElasticsearchService instance = null;

    private ElasticsearchService() {};

    public static ElasticsearchService getInstance() {
        if (instance == null) {
            instance = new ElasticsearchService();
        }
        return instance;
    }

    public void createIndex() {
        ElasticsearchParam param = new ElasticsearchParam();
        param.setEventType(ElasticsearchEvent.CREATE_INDEX);
        param.setIndexName(Constants.ELASTICSEARCH_INDEX);
        new Elasticsearch().execute(param);
    }

    public void putMapping(String putMapping, Class eventClazz) {
        ElasticsearchParam param = new ElasticsearchParam();
        param.setEventType(ElasticsearchEvent.PUT_MAPPING);
        param.setIndexName(Constants.ELASTICSEARCH_INDEX);
        param.setIndexType(eventClazz.getSimpleName());
        param.setPutMapping(putMapping);
        new Elasticsearch().execute(param);
    }

    public void search(SearchOption searchOption, CallBack.OnSearchResultListener searchResultListener) {
        ElasticsearchParam param = new ElasticsearchParam();
        param.setEventType(ElasticsearchEvent.SEARCH);
        param.setIndexName(Constants.ELASTICSEARCH_INDEX);
        param.setSearchOption(searchOption);
        param.setSearchResultListener(searchResultListener);
        new Elasticsearch().execute(param);
    }

    public void add(DataSnapshot dataSnapshot, Class eventClazz) {
        build(ElasticsearchEvent.ADD, dataSnapshot, eventClazz);
    }

    public void update(DataSnapshot dataSnapshot, Class eventClazz) {
        build(ElasticsearchEvent.UPDATE, dataSnapshot, eventClazz);
    }

    public void delete(DataSnapshot dataSnapshot, Class eventClazz) {
        build(ElasticsearchEvent.DELETE, dataSnapshot, eventClazz);
    }

    private void build(ElasticsearchEvent eventType, DataSnapshot dataSnapshot, Class eventClazz) {
        AppLog.log(AppLog.LogType.LOG_DEBUG, TAG,
                eventType.toString() + " a document type " + eventClazz.getSimpleName());

        ElasticsearchParam param = new ElasticsearchParam();
        param.setEventType(eventType);
        param.setIndexName(Constants.ELASTICSEARCH_INDEX);
        param.setIndexType(eventClazz.getSimpleName());

        final ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper
        Object object = mapper.convertValue(dataSnapshot.getValue(), eventClazz);

        if (object != null && object instanceof Searchable) {
            param.setSource((Searchable) object);
            new Elasticsearch().execute(param);
        } else {
            AppLog.log(AppLog.LogType.LOG_ERROR, "Elasticsearch", "Unknown object for indexing");
        }
    }
}
