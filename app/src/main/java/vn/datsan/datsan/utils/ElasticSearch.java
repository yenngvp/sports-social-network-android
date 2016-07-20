package vn.datsan.datsan.utils;

import android.os.AsyncTask;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;

import io.searchbox.client.JestClient;
import io.searchbox.core.Delete;
import io.searchbox.core.Index;
import io.searchbox.core.Update;
import io.searchbox.indices.CreateIndex;
import io.searchbox.indices.DeleteIndex;
import io.searchbox.indices.IndicesExists;


/**
 * Created by yennguyen on 6/27/16.
 */
public class Elasticsearch extends AsyncTask<ElasticsearchParam, Void, Void> {

    private static final String TAG = Elasticsearch.class.getName();

    private JestClient jestClient;

    public Elasticsearch() {
        // JestClient is designed to be singleton, don't construct it for each request!
        // See https://github.com/searchbox-io/Jest/tree/master/jest
        if (jestClient == null) {
            // Build connection factory to the Elasticsearch server
            JestClientFactory factory = new JestClientFactory();
            factory.setDroidClientConfig(
                    new DroidClientConfig.Builder(Constants.ELASTICSEARCH_SERVER_URL)
                            .defaultCredentials(Constants.ELASTICSEARCH_USERNAME,
                                    Constants.ELASTICSEARCH_PASSWORD)
                            .multiThreaded(true)
                            .build());
            jestClient = factory.getObject();
        }
    }

    @Override
    protected Void doInBackground(ElasticsearchParam... elasticsearchParams) {
        int count = elasticsearchParams.length;
        ElasticsearchParam param;
        for (int i = 0; i < count; i++) {
            param = elasticsearchParams[i];
            switch (param.getType()) {
                case ADD_INDEX:
                    createIndexIfNotExists(param.getIndexName());
                    break;
                case DELETE_INDEX:
                    deleteIndex(param.getIndexName());
                    break;
                case ADD:
                    createIndexIfNotExists(param.getIndexName());
                    add(param.getIndexName(), param.getIndexType(), param.getSourceObj());
                    break;
                case UPDATE:
                    update(param.getIndexName(), param.getIndexType(), param.getSourceObj());
                    break;
                case DELETE:
                    delete(param.getIndexName(), param.getIndexType(), param.getSourceObj());
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
     * Index document
     */
    private void add(String indexName, String indexType, Object source) {
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
    private void update(String indexName, String indexType, Object source) {
        try {
            String script = "{\n" +
                    "    \"script\" : \"ctx._source.tags += tag\",\n" +
                    "    \"params\" : {\n" +
                    "        \"tag\" : \"blue\"\n" +
                    "    }\n" +
                    "}";
            jestClient.execute(new Update.Builder(script).index(indexName).type(indexType).id("").build());
        } catch (Exception e) {
            AppLog.log(e);
        }
    }

    /**
     * Delete a document
     */
    private void delete(String indexName, String indexType, Object source) {
        try {
            jestClient.execute(new Delete.Builder("").index(indexName).type(indexType).build());
        } catch (Exception e) {
            AppLog.log(e);
        }
    }
}
