package de.teamhardcore.pvp.utils;

public class StringDefaults {

    public final static String PREFIX = "§8§l> §r";
    public final static String GLOBAL_PREFIX = "§9§lAriseMC";
    public final static String NO_PERM = PREFIX + "§cDazu hast du keine Berechtigung!";
    public final static String NOT_ONLINE = PREFIX + "§cDieser Spieler ist nicht online.";

    public final static String REPORT_PREFIX = "§c§LREPORT §8§l> §r";


    public static String getHeader(String color, String header) {
        return "§8§m->--------" + color + header + "§8§m--------<-";
        //  return "->--------" + header + "--------<-";
        //  return "§8§m->--------§r " + color + header.toUpperCase() + "§8§m--------<-";
    }

    public static String getFooter(String footer) {
        String a = "";
        for (int i = 0; i < footer.length(); i++) {
            a = a + "-";
        }

        return "§8§m->--------" + (a + "-") + "§8§m--------<-";
        //return "->--------" + (a) + "--------<-";
        // return "§8§m->--------" + (a + "-") + " --------<-§r";
    }
}
