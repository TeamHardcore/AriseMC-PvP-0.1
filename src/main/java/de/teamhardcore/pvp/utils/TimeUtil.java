/*
 *
 * @author TeamHardcore
 *
 * Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.utils;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeUtil {

    public static long parseTime(String timeformat) {
        Pattern pattern = Pattern.compile("(?:([0-9]+)\\s*y[a-z]*[,\\s]*)?(?:([0-9]+)\\s*mo[a-z]*[,\\s]*)?(?:([0-9]+)\\s*w[a-z]*[,\\s]*)?(?:([0-9]+)\\s*d[a-z]*[,\\s]*)?(?:([0-9]+)\\s*h[a-z]*[,\\s]*)?(?:([0-9]+)\\s*m[a-z]*[,\\s]*)?(?:([0-9]+)\\s*(?:s[a-z]*)?)?", 2);
        Matcher matcher = pattern.matcher(timeformat);
        long years = 0L;
        long month = 0L;
        long weeks = 0L;
        long days = 0L;
        long hours = 0L;
        long minutes = 0L;
        long seconds = 0L;
        boolean found = false;

        while (matcher.find()) {
            if (matcher.group() == null || matcher.group().isEmpty()) continue;
            for (int i = 0; i < matcher.groupCount(); ++i) {
                if (matcher.group(i) == null || matcher.group(i).isEmpty()) continue;
                found = true;
                break;
            }
            if (!found) continue;
            if (matcher.group(1) != null && !matcher.group(1).isEmpty())
                years = Long.parseLong(matcher.group(1));

            if (matcher.group(2) != null && !matcher.group(2).isEmpty())
                month = Long.parseLong(matcher.group(2));

            if (matcher.group(3) != null && !matcher.group(3).isEmpty())
                weeks = Long.parseLong(matcher.group(3));

            if (matcher.group(4) != null && !matcher.group(4).isEmpty())
                days = Long.parseLong(matcher.group(4));

            if (matcher.group(5) != null && !matcher.group(5).isEmpty())
                hours = Long.parseLong(matcher.group(5));

            if (matcher.group(6) != null && !matcher.group(6).isEmpty())
                minutes = Long.parseLong(matcher.group(6));

            if (matcher.group(7) == null || matcher.group(7).isEmpty()) continue;
            seconds = Long.parseLong(matcher.group(7));
        }
        if (!found) {
            return -1L;
        }
        long millis = 0L;
        if (years > 0L)
            millis += years * 224985600000L;

        if (month > 0L)
            millis += month * 18748800000L;

        if (weeks > 0L)
            millis += weeks * 604800000L;

        if (days > 0L)
            millis += days * 86400000L;

        if (hours > 0L)
            millis += hours * 3600000L;

        if (minutes > 0L)
            millis += minutes * 60000L;

        if (seconds > 0L)
            millis += seconds * 1000L;

        return millis;
    }


    public static String timeToString(long time) {
        String msg = "";
        long seconds = time / 1000L;

        if (seconds >= 86400L) {
            long days = seconds / 86400L;
            msg = msg + days + (days == 1L ? " Tag " : " Tage ");
            return msg;
        }

        if (seconds >= 3600L) {
            long hours = seconds / 3600L;
            msg = msg + hours + (hours == 1L ? " Stunde" : " Stunden");
            return msg;
        }

        if (seconds >= 60L) {
            long minutes = seconds / 60L;
            msg = msg + minutes + (minutes == 1L ? " Minute" : " Minuten");
            return msg;
        }

        if (seconds > 0L) {
            msg = msg + seconds + (seconds == 1L ? " Sekunde" : " Sekunden");
            return msg;
        }
        msg = msg + "0 Sekunden";
        return msg;
    }
}
