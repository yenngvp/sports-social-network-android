package vn.datsan.datsan.utils;

import java.util.Map;

/**
 * Created by yennguyen on 7/5/16.
 */
public class ElasticsearchParam {

    private ElasticsearchEvent type;
    private String indexName;
    private String indexType;
    private Object sourceObj;
    private Map<String, Map> sourceMap;

    public ElasticsearchEvent getType() {
        return type;
    }

    public void setType(ElasticsearchEvent type) {
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

    public Map<String, Map> getSourceMap() {
        return sourceMap;
    }

    public void setSourceMap(Map<String, Map> sourceMap) {
        this.sourceMap = sourceMap;
    }
}
