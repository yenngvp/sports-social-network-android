package vn.datsan.datsan.utils.localization;

import static junit.framework.Assert.*;

import org.joda.time.DateTimeConstants;
import org.joda.time.MutableDateTime;
import org.junit.Test;

import java.util.Locale;

/**
 * Created by yennguyen on 9/7/16.
 */
public class DateTimeWithLocaleTest {

    @Test
    public void testGetDayOfWeekAsText() {
        Locale locale = new Locale("vi", "VN");
        MutableDateTime start = new MutableDateTime(2012,1, 10,10,0,0,0 );
        // Thu 2
        start.setDayOfWeek(DateTimeConstants.MONDAY);
        String localDayText = start.dayOfWeek().getAsText(locale);
        String expected = "Thứ hai";
        assertEquals(expected, localDayText);

        // Thu 3
        start.setDayOfWeek(DateTimeConstants.TUESDAY);
        localDayText = start.dayOfWeek().getAsText(locale);
        expected = "Thứ ba";
        assertEquals(expected, localDayText);

        // Thu 4
        start.setDayOfWeek(DateTimeConstants.WEDNESDAY);
        localDayText = start.dayOfWeek().getAsText(locale);
        expected = "Thứ tư";
        assertEquals(expected, localDayText);

        // Thu 5
        start.setDayOfWeek(DateTimeConstants.THURSDAY);
        localDayText = start.dayOfWeek().getAsText(locale);
        expected = "Thứ năm";
        assertEquals(expected, localDayText);

        // Thu 6
        start.setDayOfWeek(DateTimeConstants.FRIDAY);
        localDayText = start.dayOfWeek().getAsText(locale);
        expected = "Thứ sáu";
        assertEquals(expected, localDayText);

        // Thu 7
        start.setDayOfWeek(DateTimeConstants.SATURDAY);
        localDayText = start.dayOfWeek().getAsText(locale);
        expected = "Thứ bảy";
        assertEquals(expected, localDayText);

        // CN
        start.setDayOfWeek(DateTimeConstants.SUNDAY);
        localDayText = start.dayOfWeek().getAsText(locale);
        expected = "Chủ nhật";
        assertEquals(expected, localDayText);

    }

    @Test
    public void testGetDayOfMonthAsText() {
        Locale locale = new Locale("vi", "VN");
        MutableDateTime start = new MutableDateTime(2012, 1, 10, 10, 0, 0, 0);

        start.setDayOfMonth(1);
        String localMonthText = start.dayOfMonth().getAsText(locale);
        String expected = "1";
        assertEquals(expected, localMonthText);
    }
}
