package com.Digital_Bank.shared.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public final class DateTimeUtils {

    private DateTimeUtils() {

        }

        public static LocalDateTime now() {
            return LocalDateTime.now();
        }

        public static LocalDateTime startOfDay(LocalDate date) {

            return date.atStartOfDay();
        }

        public static LocalDateTime endOfDay(LocalDate date) {

            return date.atTime(LocalTime.MAX);
        }

        public static boolean isPast(LocalDate date) {

            return date.isBefore(LocalDate.now());
        }

        public static boolean isFuture(LocalDate date) {

            return date.isAfter(LocalDate.now());
        }

}
