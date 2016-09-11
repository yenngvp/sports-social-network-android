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
import io.searchbox.indices.mapping.PutMapping;
import vn.datsan.datsan.serverdata.CallBack;
import vn.datsan.datsan.utils.AppConstants;
import vn.datsan.datsan.utils.AppLog;
import vn.datsan.datsan.search.interfaces.Searchable;


/**
 * Created by yennguyen on 6/27/16.
 */
public class Elasticsearch extends AsyncTask<ElasticsearchParam, Void, Object> {

    private static final String TAG = Elasticsearch.class.getName();

    private static JestClient jestClient;
    private CallBack.OnResultReceivedListener callback;
    public Elasticsearch() {

        // JestClient is designed to be singleton, don't construct it for each request!
        // See https://github.com/searchbox-io/Jest/tree/master/jest
        if (jestClient == null) {

            if (AppConstants.ELASTICSEARCH_USERNAME == null || "".equals(AppConstants.ELASTICSEARCH_USERNAME)) {
                // Not required authorisation
                // Build connection factory to the Elasticsearch server
                JestClientFactory factory = new JestClientFactory();
                factory.setDroidClientConfig(
                        new DroidClientConfig
                                .Builder(AppConstants.ELASTICSEARCH_SERVER_URL)
                                .multiThreaded(true)
                                .build());
                jestClient = factory.getObject();
            } else {
                // Build connection factory to the Elasticsearch server
                JestClientFactory factory = new JestClientFactory();
                factory.setDroidClientConfig(
                        new DroidClientConfig
                                .Builder(AppConstants.ELASTICSEARCH_SERVER_URL)
                                .defaultCredentials(AppConstants.ELASTICSEARCH_USERNAME,
                                        AppConstants.ELASTICSEARCH_PASSWORD)
                                .multiThreaded(true)
                                .build());
                jestClient = factory.getObject();
            }
        }

    }

    @Override
    protected Object doInBackground(ElasticsearchParam... elasticsearchParams) {

        Object result = null;
        ElasticsearchParam param;
        for (ElasticsearchParam elasticsearchParam : elasticsearchParams) {
            param = elasticsearchParam;
            callback = param.getSearchResultListener();

            switch (param.getType()) {
                case CREATE_INDEX:
                    createIndexIfNotExists(param.getIndexName());
                    break;
                case DELETE_INDEX:
                    deleteIndex(param.getIndexName());
                    break;
                case PUT_MAPPING:
                    putMapping(param.getIndexName(), param.getIndexType(), param.getPutMapping());
                    break;
                case ADD:
                    add(param.getIndexName(), param.getIndexType(), param.getSource());
                    break;
                case UPDATE:
                    update(param.getIndexName(), param.getIndexType(), param.getSource());
                    break;
                case DELETE:
                    delete(param.getIndexName(), param.getIndexType(), param.getSource());
                    break;
                case SEARCH:
                    result = search(param.getSearchOption());
                default:
                    break;
            }
        }

        return result;
    }

    @Override
    protected void onPostExecute(Object result) {
        super.onPostExecute(result);
        if (callback != null) {
            callback.onResultReceived(result);
        }
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
     * Put mapping type
     */
    private void putMapping(String indexName, String indexType, String mapping) {
        try {
            PutMapping putMapping = new PutMapping.Builder(
                    indexName,
                    indexType,
                    mapping
            ).build();
            jestClient.execute(putMapping);
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
            AppLog.d(TAG, "Update script: " + scriptBuilder.toString());

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
    private SearchResult search(SearchOption searchOption) {

        try {
            String query;
            if (searchOption.getFilteredDistance() != null) {
                // Searching with geolocation awareness
                // Basically, the query means "Find me the content matched with 'the keyword' and around 'filtedDistance' from a point (lat,lon)"
                query = "{\n" +
                        "    \"from\": \"" + searchOption.getFrom() + "\",\n" +
                        "    \"size\": \"" + searchOption.getSize() + "\",\n" +
                        "    \"query\": {\n" +
                        "        \"filtered\" : {\n" +
                        "            \"query\" : {\n" +
                        "                \"query_string\" : {\n" +
                        "                    \"query\" : \"" + searchOption.getKeyword()  + "\"\n" +
                        "                }\n" +
                        "            },\n" +
                        "            \"filter\" : {\n" +
                        "                \"geo_distance\" : {\n" +
                        "                      \"distance\": \"" + searchOption.getFilteredDistance() + "\",\n" +
                        "                      \"location\": { \n" +
                        "                           \"lat\": \"" + searchOption.getLat() + "\",\n" +
                        "                           \"lon\": \"" + searchOption.getLon() + "\"\n" +
                        "                      }\n" +
                        "                 }\n" +
                        "            }\n" +
                        "        }\n" +
                        "    },\n" +
                        "    \"sort\": [\n" +
                        "        {\n" +
                        "            \"_geo_distance\": {\n" +
                        "                   \"location\": {\n" +
                        "                       \"lat\":  \"" + searchOption.getLat() + "\",\n" +
                        "                       \"lon\":  \"" + searchOption.getLon() + "\"\n" +
                        "                   },\n" +
                        "                   \"order\": \"asc\",\n" +
                        "                   \"unit\":  \"" + searchOption.getDistanceUnit() + "\"\n" +
                        "             }\n" +
                        "        }\n" +
                        "     ]\n" +
                        "}";
            } else {
                // Basically, the query means "Find me the content matched with 'the keyword', NO location awareness
                query = "{\n" +
                        "    \"from\": \"" + searchOption.getFrom() + "\",\n" +
                        "    \"size\": \"" + searchOption.getSize() + "\",\n" +
                        "    \"query\": {\n" +
                        "        \"filtered\" : {\n" +
                        "            \"query\" : {\n" +
                        "                \"query_string\" : {\n" +
                        "                    \"query\" : \"" + searchOption.getKeyword()  + "\"\n" +
                        "                }\n" +
                        "            },\n" +
                        "            \"filter\" : {\n" +
                        "            }\n" +
                        "        }\n" +
                        "    }\n" +
                        "}";
            }
            AppLog.d(TAG, "Query string: " + query);

            Search search = new Search.Builder(query)
                    .addIndex(searchOption.getIndices())
                    .addType(searchOption.getTypes())
                    .build();

            SearchResult result = jestClient.execute(search);
            return result;

        } catch (Exception e) {
            AppLog.log(e);
        }
        return null;
    }

}
