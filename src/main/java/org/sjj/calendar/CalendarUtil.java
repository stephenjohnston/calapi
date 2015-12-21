package org.sjj.calendar;

import com.google.common.collect.Iterators;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Created by stephen on 7/16/15.
 */
public class CalendarUtil {
    /********************
     * Offset functions
     ********************/
    public static LocalDate nearestWeekDayOffset(LocalDate d) {
        DayOfWeek day_of_week = d.getDayOfWeek();
        if (day_of_week == DayOfWeek.SATURDAY) {
            d = d.minus(1, ChronoUnit.DAYS);
        } else if (day_of_week == DayOfWeek.SUNDAY) {
            d = d.plus(1, ChronoUnit.DAYS);
        }
        return d;
    }

    public static LocalDate sundayToMondayOffset(LocalDate d) {
        if (d.getDayOfWeek() == DayOfWeek.SUNDAY) {
            d = d.plus(1, ChronoUnit.DAYS);
        }
        return d;
    }

    /********************
     * Easter
     ********************/
    /*
     *	Reference: Donald Knuth, The Art of Computer Programming,
     *	Volume 1: Fundamental Algorithms (2nd Ed., 1973), p155-6.
     */
    public static LocalDate getEasterForYear(int year) throws IllegalArgumentException {
        if ( year <= 1582 ) throw new IllegalArgumentException("Easter on the gregorian calendar only goes back to 1582");
        int golden = (year % 19) + 1;
        int century = (year / 100) + 1;
        int	x = ( 3 * century / 4 ) - 12;
        int	z = ( (8 * century + 5) / 25 ) - 5;
        int	day = ( 5 * year / 4 ) - x - 10;
        int	epact = ( 11 * golden + 20 + z - x ) % 30;
        if ( (( epact == 25 )&&( golden > 11 ))||( epact == 24 ) ) ++epact;
        int	moon = 44 - epact;
        if ( moon < 21 ) moon += 30;
        int	n = ( moon + 7 ) - ( ( day + moon ) % 7 );

        if ( n > 31 )
            return LocalDate.of(year, 4, n-31);
        else
            return LocalDate.of(year, 3, n);
    }


    /**************************
     * Merge helper functions
     **************************/
    public static <T> Stream<T> mergeSortedStreams(Stream<T> s1, Stream<T> s2, Comparator<T> c) {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(
                        Iterators.mergeSorted(
                                Arrays.asList(s1.iterator(), s2.iterator()),
                                c),
                        Spliterator.ORDERED),
                false /* not parallel */);
    }

}
