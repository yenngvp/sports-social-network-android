package vn.datsan.datsan;


/**
 * Created by yennguyen on 10/06/2016.
 */
public class SocialSportApplication extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        /* Initialize Firebase */
        //Firebase.setAndroidContext(this);
        /* Enable disk persistence  */
        //Firebase.getDefaultConfig().setPersistenceEnabled(true);
    }
}
