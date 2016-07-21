package vn.datsan.datsan;


import android.content.Context;
import android.support.multidex.MultiDex;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import vn.datsan.datsan.utils.AppLog;

/**
 * Created by yennguyen on 10/06/2016.
 */
public class SocialSportApplication extends android.app.Application {

    private static final String TAG = SocialSportApplication.class.getName();

    @Override
    public void onCreate() {
        super.onCreate();

        AppLog.log(AppLog.LogType.LOG_ERROR, TAG, "App starting up");
//        Elasticsearch elasticsearch = new Elasticsearch();

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
