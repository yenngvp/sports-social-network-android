package vn.datsan.datsan.utils;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;

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
    public static final String FIREBASE_APP_ROOT = "/";
    public static final String FIREBASE_USERS = "app/users";
    public static final String FIREBASE_GROUPS = "app/groups";
    public static final String FIREBASE_FIELDS = "app/fields";
    public static final String FIREBASE_CHATS = "app/chats";
    public static final String FIREBASE_MESSAGES = "app/messages";
    public static final String FIREBASE_MEMBERS = "app/members";
    public static final String FIREBASE_MATCHS = "app/matchs";

    /*
     * Elasticsearch server
     */
    // Elasticsearch URL
    public static final String ELASTICSEARCH_SERVER_URL = BuildConfig.ELASTICSEARCH_SERVER_URL;
    // Elasticsearch default credentials (api_key, api_value)
    public static final String ELASTICSEARCH_USERNAME = BuildConfig.ELASTICSEARCH_USERNAME;
    public static final String ELASTICSEARCH_PASSWORD = BuildConfig.ELASTICSEARCH_PASSWORD;
    public static final String ELASTICSEARCH_INDEX = "socialsport";

    public static final int GOOGLE_SIGN_IN_CODE = 9001;

    public static final int ELASTICSEARCH_PAGINATION_SIZE_DEFAULT = 100;
    public static final int CHAT_HISTORY_PAGINATION_SIZE_DEFAULT = 100;

    public static final String DATETIME_FORMAT = "dd/MM/yy HH:mm:ss";
    public static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormat.forPattern(DATETIME_FORMAT);

    public static final String GROUP_NAME_SEPARATOR = ", ";
}
