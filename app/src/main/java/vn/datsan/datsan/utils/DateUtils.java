package vn.datsan.datsan.utils;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.SimpleDateFormat;

/**
 * Created by yennguyen on 10/06/2016.
 */
public class DateUtils {

    /**
     * Format the timestamp with SimpleDateFormat
     */
    public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    public static final SimpleDateFormat FILENAME_DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
    public static final DateTimeFormatter DAYMONTH_FORMAT = DateTimeFormat.forPattern("dd/MM");

    public static String convertWeekDayToString(int day) {
        if (day == 7)
            return "Chủ nhật";
        return "Thứ " + day;
    }

}
