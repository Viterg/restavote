package ru.viterg.restavote.util;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class DateTimeUtil {
    public static final String            DATE_TIME_PATTERN   = "yyyy-MM-dd HH:mm";
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);

    public static final  LocalTime     LAST_VOTE_TIME = LocalTime.of(11, 0);
    // DB doesn't support LocalDate.MIN/MAX
    private static final LocalDateTime MIN_DATE       = LocalDateTime.of(1, 1, 1, 0, 0);
    private static final LocalDateTime MAX_DATE       = LocalDateTime.of(3000, 1, 1, 0, 0);


    private DateTimeUtil() {
    }

    public static boolean isTimeAvailableForVote(@NonNull LocalTime voteTime) {
        return voteTime.isBefore(LAST_VOTE_TIME);
    }

    public static LocalDateTime atStartOfDayOrMin(LocalDate localDate) {
        return localDate != null ? localDate.atStartOfDay() : MIN_DATE;
    }

    public static LocalDateTime atStartOfNextDayOrMax(LocalDate localDate) {
        return localDate != null ? localDate.plus(1, ChronoUnit.DAYS).atStartOfDay() : MAX_DATE;
    }

    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }

    @Nullable
    public static LocalDate parseLocalDate(@Nullable String str) {
        return StringUtils.hasText(str) ? LocalDate.parse(str) : null;
    }

    @Nullable
    public static LocalTime parseLocalTime(@Nullable String str) {
        return StringUtils.hasText(str) ? LocalTime.parse(str) : null;
    }
}
