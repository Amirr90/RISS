package com.example.riss.AppUtils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddMonthsToCurrentDate {
    private static final String TAG = "AddMonthsToCurrentDate";



    public String getDateAfterMonth(String month) {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.MONTH, Integer.parseInt(month));
        String date = getCurrentDateInWeekMonthDayFormat((now.get(Calendar.MONTH) + 1) + "-" + now.get(Calendar.DATE) + "-" + now.get(Calendar.YEAR));
        Log.d(TAG, "getDateAfterMonth: " + date);
        return date;
    }

    public String getCurrentDate() {
        Calendar now = Calendar.getInstance();
        String date = getCurrentDateInWeekMonthDayFormat((now.get(Calendar.MONTH) + 1) + "-" + now.get(Calendar.DATE) + "-" + now.get(Calendar.YEAR));
        Log.d(TAG, "getCurrentDate: " + date);
        return date;
    }

    public String getDateBeforeMonth(String months) {
        Calendar now = Calendar.getInstance();
        now = Calendar.getInstance();
        now.add(Calendar.MONTH, -(Integer.parseInt(months)));
        String date = getCurrentDateInWeekMonthDayFormat((now.get(Calendar.MONTH) + 1) + "-" + now.get(Calendar.DATE) + "-" + now.get(Calendar.YEAR));
        Log.d(TAG, "getDateBeforeMonth: " + date);
        return date;
    }

    public static String getCurrentDateInWeekMonthDayFormat(String oldDate) {

        String inputPattern = "MM-dd-yyyy";
        String outputPattern = "EEE, MMM d, yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(oldDate);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.d("TAG", "parseDateToFormatNew:Date In pattern : " + oldDate);
        Log.d("TAG", "parseDateToFormatNew:Date Out pattern: " + str);
        return str;

    }
}
