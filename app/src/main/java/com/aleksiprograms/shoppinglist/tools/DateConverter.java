package com.aleksiprograms.shoppinglist.tools;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class DateConverter {

    static SimpleDateFormat formatter;

    private DateConverter() {
    }

    public static String millisToDate(long millis) {
        if (Locale.getDefault().getLanguage().equals("fi")) {
            formatter = new SimpleDateFormat(
                    "EEEE, dd. MMMM yyyy",
                    new Locale("fi", "FI"));
        } else {
            formatter = new SimpleDateFormat(
                    "EEEE, dd MMMM yyyy",
                    new Locale("en", "EN"));
        }
        String date = formatter.format(millis);
        date = date.substring(0, 1).toUpperCase() + date.substring(1);
        return date;
    }
}