package vn.datsan.datsan.search;

import java.util.ArrayList;
import java.util.List;

import vn.datsan.datsan.utils.Constants;

/**
 * Created by yennguyen on 7/29/16.
 */
public class SearchOption {
    private String keyword;
    private List<String> indices = new ArrayList<>();
    private List<String> types;

    public SearchOption() {

    }

    public SearchOption(String keyword) {
        this.keyword = keyword;
        // Defaulted search index
        this.indices.add(Constants.ELASTICSEARCH_INDEX);
    }

    public SearchOption(String keyword, List<String> types) {
        this.keyword = keyword;
        this.types = types;
        // Defaulted search index
        this.indices.add(Constants.ELASTICSEARCH_INDEX);
    }

    public SearchOption(String keyword, List<String> indices, List<String> types) {
        this.keyword = keyword;
        this.indices = indices;
        this.types = types;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public List<String> getIndices() {
        return indices;
    }

    public void setIndices(List<String> indices) {
        this.indices = indices;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }
}
