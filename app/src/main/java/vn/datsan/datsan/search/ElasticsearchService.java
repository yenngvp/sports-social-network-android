package vn.datsan.datsan.search;

import vn.datsan.datsan.serverdata.CallBack;
import vn.datsan.datsan.utils.Constants;

/**
 * Created by yennguyen on 7/29/16.
 */
public class ElasticsearchService {

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

    public void search(SearchOption searchOption, CallBack.OnSearchResultListener searchResultListener) {
        ElasticsearchParam param = new ElasticsearchParam();
        param.setEventType(ElasticsearchEvent.SEARCH);
        param.setIndexName(Constants.ELASTICSEARCH_INDEX);
        param.setSearchOption(searchOption);
        param.setSearchResultListener(searchResultListener);
        new Elasticsearch().execute(param);
    }

}
