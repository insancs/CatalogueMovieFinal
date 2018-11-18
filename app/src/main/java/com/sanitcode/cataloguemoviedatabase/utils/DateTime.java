package com.sanitcode.cataloguemoviedatabase.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTime {
    private final static String DATE_FORMAT_DAY = "EEEE, MMM d, yyyy";

    private static String format(String date, String format) {
        String result = "";

        java.text.DateFormat old = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date oldDate = old.parse(date);
            java.text.DateFormat newFormat = new SimpleDateFormat("EEEE, MMM dd, yyyy");
            result = newFormat.format(oldDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static String getDateDay(String date) {
        return format(date, DATE_FORMAT_DAY);
    }
}

