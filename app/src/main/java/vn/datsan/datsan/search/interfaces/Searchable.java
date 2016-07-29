package vn.datsan.datsan.search.interfaces;

import java.util.Map;

/**
 * Created by yennguyen on 7/27/16.
 */
public interface Searchable {

    String getDocumentId();
    Map<String, String> getSearchableSource();
}
