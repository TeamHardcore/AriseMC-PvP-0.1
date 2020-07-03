/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model;

public enum EnumLeague {

    BRONZE(500, "§cBronze"),
    SILVER(1000, "§7Silber"),
    GOLD(1500, "§eGold"),
    DIAMOND(2000, "§b§lDiamant"),
    PLATINUM(2500, "§8§lPlatinum");

    private int trophies;
    private String displayName;

    EnumLeague(int trophies, String displayName) {
        this.trophies = trophies;
        this.displayName = displayName;
    }

    public int getTrophies() {
        return trophies;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static EnumLeague getLeagueByTrophies(int trophies) {
        EnumLeague chosen = null;

        for (EnumLeague league : values()) {
            if (league.getTrophies() <= trophies)
                if (chosen == null || chosen.getTrophies() < league.getTrophies())
                    chosen = league;
        }

        return chosen;
    }

    public static boolean checkRankSwitch(int trophiesBefore, int trophiesAfter) {
        return (getLeagueByTrophies(trophiesBefore) != getLeagueByTrophies(trophiesAfter));
    }

}
