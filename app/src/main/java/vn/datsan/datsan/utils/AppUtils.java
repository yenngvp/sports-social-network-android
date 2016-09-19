package vn.datsan.datsan.utils;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Locale;

import vn.datsan.datsan.serverdata.ServerTimeService;

/**
 * Created by yennguyen on 10/06/2016.
 */
public class AppUtils {

    /**
     * Format the timestamp with SimpleDateFormat
     */
    public static final Locale LOCALE_VN = new Locale("vi", "VN");
    public static final Locale CURRENT_LOCALE = LOCALE_VN;
    public static final DateTimeFormatter DAYMONTH_FORMAT = DateTimeFormat.forPattern("dd/MM");
    public static final String DATETIME_FORMAT = "dd/MM/yy HH:mm:ss";
    public static final String DATETIME_ddMMyy_FORMAT = "dd/MM/yy";
    public static final String DATETIME_ddMMyyyy_FORMAT = "dd/MM/yyyyy";
    public static final String DATETIME_HHmm_FORMAT = "HH:mm";
    public static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormat.forPattern(DATETIME_FORMAT);
    public static final DateTimeFormatter DATETIME_ddMMyy_FORMATTER = DateTimeFormat.forPattern(DATETIME_ddMMyy_FORMAT);
    public static final DateTimeFormatter DATETIME_ddMMyyyy_FORMATTER = DateTimeFormat.forPattern(DATETIME_ddMMyyyy_FORMAT);
    public static final DateTimeFormatter DATETIME_HHmm_FORMATTER = DateTimeFormat.forPattern(DATETIME_HHmm_FORMAT);
    public static final int CURRENT_TIMEZONE_OFFSET_HOUR = 7;

    public static String getWeekDayAsText(DateTime dateTime) {
        return dateTime.dayOfWeek().getAsText(CURRENT_LOCALE);
    }

    public static String getMonthDayAsText(DateTime dateTime) {
        return dateTime.dayOfMonth().getAsText(CURRENT_LOCALE);
    }

    public static String getDateTimeAsString(long timestampMillis, DateTimeFormatter formatter) {
        int offset = AppUtils.CURRENT_TIMEZONE_OFFSET_HOUR;
        DateTime dateTime = new DateTime(timestampMillis, DateTimeZone.forOffsetHours(offset));
        return formatter.print(dateTime);
    }

    public static DateTime getDateTime(long timestampMillis) {
        int offset = AppUtils.CURRENT_TIMEZONE_OFFSET_HOUR;
        DateTime dateTime = new DateTime(timestampMillis, DateTimeZone.forOffsetHours(offset));
        return dateTime;
    }

    public static String getDateTimeForMessageSent(long timestampMillis) {
        DateTime todayAtMidnightServerTime = ServerTimeService.todayAtMidnightServerTime;
        if (todayAtMidnightServerTime == null) {
            // Use device time
            todayAtMidnightServerTime = LocalDate.now().toDateTimeAtStartOfDay();
        }

        int offset = AppUtils.CURRENT_TIMEZONE_OFFSET_HOUR;
        DateTime dateTime = new DateTime(timestampMillis, DateTimeZone.forOffsetHours(offset));
        LocalDate today = todayAtMidnightServerTime.toLocalDate();
        LocalDate tomorrow = today.plusDays(1);
        LocalDate yesterday = today.minusDays(1);
        DateTime now = DateTime.now();

        DateTime startOfToday = today.toDateTimeAtStartOfDay(now.getZone());
        DateTime startOfTomorrow = tomorrow.toDateTimeAtStartOfDay(now.getZone());
        DateTime startOfYesterday = yesterday.toDateTimeAtStartOfDay(now.getZone());

        if (dateTime.isAfter(startOfToday) && dateTime.isBefore(startOfTomorrow)) {
            return "Today";
        } else if (dateTime.isAfter(startOfYesterday) && dateTime.isBefore(startOfToday)) {
            return "Yesterday";
        } else {
            return DATETIME_ddMMyyyy_FORMATTER.print(dateTime);
        }
    }

    public static int DISPLAY_WIDTH;
    public static int DISPLAY_HEIGHT;

    public static void getDisplaySize(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        Point size = new Point();
        display.getSize(size);
        DISPLAY_WIDTH = size.x;
        DISPLAY_HEIGHT = size.y;
        AppLog.d("APP", String.format("DEVICE DISPLAY (w=%d, h=%d)", DISPLAY_WIDTH, DISPLAY_HEIGHT));
    }
}
