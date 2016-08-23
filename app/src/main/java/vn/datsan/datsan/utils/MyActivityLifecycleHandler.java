package vn.datsan.datsan.utils;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;


/**
 * Created by yennguyen on 8/23/16.
 */
public class MyActivityLifecycleHandler implements Application.ActivityLifecycleCallbacks {

    private static final String TAG = MyActivityLifecycleHandler.class.getName();

    private static int resumed;
    private static int paused;
    private static int started;
    private static int stopped;

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
    }

    @Override
    public void onActivityResumed(Activity activity) {
        ++resumed;
    }

    @Override
    public void onActivityPaused(Activity activity) {
        ++paused;
        AppLog.w(TAG, "The Application is in foreground: " + (resumed > paused));
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    @Override
    public void onActivityStarted(Activity activity) {
        ++started;
    }

    @Override
    public void onActivityStopped(Activity activity) {
        ++stopped;
        AppLog.w(TAG, "The Application is visible: " + (started > stopped));
    }

    public static boolean isApplicationVisible() {
        AppLog.w(TAG, "The Application is visible: " + (started > stopped));
        return started > stopped;
    }

    public static boolean isApplicationInForeground() {
        AppLog.w(TAG, "The Application is in foreground: " + (resumed > paused));
        return resumed > paused;
    }

}
