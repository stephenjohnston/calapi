package org.sjj.calendar;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.time.temporal.TemporalAdjusters.dayOfWeekInMonth;
import static java.time.temporal.TemporalAdjusters.previous;
/**
 * Created by stephen on 7/16/15.
 */


// ideas like hard-start, hard-stop, and offset could be applied to any of these base calendars:
// { "id": "", "displayName": "", "rule": "month-day", "day": 1, "month": 12 }
// { "id": "", "displayName": "", "rule": "nth-dow-of-month", "n": 1, "month": 12 }
// { "id": "", "displayName": "", "rule": "nth-to-last-dow-of-month", "n": 1, "month": 5 }
// { "id": "", "displayName": "", "rule": "list-of-lists", "lists": ["xmas", "thanksgiving", "independence" ]

/* TODO
 *   - exception for new years?
 *   - wrapper functions?
 *   - list of list?
 *   - hard-coded list from persistent storage
 */
public class Calendars {
    /**
     * getCalForMonthDay - returns an infinite stream of dates for the specified month and day of every year, starting
     * from the start date.
     *
     * @param start - the beginning date for this calendar
     * @param month - An integer representing the month (1 thru 12)
     * @param day - an integer representing the day of the month ( 1 thru 31)
     */
    public static Stream<LocalDate> getCalForMonthDay(LocalDate start, Month month, int day) {
        LocalDate actual_start = start.withMonth(month.getValue()).withDayOfMonth(day);
        return Stream
                .iterate(actual_start, ld -> ld.plus(1, ChronoUnit.YEARS))
                .filter(ld -> (start.isEqual(ld) || start.isBefore(ld)));
    }

    public static Stream<LocalDate> getCalForMonthDayWithOffset(LocalDate start, Month month, int day,
                                                                Function<LocalDate,LocalDate> fn) {
        return getCalForMonthDay(start, month, day)
                .map(fn);
    }

    public static Stream<LocalDate> getCalForNthDowForMonth(LocalDate start, int n, Month m, DayOfWeek dow) {
        return getCalForMonthDay(start, m, 1)
                .map(dt -> dt.with(dayOfWeekInMonth(n, dow)));
    }

    public static Stream<LocalDate> getCalForNthToLastDowForMonth(LocalDate start, int n, Month m, DayOfWeek dow) {
        return getCalForMonthDay(start, m, 1)
                .map(dt -> dt.with(dayOfWeekInMonth(-n, dow)));
    }

    public static Stream<LocalDate> getCalForEaster(LocalDate start) {
        return getCalForMonthDay(start, Month.JANUARY, 1)
                .map(ld -> CalendarUtil.getEasterForYear(ld.getYear()));
    }

    public static Stream<LocalDate> getCalForGoodFriday(LocalDate start) {
        return getCalForEaster(start)
                .map(ld -> ld.with(previous(DayOfWeek.FRIDAY)));
    }

    /* Example usage */
    public static void main(String[] args) {
        LocalDate start_date = LocalDate.of(2015, 12, 1);
        Stream<LocalDate> dates = getCalForNthToLastDowForMonth(start_date, 1, Month.MAY, DayOfWeek.MONDAY);
        dates.limit(10).forEach(System.out::println);
    }

}
