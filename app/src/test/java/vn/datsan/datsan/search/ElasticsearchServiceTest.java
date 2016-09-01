package vn.datsan.datsan.search;

import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.HttpClients;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import io.searchbox.core.SearchResult;
import vn.datsan.datsan.models.Field;
import vn.datsan.datsan.models.User;
import vn.datsan.datsan.serverdata.CallBack;
import vn.datsan.datsan.utils.AppLog;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by yennguyen on 7/30/16.
 */
public class ElasticsearchServiceTest {

    private static final String TAG = ElasticsearchServiceTest.class.getName();

    @BeforeClass
    public static void setup() {
        ElasticsearchService.getInstance().createIndex();
    }

    /**
     * Test search User keywords
     */
    @Test
    public void testSearchUser() {

        String keyword = "yen";
        List<String> searchTypes = new ArrayList<>();
        searchTypes.add(User.class.getSimpleName());
        SearchOption searchOption = new SearchOption(keyword, searchTypes);
    }
}
