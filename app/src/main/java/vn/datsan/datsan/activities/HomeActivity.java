package vn.datsan.datsan.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.searchbox.core.SearchResult;
import vn.datsan.datsan.R;
import vn.datsan.datsan.fragments.SportClubFragment;
import vn.datsan.datsan.fragments.FriendlyMatchFragment;
import vn.datsan.datsan.fragments.SportFieldFragment;
import vn.datsan.datsan.search.SearchOption;
import vn.datsan.datsan.serverdata.CallBack;
import vn.datsan.datsan.ui.appviews.LoginPopup;
import vn.datsan.datsan.ui.appviews.NewFCPopup;
import vn.datsan.datsan.utils.AppLog;
import vn.datsan.datsan.search.ElasticsearchService;
import vn.datsan.datsan.search.interfaces.Searchable;

public class HomeActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener, FirebaseAuth.AuthStateListener {
    private static final String TAG = HomeActivity.class.getName();

    private TextView userName;
    private Button loginLogout;
    @BindView(R.id.searchResultView)
    View searchResultView;

    LoginPopup loginPopup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
//        ButterKnife.bind(this);
        FirebaseAuth.getInstance().addAuthStateListener(this);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Setting ViewPager for each Tabs
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        viewPager.setOffscreenPageLimit(3);
        // Set Tabs inside Toolbar
        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String name = CloudDataStorage.getInstance().genUniqFileName();
                //AppLog.log(AppLog.LogType.LOG_ERROR, TAG, name);
//                Intent intent = new Intent(HomeActivity.this, FieldSearchActivity.class);
//                startActivity(intent);
                NewFCPopup popup = new NewFCPopup(HomeActivity.this);
                popup.show();

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerview = navigationView.getHeaderView(0);

        loginLogout = (Button) headerview.findViewById(R.id.loginLogout);
        loginLogout.setOnClickListener(onLoginLogoutBtnClicked);
        userName = (TextView) headerview.findViewById(R.id.userName);
//
//        reloadView();


        loginPopup = new LoginPopup(HomeActivity.this);

        /*
         * Create elasticsearch index if it not exists
         */
        ElasticsearchService.getInstance().createIndex();

    }

    // Add Fragments to Tabs
    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment( SportFieldFragment.newInstance("",""), getString(R.string.sport_field));
        adapter.addFragment(FriendlyMatchFragment.newInstance("", ""), getString(R.string.friendly_match));
        adapter.addFragment(SportClubFragment.newInstance("", ""), getString(R.string.sport_club));
        viewPager.setAdapter(adapter);
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void reloadView() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null || user.isAnonymous()) {
            userName.setText("Annonymous");
            loginLogout.setText("Login");
        } else {
            userName.setText(user.getEmail());
            loginLogout.setText("Log out");
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_screen, menu);
        AppLog.log(AppLog.LogType.LOG_DEBUG, TAG, "onCreateOptionMenu");
        MenuItem itemSearch = menu.findItem(R.id.mapview_menu_search);

    final SearchView searchView = (SearchView) MenuItemCompat.getActionView(itemSearch);
    EditText searchEdit = ((EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text));
    searchEdit.setFocusable(true);
    searchEdit.setFocusableInTouchMode(true);
    searchEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            AppLog.log(AppLog.LogType.LOG_DEBUG, TAG, "OnFocusChanged");
            if (hasFocus) {
                searchResultView.setVisibility(View.VISIBLE);
            } else {
                searchResultView.setVisibility(View.GONE);
            }
        }
    });

    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            AppLog.log(AppLog.LogType.LOG_DEBUG, TAG, query);
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            AppLog.log(AppLog.LogType.LOG_DEBUG, TAG, newText);
            return false;
        }
    });

    searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            AppLog.log(AppLog.LogType.LOG_DEBUG, TAG, "OnQueryTextFocusChange");
        }
    });
    searchView.setOnSearchClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            searchResultView.setVisibility(View.VISIBLE);
        }
    });

    searchView.setOnCloseListener(new SearchView.OnCloseListener() {
        @Override
        public boolean onClose() {
            searchResultView.setVisibility(View.GONE);
            return false;
        }
    });

    searchView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            AppLog.log(AppLog.LogType.LOG_DEBUG, TAG, "OnFocusChanged");
            if (hasFocus) {
                searchResultView.setVisibility(View.VISIBLE);
            } else {
                searchResultView.setVisibility(View.GONE);
            }
        }
    });

        MenuItemCompat.setOnActionExpandListener(itemSearch,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionExpand(MenuItem menuItem) {
                        // Return true to allow the action view to expand
                        searchResultView.setVisibility(View.VISIBLE);
                        return true;
                    }

                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                        // When the action view is collapsed, reset the query
                        searchResultView.setVisibility(View.GONE);
                        // Return true to allow the action view to collapse
                        return true;
                    }
                });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (id == R.id.nav_profile) {
            if (user != null)
                startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public View.OnClickListener onLoginLogoutBtnClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user == null || user.isAnonymous()) {
                loginPopup.show();
            } else {
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
            }
        }
    };

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        AppLog.log(AppLog.LogType.LOG_ERROR, TAG, "AuthChanged");
        reloadView();
    }
}
