package com.uangel.ais.util;

import org.apache.commons.lang3.time.FastDateFormat;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author dajin kim
 */
public class DateFormatUtil {
    private static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    private static final String YYYY_MM_DD_HH_MM_SS_SSS = "yyyy-MM-dd HH:mm:ss.SSS";
    private static final String YYYY_MM_DD = "yyyyMMdd";
    private static final String HH_MM_SS = "HHmmss";
    private static final String HH_MM = "HHmm";

    private DateFormatUtil() {
        // Do Nothing
    }

    public static String formatYmdHms(long longDate) {
        return format(YYYY_MM_DD_HH_MM_SS, longDate);
    }

    public static String formatYmdHmsS(long longDate) {
        return format(YYYY_MM_DD_HH_MM_SS_SSS, longDate);
    }

    public static String formatYmd(long longDate) {
        return format(YYYY_MM_DD, longDate);
    }

    public static String formatHms(long longDate) {
        return format(HH_MM_SS, longDate);
    }

    public static String formatHm(long longDate) {
        return format(HH_MM, longDate);
    }

    private static String format(String dateFormat, long longDate) {
        SimpleDateFormat format = new SimpleDateFormat(dateFormat);
        return format.format(longDate);
    }

    public static String currentTimeStamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(YYYY_MM_DD_HH_MM_SS_SSS));
    }

    public static String fastFormatYmdHmsS(Date date) {
        FastDateFormat format = FastDateFormat.getInstance(YYYY_MM_DD_HH_MM_SS_SSS);
        return format.format(date);
    }

}
