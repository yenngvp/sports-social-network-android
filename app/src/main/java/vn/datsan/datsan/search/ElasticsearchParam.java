package vn.datsan.datsan.search;

import java.util.Map;

import vn.datsan.datsan.serverdata.CallBack;
import vn.datsan.datsan.search.interfaces.Searchable;

/**
 * Created by yennguyen on 7/5/16.
 */
public class ElasticsearchParam {

    private ElasticsearchEvent type;
    private String indexName;
    private String indexType;
    private Object sourceObj;
    private Map sourceMap;
    private Searchable source;
    private SearchOption searchOption;
    private CallBack.OnSearchResultListener searchResultListener;

    public ElasticsearchEvent getType() {
        return type;
    }

    public void setEventType(ElasticsearchEvent type) {
        this.type = type;
    }

    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public String getIndexType() {
        return indexType;
    }

    public void setIndexType(String indexType) {
        this.indexType = indexType;
    }

    public Object getSourceObj() {
        return sourceObj;
    }

    public void setSourceObj(Object sourceObj) {
        this.sourceObj = sourceObj;
    }

    public Map getSourceMap() {
        return sourceMap;
    }

    public void setSourceMap(Map sourceMap) {
        this.sourceMap = sourceMap;
    }

    public Searchable getSource() {
        return source;
    }

    public void setSource(Searchable source) {
        this.source = source;
    }

    public void setType(ElasticsearchEvent type) {
        this.type = type;
    }

    public SearchOption getSearchOption() {
        return searchOption;
    }

    public void setSearchOption(SearchOption searchOption) {
        this.searchOption = searchOption;
    }

    public CallBack.OnSearchResultListener getSearchResultListener() {
        return searchResultListener;
    }

    public void setSearchResultListener(CallBack.OnSearchResultListener searchResultListener) {
        this.searchResultListener = searchResultListener;
    }
}
