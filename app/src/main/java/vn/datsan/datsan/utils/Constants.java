package vn.datsan.datsan.utils;

import vn.datsan.datsan.BuildConfig;

/**
 * Created by yennguyen on 10/06/2016.
 */
public class Constants {

    /**
     * Constants for Firebase URL
     */
    public static final String FIREBASE_URL = BuildConfig.UNIQUE_FIREBASE_ROOT_URL;

    /*
     * Firebase objects
     */
    public static final String FIREBASE_USERS = "users";
    public static final String FIREBASE_GROUPS = "groups";
    public static final String FIREBASE_FIELDS = "fields";

    // Elasticsearch URL
    public static final String ELASTICSEARCH_SERVER_URL = BuildConfig.ELASTICSEARCH_SERVER_URL;
    public static final String APP_INDEX = "app";


}
