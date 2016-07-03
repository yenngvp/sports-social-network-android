package vn.datsan.datsan;


import vn.datsan.datsan.utils.AppLog;
import vn.datsan.datsan.utils.Elasticsearch;

/**
 * Created by yennguyen on 10/06/2016.
 */
public class SocialSportApplication extends android.app.Application {

    private static final String TAG = SocialSportApplication.class.getName();

    @Override
    public void onCreate() {
        super.onCreate();

        AppLog.log(AppLog.LogType.LOG_DEBUG, TAG, "App starting up");

//        Elasticsearch elasticsearch = new Elasticsearch();
    }
}
