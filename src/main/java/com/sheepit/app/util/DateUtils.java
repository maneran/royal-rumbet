package com.sheepit.app.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtils {

    public static String FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    public static Date safeParse(String dateString) {
        if (dateString == null) {
            return null;
        }
        DateFormat formatter = buildDateFormatter();
        try {
            return formatter.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static LocalDateTime safeParseToLocalDateTime(String dateString) {
        if (dateString == null) return null;

        try {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(FORMAT).withZone(ZoneOffset.UTC);
            return LocalDateTime.parse(dateString, dateTimeFormatter);
        } catch (DateTimeParseException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static DateFormat buildDateFormatter() {
        DateFormat formatter = new SimpleDateFormat(FORMAT);
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        return formatter;
    }

    public static String getDateAsString(Instant date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMAT).withLocale(Locale.getDefault()).withZone(ZoneId.systemDefault());
        return formatter.format(date).replace("T", " ");
    }
}
