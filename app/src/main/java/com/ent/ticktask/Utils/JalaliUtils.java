package com.ent.ticktask.Utils;

import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.icu.util.ULocale;

import java.util.Date;

public final class JalaliUtils {

    public static final String[] MONTHS = {
            "فروردین", "اردیبهشت", "خرداد", "تیر", "مرداد", "شهریور",
            "مهر", "آبان", "آذر", "دی", "بهمن", "اسفند"};

    // The calendar keyword selects the Persian calendar
    private static final ULocale FA = new ULocale("fa_IR@calendar=persian");

    private JalaliUtils() {
    }

    private static Calendar newCalendar() {
        return Calendar.getInstance(FA);
    }

    public static String fa(int number) {
        StringBuilder builder = new StringBuilder();
        for (char c : String.valueOf(number).toCharArray()) {
            builder.append((char) ('۰' + (c - '0')));
        }
        return builder.toString();
    }

    public static String fa2(int number) {
        return number < 10 ? "۰" + fa(number) : fa(number);
    }

    public static int currentYear() {
        return newCalendar().get(Calendar.YEAR);
    }

    // month is zero-based (0..11)
    public static int daysInMonth(int year, int month) {
        Calendar calendar = newCalendar();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    // month is zero-based (0..11)
    public static long toMillis(int year, int month, int day, int hour, int minute) {
        Calendar calendar = newCalendar();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    // Returns {year, month(0..11), day, hour, minute} in the Persian calendar
    public static int[] fromMillis(long millis) {
        Calendar calendar = newCalendar();
        calendar.setTimeInMillis(millis);
        return new int[]{
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE)};
    }

    public static String formatDate(long millis) {
        return new SimpleDateFormat("d MMMM yyyy", FA).format(new Date(millis));
    }

    public static String formatTime(long millis) {
        return new SimpleDateFormat("HH:mm", FA).format(new Date(millis));
    }
}