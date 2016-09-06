package vn.datsan.datsan.utils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by yennguyen on 10/06/2016.
 */
public class AppUtils {

    /**
     * Format the timestamp with SimpleDateFormat
     */
    public static final Locale LOCALE_VN = new Locale("vi", "VN");
    public static final DateTimeFormatter DAYMONTH_FORMAT = DateTimeFormat.forPattern("dd/MM");
    public static final String DATETIME_FORMAT = "dd/MM/yy HH:mm:ss";
    public static final String DATETIME_ddMMyy_FORMAT = "dd/MM/yy";
    public static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormat.forPattern(DATETIME_FORMAT);
    public static final DateTimeFormatter DATETIME_ddMMyy_FORMATTER = DateTimeFormat.forPattern(DATETIME_ddMMyy_FORMAT);
    public static final int VN_TIMEZONE_OFFSET_HOUR = 7;

    public static String getWeekDayAsText(DateTime dateTime) {
        return dateTime.dayOfWeek().getAsText(LOCALE_VN);
    }

    public static String getMonthDayAsText(DateTime dateTime) {
        return dateTime.dayOfMonth().getAsText(LOCALE_VN);
    }

    public static String getDateTimeAsString(long timestampMillis, DateTimeFormatter formatter) {
        int offset = AppUtils.VN_TIMEZONE_OFFSET_HOUR;
        DateTime dateTime = new DateTime(timestampMillis, DateTimeZone.forOffsetHours(offset));
        return formatter.print(dateTime);
    }

    public static DateTime getDateTime(long timestampMillis) {
        int offset = AppUtils.VN_TIMEZONE_OFFSET_HOUR;
        DateTime dateTime = new DateTime(timestampMillis, DateTimeZone.forOffsetHours(offset));
        return dateTime;
    }
}
