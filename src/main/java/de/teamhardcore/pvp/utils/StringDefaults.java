package de.teamhardcore.pvp.utils;

public class StringDefaults {

    public final static String SERVER_NAME = "§6§lAriseMC";
    public final static String PREFIX = "§8» §r";
    public final static String GLOBAL_PREFIX = "§6§lAriseMC " + PREFIX;
    public final static String NO_PERM = PREFIX + "§cDazu hast du keine Berechtigung!";
    public final static String NOT_ONLINE = PREFIX + "§cDieser Spieler ist nicht online.";

    public final static String REPORT_PREFIX = "§c§LREPORT §8§l> §r";
    public final static String MSG_PREFIX = "§c§lMSG §8§l> §r";
    public final static String CLAN_PREFIX = "§c§lCLAN " + PREFIX;
    public final static String DUEL_PREFIX = "§c§lDUELL  " + PREFIX;


    public final static String SLOT_UPGRADE = "slot";
    public final static String WARP_UPGRADE = "warp";
    public final static String CHEST_UPGRADE = "chest";
    public final static String LEVEL_UPGRADE = "level";


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
