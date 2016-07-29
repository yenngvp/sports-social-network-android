package vn.datsan.datsan.search;

import android.os.AsyncTask;

import java.util.Map;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;

import io.searchbox.client.JestClient;
import io.searchbox.core.Delete;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.core.Update;
import io.searchbox.indices.CreateIndex;
import io.searchbox.indices.DeleteIndex;
import io.searchbox.indices.IndicesExists;
import vn.datsan.datsan.serverdata.CallBack;
import vn.datsan.datsan.utils.AppLog;
import vn.datsan.datsan.utils.Constants;
import vn.datsan.datsan.search.interfaces.Searchable;


/**
 * Created by yennguyen on 6/27/16.
 */
public class Elasticsearch extends AsyncTask<ElasticsearchParam, Void, Void> {

    private static final String TAG = Elasticsearch.class.getName();

    private static JestClient jestClient;

    public Elasticsearch() {

        // JestClient is designed to be singleton, don't construct it for each request!
        // See https://github.com/searchbox-io/Jest/tree/master/jest
        //  .defaultCredentials(Constants.ELASTICSEARCH_USERNAME,
        //        Constants.ELASTICSEARCH_PASSWORD)
        if (jestClient == null) {
            // Build connection factory to the Elasticsearch server
            JestClientFactory factory = new JestClientFactory();
            factory.setDroidClientConfig(
                    new DroidClientConfig
                            .Builder(Constants.ELASTICSEARCH_SERVER_URL)
                            .multiThreaded(true)
                            .build());
            jestClient = factory.getObject();
        }

    }

    @Override
    protected Void doInBackground(ElasticsearchParam... elasticsearchParams) {

        ElasticsearchParam param;
        for (ElasticsearchParam elasticsearchParam : elasticsearchParams) {
            param = elasticsearchParam;
            switch (param.getType()) {
                case CREATE_INDEX:
                    createIndexIfNotExists(param.getIndexName());
                    break;
                case DELETE_INDEX:
                    deleteIndex(param.getIndexName());
                    break;
                case ADD:
                    createIndexIfNotExists(param.getIndexName());
                    if (param.getSourceObj() != null) {
                        add(param.getIndexName(), param.getIndexType(), param.getSource());
                    } else if (param.getSourceMap() != null) {
                        add(param.getIndexName(), param.getIndexType(), param.getSourceMap());
                    }
                    break;
                case UPDATE:
                    update(param.getIndexName(), param.getIndexType(), param.getSource());
                    break;
                case DELETE:
                    delete(param.getIndexName(), param.getIndexType(), param.getSource());
                    break;
                case SEARCH:
                    search(param.getSearchOption(), param.getSearchResultListener());
                    break;
                default:
                    break;
            }
        }

        return null;
    }

    /**
     * Create index
     */
    private void createIndexIfNotExists(String index) {
        try {
            boolean indexExists = jestClient.execute(new IndicesExists.Builder(index).build()).isSucceeded();
            if (!indexExists) {
                jestClient.execute(new CreateIndex.Builder(index).build());
            }
        } catch (Exception e) {
            AppLog.log(e);
        }
    }

    /**
     * Remove index
     */
    private void deleteIndex(String index) {
        try {
            boolean indexExists = jestClient.execute(new IndicesExists.Builder(index).build()).isSucceeded();
            if (indexExists) {
                jestClient.execute(new DeleteIndex.Builder(index).build());
            }
        } catch (Exception e) {
            AppLog.log(e);
        }
    }

    /**
     * Index document from an object
     */
    private void add(String indexName, String indexType, Searchable source) {

        try {
            Index index = new Index.Builder(source.getSearchableSource()).index(indexName).type(indexType).id(source.getDocumentId()).build();
            jestClient.execute(index);
        } catch (Exception e) {
            AppLog.log(e);
        }
    }

    /**
     * Index document from a hash map
     */
    private void add(String indexName, String indexType, Map source) {
        try {
            Index index = new Index.Builder(source).index(indexName).type(indexType).build();
            jestClient.execute(index);
        } catch (Exception e) {
            AppLog.log(e);
        }
    }

    /**
     * Update document
     */
    private void update(String indexName, String indexType, Searchable source) {

        try {
            if (source.getSearchableSource().isEmpty()) {
                return; // nothing to update
            }

            // Build update script
            StringBuilder scriptBuilder = new StringBuilder(
                    "{\n" +
                    "  \"doc\" : {\n"
            );
            for (Map.Entry<String, String> e : source.getSearchableSource().entrySet()) {
                scriptBuilder.append(e.getKey()).append(" : ").append(e.getValue()).append(",\n");
            }
            // replace to last comma ','
            scriptBuilder.delete(scriptBuilder.lastIndexOf(",\n"), scriptBuilder.length());
            scriptBuilder.append("}\n}");
            scriptBuilder.append("}\n}");
            AppLog.log(AppLog.LogType.LOG_DEBUG, TAG, "Update script: " + scriptBuilder.toString());

            jestClient.execute(new Update.Builder(scriptBuilder.toString()).index(indexName).type(indexType).id(source.getDocumentId()).build());
        } catch (Exception e) {
            AppLog.log(e);
        }
    }

    /**
     * Delete a document
     */
    private void delete(String indexName, String indexType, Searchable source) {
        try {
            jestClient.execute(new Delete.Builder(source.getDocumentId()).index(indexName).type(indexType).build());
        } catch (Exception e) {
            AppLog.log(e);
        }
    }

    /**
     * Search
     */
    private void search(SearchOption searchOption, CallBack.OnSearchResultListener searchResultListener) {

        try {
            String query = "{\n" +
                    "    \"query\": {\n" +
                    "        \"filtered\" : {\n" +
                    "            \"query\" : {\n" +
                    "                \"query_string\" : {\n" +
                    "                    \"query\" : " + searchOption.getKeyword()  + "\n" +
                    "                }\n" +
                    "            },\n" +
                    "            \"filter\" : {\n" +
                    "            }\n" +
                    "        }\n" +
                    "    }\n" +
                    "}";
            AppLog.log(AppLog.LogType.LOG_DEBUG, TAG, "Query string: " + query);

            Search search = new Search.Builder(query)
                    .addIndex(searchOption.getIndices())
                    .addType(searchOption.getTypes())
                    .build();

            SearchResult result = jestClient.execute(search);
            if (result.isSucceeded()) {
                AppLog.log(AppLog.LogType.LOG_DEBUG, TAG, "Search result for keyword \"" + searchOption.getKeyword() + "\"" + " found " + result.getTotal());

                if (searchResultListener != null) {
                    searchResultListener.onSearchResult(result);
                }
            } else {
                AppLog.log(AppLog.LogType.LOG_ERROR, TAG, "Search result for keyword \"" + searchOption.getKeyword() + "\"" + " error: " + result.getErrorMessage());

                if (searchResultListener != null) {
                    searchResultListener.onSearchResult(null);
                }
            }

        } catch (Exception e) {
            AppLog.log(e);
        }
    }

}
