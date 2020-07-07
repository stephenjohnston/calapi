package org.sjj.calendar;

import org.junit.Assert;
import org.junit.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import com.codepoetics.protonpack.StreamUtils;

/**
 * Created by stephen on 7/16/15.
 */
public class CalendarsTest {
    private void testResults(String[] expected_dates, Stream<LocalDate> result) {
        List<String> expected = Arrays.asList(expected_dates);
        List<String> actual = result
                .map(Object::toString)
                .limit(expected_dates.length)
                .collect(Collectors.toList());
        Assert.assertEquals(expected, actual);
    }

    // Test a Christmas holiday calendar
    @Test
    public void testCalendarForMonthDay() {
        String[] expected = { "2000-12-25", "2001-12-25", "2002-12-25", "2003-12-25", "2004-12-25", "2005-12-25" };
        String start_date = "2000-12-25";
        Stream<LocalDate> result = Calendars.getCalForMonthDay(LocalDate.parse(start_date), Month.DECEMBER, 25);
        testResults(expected, result);
    }

    // Test a Christmas holiday calendar where weekends are rolled to the nearest weekday.
    @Test
    public void testCalendarForMonthDayWithOffset() {
        String[] expected = { "2003-12-25", "2004-12-24", "2005-12-26", "2006-12-25" };
        String start_date = "2003-12-25";
        Stream<LocalDate> actual = Calendars.getCalForMonthDayWithOffset(LocalDate.parse(start_date),
                Month.DECEMBER, 25, CalendarUtil::nearestWeekDayOffset);
        testResults(expected, actual);
    }

    @Test
    public void testGetCalForNthDowForMonth() {
        String[] expected = { "2014-01-20", "2015-01-19", "2016-01-18", "2017-01-16" };
        String start_date = "2014-01-01";
        Stream<LocalDate> actual = Calendars.getCalForNthDowForMonth(LocalDate.parse(start_date),
                3, Month.JANUARY, DayOfWeek.MONDAY);
        testResults(expected, actual);
    }

    @Test
    public void testGetCalForNthToLastDowForMonth() {
        String[] expected = { "2014-05-26", "2015-05-25", "2016-05-30", "2017-05-29" };
        String start_date = "2014-01-01";
        Stream<LocalDate> actual = Calendars.getCalForNthToLastDowForMonth(LocalDate.parse(start_date),
                1, Month.MAY, DayOfWeek.MONDAY);
        testResults(expected, actual);
    }

    @Test
    public void testMergeSortedStreams() {
        List<Integer> expected = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        List<Integer> i1 = Arrays.asList(1, 3, 5, 7, 9);
        List<Integer> i2 = Arrays.asList(2, 4, 6, 8, 10);
        List<Integer> actual = CalendarUtil.mergeSortedStreams(i1.stream(), i2.stream(),
               (v1, v2) -> v1.compareTo(v2)).collect(Collectors.toList());
        actual.stream().forEach(System.out::println);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testMergeSortedStreamsWithDates() {
        Stream<LocalDate> s1 = Calendars.getCalForMonthDay(LocalDate.of(2000, 12, 25), Month.DECEMBER, 25);
        Stream<LocalDate> s2 = Calendars.getCalForMonthDay(LocalDate.of(2000, 1, 1), Month.JULY, 4);

        Stream<LocalDate> merged = CalendarUtil.mergeSortedStreams(s1, s2,
                (v1, v2) -> v1.compareTo(v2)).limit(50);
        merged.forEach(System.out::println);
    }

    @Test
    public void testGetEasterDates() {
        String[] expected = { "2015-04-05", "2016-03-27", "2017-04-16" };
        String start_date = "2014-04-21";
        Stream<LocalDate> actual = Calendars.getCalForEaster(LocalDate.parse(start_date));
        testResults(expected, actual);
    }
    @Test
    public void testGetGoodFridayDates() {
        String[] expected = { "2015-04-03", "2016-03-25", "2017-04-14", "2018-03-30" };
        String start_date = "2015-01-01";
        Stream<LocalDate> actual = Calendars.getCalForGoodFriday(LocalDate.parse(start_date));
        testResults(expected, actual);
    }
    @Test
    public void testTakeWhile() {
	String[] expected_dates = { "2015-04-03", "2016-03-25", "2017-04-14", "2018-03-30" };
        String start_date = "2015-01-01";
        LocalDate end_date = LocalDate.parse("2018-12-01");
        Stream<LocalDate> infiniteStream = Calendars.getCalForGoodFriday(LocalDate.parse(start_date));
	
	// Keep taking elements from the stream while the date is less than 2018-12-01
	Stream<LocalDate> finiteStream = StreamUtils.takeWhile(infiniteStream, d -> d.isBefore(end_date));

        List<String> expected = Arrays.asList(expected_dates);
        List<String> actual = finiteStream.map(Object::toString).collect(Collectors.toList());
        Assert.assertEquals(expected, actual);
    }
}
