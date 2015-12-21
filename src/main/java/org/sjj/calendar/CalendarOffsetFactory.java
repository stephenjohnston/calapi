package org.sjj.calendar;

import java.time.LocalDate;
import java.util.EnumMap;
import java.util.function.Function;

/**
 * Created by stephen on 7/22/15.
 */
public class CalendarOffsetFactory {
    private static final EnumMap<CalendarOffsetType, Function<LocalDate, LocalDate>> map = new EnumMap<>(CalendarOffsetType.class);

    static {
        CalendarOffsetFactory.map.put(CalendarOffsetType.NEAREST_WEEKDAY, CalendarUtil::nearestWeekDayOffset);
        CalendarOffsetFactory.map.put(CalendarOffsetType.SUNDAY_TO_MONDAY, CalendarUtil::sundayToMondayOffset);
    }

    public static Function<LocalDate, LocalDate> getOffsetFnForOffsetType(CalendarOffsetType type) {
        Function<LocalDate, LocalDate> ret_val = CalendarOffsetFactory.map.get(type);
        if (ret_val == null) {
            throw new IllegalArgumentException("The enumMap doesn't contain a fn for type " + type);
        }
        return ret_val;
    }
}
