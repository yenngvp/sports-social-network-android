package vn.datsan.datsan.search;

import java.util.ArrayList;
import java.util.List;

import io.searchbox.core.SearchResult;
import vn.datsan.datsan.models.Field;
import vn.datsan.datsan.serverdata.CallBack;
import vn.datsan.datsan.utils.AppLog;

/**
 * Created by xuanpham on 8/5/16.
 */
public class AppSearch {

    public static void searchField(String keyword, String distance, float lati, float longi, final CallBack.OnResultReceivedListener callback) {

        List<String> searchTypes = new ArrayList<>();
        searchTypes.add(Field.class.getSimpleName());
        SearchOption searchOption = new SearchOption(keyword, searchTypes);
        searchOption.setFilteredDistance("1km");
        searchOption.setLatLon("10.77533", "106.69453");
//        ElasticsearchService.getInstance().search(searchOption, callback);
        ElasticsearchService.getInstance().search(searchOption, new CallBack.OnSearchResultListener() {
            @Override
            public void onSearchResult(SearchResult searchResult) {
                if (searchResult == null) {
                    // No search result found
                    if (callback != null)
                        callback.onResultReceived(null);
                    return;
                }

                AppLog.log(AppLog.LogType.LOG_INFO, "TAG", "Search result callback returns: " + searchResult.getTotal());

                // Get search result type fields
                List<SearchResult.Hit<Field, Void>> fieldHits = searchResult.getHits(Field.class);
                List<Field> fields = new ArrayList<>();
                for (SearchResult.Hit hit : fieldHits) {
                    String type = hit.type;
                    Object source = hit.source;
                    fields.add((Field) source);

                    AppLog.log(AppLog.LogType.LOG_INFO, "TAG", "Found a " + type + " : " + source);
                }

                if (callback != null)
                    callback.onResultReceived(fields);
            }
        });
    }
}
