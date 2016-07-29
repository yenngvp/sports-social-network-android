package vn.datsan.datsan.utils;

import java.util.HashMap;
import java.util.Map;

import vn.datsan.datsan.models.Field;
import vn.datsan.datsan.utils.interfaces.Searchable;

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
    private String searchText;

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

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }
}
