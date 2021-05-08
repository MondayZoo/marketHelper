package com.month.markethelper.utils;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FormatUtils {

    public static String dateFormat(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd", Locale.CHINA);
        return simpleDateFormat.format(date);
    }

    public static String percentFormat(int a, int b) {
        if (b == 0) return null;
        DecimalFormat df = new DecimalFormat("0");
        return df.format((float) a / b * 100) + "%";
    }

    public static String oneDecimalFormat(float a, float b) {
        if (b == 0) return null;
        DecimalFormat df = new DecimalFormat("0.0");
        return df.format(a / b);
    }
}
