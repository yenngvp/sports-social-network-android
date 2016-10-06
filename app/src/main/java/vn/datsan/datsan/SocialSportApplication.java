package vn.datsan.datsan;


import android.content.Context;
import android.provider.SyncStateContract;
import android.support.multidex.MultiDex;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import vn.datsan.datsan.models.ServerTime;
import vn.datsan.datsan.search.ElasticsearchService;
import vn.datsan.datsan.serverdata.CallBack;
import vn.datsan.datsan.serverdata.ServerTimeService;
import vn.datsan.datsan.utils.AppConstants;
import vn.datsan.datsan.utils.AppLog;
import vn.datsan.datsan.utils.AppUtils;
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

        if (!FirebaseApp.getApps(this).isEmpty()) {
            // Enable offline capability
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);

            // Get server time
            final String timeServerKey = AppConstants.TODAY_SERVER_TIME;
            ServerTimeService.getInstance().getServerTime(timeServerKey, new CallBack.OnResultReceivedListener() {
                @Override
                public void onResultReceived(Object result) {
                    ServerTime serverTime = (ServerTime) result;
                    if (result == null) return;

                    // Today at device timezone
                    DateTime dateTime = new DateTime(serverTime.getTodayAtMidnightMillis(),
                            DateTimeZone.forOffsetHours(AppUtils.CURRENT_TIMEZONE_OFFSET_HOUR));
                    ServerTimeService.todayAtMidnightServerTime = dateTime;
                    AppLog.i(timeServerKey, dateTime.toString());
                }
            });
        }

        AppUtils.getDisplaySize(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
