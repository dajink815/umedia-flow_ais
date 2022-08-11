/*
 * Copyright (C) 2018. Uangel Corp. All rights reserved.
 *
 */

package com.uangel.ais.util;

import com.google.common.net.InetAddresses;

/**
 * String Util
 *
 * @file StringUtil.java
 * @author Tony Lim
 */
public class StringUtil {
    private static final String STR_OK = "OK";
    private static final String STR_FAIL = "FAIL";
    private static final String TRUE = "TRUE";
    private static final String TEL = "TEL";
    private static final String ON = "ON";
    private static final String AS = "AS";

    private StringUtil() {
        // Do Nothing
    }

    public static String getOkFail(boolean result) {
        return (result ? STR_OK : STR_FAIL);
    }

    public static boolean checkTrue(String str) {
        return TRUE.equalsIgnoreCase(str);
    }

    public static boolean checkTel(String str) {
        return TEL.equalsIgnoreCase(str);
    }

    public static boolean checkOn(String str) {
        return ON.equalsIgnoreCase(str);
    }

    public static boolean checkAS(String str) {
        return AS.equalsIgnoreCase(str);
    }

    public static String blankIfNull(String str) {
        return str == null ? "" : str;
    }

    public static String removeLine(String str) {
        return str.replaceAll("(\r\n|\r|\n|\n\r)", "").trim();
    }

    public static String removeUnderBar(String str) {
        return str.replace("_", "");
    }

    public static boolean isNull(String str) {
        return str == null || str.isEmpty();
    }

    public static boolean isNumeric(String strNum) {
        if (isNull(strNum)) return false;
        try {
            Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static boolean validateIPAddress(String ipAddress) {
        if (InetAddresses.isInetAddress(ipAddress)) {
            //log.debug("The IP address " + ipAddress + " is valid");
            return true;
        } else {
            //log.debug("The IP address " + ipAddress + " isn't valid");
        }
        return false;
    }

}
