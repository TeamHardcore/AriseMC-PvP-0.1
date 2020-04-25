/*
 *
 * @author TeamHardcore
 *
 * Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.utils;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class DateFormats {

    public static final SimpleDateFormat FORMAT_HOME = new SimpleDateFormat("dd.MM.yy 'um' HH:mm 'Uhr'", Locale.GERMAN);
    public static final SimpleDateFormat FORMAT_ACHIEVEMENT = new SimpleDateFormat("dd.MM.yy 'um' HH:mm 'Uhr'", Locale.GERMAN);
    public static final SimpleDateFormat FORMAT_NORMAL = new SimpleDateFormat("dd.MM.yy HH:mm 'Uhr'", Locale.GERMAN);
    public static final SimpleDateFormat FORMAT_SIMPLE_EXT = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.GERMAN);
    public static final SimpleDateFormat FORMAT_SIMPLE = new SimpleDateFormat("dd.MM.yy HH:mm", Locale.GERMAN);

}
