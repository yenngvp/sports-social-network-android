package vn.datsan.datsan.serverdata;

import io.searchbox.core.SearchResult;

/**
 * Created by xuanpham on 6/20/16.
 */
public class CallBack {
    public interface OnResultReceivedListener {
        void onResultReceived(Object result);
    }
}
