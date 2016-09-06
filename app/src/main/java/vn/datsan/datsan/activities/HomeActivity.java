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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import vn.datsan.datsan.R;
import vn.datsan.datsan.fragments.SportClubFragment;
import vn.datsan.datsan.fragments.FriendlyMatchFragment;
import vn.datsan.datsan.fragments.SportFieldFragment;
import vn.datsan.datsan.models.User;
import vn.datsan.datsan.search.AppSearch;
import vn.datsan.datsan.serverdata.CallBack;
import vn.datsan.datsan.serverdata.UserManager;
import vn.datsan.datsan.ui.appviews.LoginPopup;
import vn.datsan.datsan.ui.appviews.MaterialSearchView;
import vn.datsan.datsan.ui.customwidgets.SimpleProgress;
import vn.datsan.datsan.utils.AppLog;

public class HomeActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener, FirebaseAuth.AuthStateListener {
    private static final String TAG = HomeActivity.class.getName();

    private TextView userName;
    private Button loginLogout;
    LoginPopup loginPopup;
    MaterialSearchView searchView;
    TabLayout tabs;
    ViewPager viewPager;
    SportFieldFragment sportFieldFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

//        ButterKnife.bind(this);
        FirebaseAuth.getInstance().addAuthStateListener(this);

        FacebookSdk.sdkInitialize(this.getApplicationContext());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Setting ViewPager for each Tabs
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        viewPager.setOffscreenPageLimit(3);
        // Set Tabs inside Toolbar
        tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        FloatingActionButton chatFab = (FloatingActionButton) findViewById(R.id.chatFab);
        chatFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser == null || currentUser.isAnonymous()) {
                    loginPopup.show();
                } else {
                    // Show chat history
                    Intent intent = new Intent(HomeActivity.this, ChatRecentActivity.class);
                    startActivity(intent);
                }
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

        loginPopup = new LoginPopup(HomeActivity.this);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                MenuItemCompat.collapseActionView(appBarMenu.findItem(R.id.action_search));
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void navigateSearch(String text) {
        switch (viewPager.getCurrentItem()) {
            case 0:
                searchLocation(text);
                break;
            case 1:
                break;
            case 2:
                break;
        }
    }

    // Add Fragments to Tabs
    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        sportFieldFragment = SportFieldFragment.newInstance("", "");
        adapter.addFragment(sportFieldFragment, getString(R.string.sport_field));
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

     Menu appBarMenu;

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        AppLog.log(AppLog.LogType.LOG_ERROR, "TAG", "create menu");
        getMenuInflater().inflate(R.menu.main_screen, menu);
        appBarMenu = menu;

        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
//        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleSearchItemClicked(searchView);
            }
        });

        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                handleSearchViewQuerySubmite(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        AppLog.log(AppLog.LogType.LOG_ERROR, "Tag", "Click");
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_search) {
            if (viewPager.getCurrentItem() == 0) {
//                startActivity(new Intent(getBaseContext(), FieldSearchActivity.class));
//                return false;
            } else {
                //searchView.showSearch();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void handleSearchItemClicked(SearchView searchView) {
        switch (viewPager.getCurrentItem()){
            case 0:
                searchView.setQueryHint(getString(R.string.search_field_hint));
                break;
            case 1:
                searchView.setQueryHint(getString(R.string.search_match_hint));
                break;
            case 2:
                searchView.setQueryHint(getString(R.string.search_club_hint));
                break;
        }
    }

    private void handleSearchViewQuerySubmite(String text) {
        searchLocation(text);
    }

    DrawerLayout drawer;
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

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public View.OnClickListener onLoginLogoutBtnClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user == null || user.isAnonymous()) {
                loginPopup.show();
                try {
                    drawer.closeDrawer(GravityCompat.START);
                }catch (Exception e) {

                }
            } else {
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
            }
        }
    };

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        AppLog.log(AppLog.LogType.LOG_ERROR, TAG, "AuthChanged");
        if (firebaseAuth.getCurrentUser() != null) {
            UserManager.getInstance().getCurrentUserInfo(new CallBack.OnResultReceivedListener() {
                @Override
                public void onResultReceived(Object result) {
                    UserManager.getInstance().setCurrentUser((User) result);

                    // TODO: Wait until here before "Dismiss spinner"?
                }
            });
        } else {
            UserManager.getInstance().setCurrentUser(null);
        }

        reloadView();
    }

    private void searchLocation(String searchText) {
        final SimpleProgress progress = new SimpleProgress(this, "Đang tìm...");
        progress.show();
        AppSearch.searchField(searchText, "40km", 1, 1, new CallBack.OnResultReceivedListener() {
            @Override
            public void onResultReceived(Object result) {
                sportFieldFragment.showSearchResult(result);
                progress.dismiss();
            }
        });
    }
}
