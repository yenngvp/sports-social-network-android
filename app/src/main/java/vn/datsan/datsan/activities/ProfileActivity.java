package vn.datsan.datsan.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.searchbox.core.SearchResult;
import vn.datsan.datsan.R;
import vn.datsan.datsan.models.Field;
import vn.datsan.datsan.models.User;
import vn.datsan.datsan.search.ElasticsearchService;
import vn.datsan.datsan.search.SearchOption;
import vn.datsan.datsan.search.interfaces.Searchable;
import vn.datsan.datsan.serverdata.CallBack;
import vn.datsan.datsan.serverdata.UserManager;
import vn.datsan.datsan.ui.customwidgets.SimpleProgress;
import vn.datsan.datsan.utils.AppLog;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = ProfileActivity.class.getName();

    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.years)
    TextView years;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        SimpleProgress.show(ProfileActivity.this);
        UserManager.getInstance().getCurrentUserInfo(profileFetchCallBack);

        /*
         * Search a text
         */
        final String keyword = "946 280 281";
        List<String> searchTypes = new ArrayList<>();
        searchTypes.add(User.class.getSimpleName());
        searchTypes.add(Field.class.getSimpleName());
        SearchOption searchOption = new SearchOption(keyword, searchTypes);
        ElasticsearchService.getInstance().search(searchOption, new CallBack.OnSearchResultListener() {
            @Override
            public void onSearchResult(SearchResult searchResult) {
                if (searchResult == null) {
                    // No search result found
                    return;
                }

                AppLog.log(AppLog.LogType.LOG_DEBUG, TAG, "Search result callback returns: " + searchResult.getTotal());

                // Get search result type fields
                List<SearchResult.Hit<Field, Void>> fieldHits = searchResult.getHits(Field.class);
                for (SearchResult.Hit hit : fieldHits) {
                    String type = hit.type;
                    Object source = hit.source;

                    AppLog.log(AppLog.LogType.LOG_DEBUG, TAG, "Found a " + type + " : " + source);
                }
            }
        });
    }

    CallBack.OnResultReceivedListener profileFetchCallBack = new CallBack.OnResultReceivedListener() {
        @Override
        public void onResultReceived(Object result) {
            SimpleProgress.dismiss();

            // reload view
            if (result != null) {
                reloadView((User) result);
            } else {
                Toast.makeText(ProfileActivity.this, "Error !!!", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void reloadView(User user) {
        name.setText(user.getName());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
