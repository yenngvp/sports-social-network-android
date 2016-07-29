package vn.datsan.datsan.utils.interfaces;

import java.util.Map;

import vn.datsan.datsan.utils.listeners.FirebaseChildEventListener;

/**
 * Created by yennguyen on 7/27/16.
 */
public interface Searchable {

    String getDocumentId();
    Map<String, String> getSearchableSource();
}
