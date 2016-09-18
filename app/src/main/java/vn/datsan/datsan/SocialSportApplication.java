package vn.datsan.datsan;


import android.content.Context;
import android.support.multidex.MultiDex;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

import vn.datsan.datsan.search.ElasticsearchService;
import vn.datsan.datsan.utils.AppLog;
import vn.datsan.datsan.utils.MyActivityLifecycleHandler;

/**
 * Created by yennguyen on 10/06/2016.
 */
public class SocialSportApplication extends android.app.Application {

    private static final String TAG = SocialSportApplication.class.getName();

    @Override
    public void onCreate() {
        super.onCreate();

        AppLog.log(AppLog.LogType.LOG_DEBUG, TAG, "App starting up");

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        // If we need it, enable it
        //registerActivityLifecycleCallbacks(new MyActivityLifecycleHandler());

        // Enable offline capability
        if (!FirebaseApp.getApps(this).isEmpty()) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
