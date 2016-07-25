package vn.datsan.datsan.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import vn.datsan.datsan.R;
import vn.datsan.datsan.models.Field;
import vn.datsan.datsan.serverdata.CallBack;
import vn.datsan.datsan.serverdata.FieldDataManager;
import vn.datsan.datsan.serverdata.storage.CloudDataStorage;
import vn.datsan.datsan.ui.appviews.LoginPopup;
import vn.datsan.datsan.utils.AppLog;
import vn.datsan.datsan.utils.Constants;
import vn.datsan.datsan.utils.Elasticsearch;
import vn.datsan.datsan.utils.ElasticsearchEvent;
import vn.datsan.datsan.utils.ElasticsearchParam;
import vn.datsan.datsan.utils.ElasticsearchTask;

public class HomeActivity extends AppCompatActivity implements OnMapReadyCallback,
        NavigationView.OnNavigationItemSelectedListener, FirebaseAuth.AuthStateListener {
    private static final String TAG = HomeActivity.class.getName();

    private GoogleMap mMap;
    private TextView userName;
    private Button loginLogout;
    @BindView(R.id.searchResultView)
    View searchResultView;

    LoginPopup loginPopup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        ButterKnife.bind(this);
        FirebaseAuth.getInstance().addAuthStateListener(this);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //String name = CloudDataStorage.getInstance().genUniqFileName();
                //AppLog.log(AppLog.LogType.LOG_ERROR, TAG, name);
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

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        reloadView();


        loginPopup = new LoginPopup(HomeActivity.this);

        FieldDataManager.getInstance().getFields(new CallBack.OnResultReceivedListener() {
            @Override
            public void onResultReceived(Object result) {
                if (result != null) {
                    List<Field> fieldList = (List<Field>) result;
                    addMarkers(fieldList);
                }
            }
        });

        // Create elasticsearch index
        // Create index as object's key, such as: users, groups or fields
        ElasticsearchParam param = new ElasticsearchParam();
        param.setType(ElasticsearchEvent.ADD);
        param.setIndexName(Constants.ELASTICSEARCH_INDEX);
        new Elasticsearch().execute(param);

        ElasticsearchTask esTask = new ElasticsearchTask();
        esTask.execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void addMarkers(List<Field> fieldList) {
        for (Field field : fieldList) {
            String latlon = field.getLocation();
            if (latlon != null && latlon.length() > 6) {
                String arr[] = latlon.split(",");
                MarkerOptions marker = new MarkerOptions();
                marker.position(new LatLng(Double.parseDouble(arr[0]), Double.parseDouble(arr[1])));
                marker.title(field.getName());
                marker.snippet(field.getAddress());
                mMap.addMarker(marker);
            }
        }
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

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(10.777098, 106.695487);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
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
