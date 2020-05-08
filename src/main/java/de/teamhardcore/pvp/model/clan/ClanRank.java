/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.clan;

public enum ClanRank {

    MEMBER(0, "Member", "§9"),
    TRUSTED(1, "Trusted", "§e"),
    MOD(2, "Mod", "§5"),
    OWNER(3, "Owner", "§4");

    private int rankPosition;
    private String name;
    private String color;

    ClanRank(int rankPosition, String name, String color) {
        this.rankPosition = rankPosition;
        this.name = name;
        this.color = color;
    }

    public int getRankPosition() {
        return this.rankPosition;
    }

    public String getName() {
        return this.name;
    }

    public String getColor() {
        return this.color;
    }

    public static ClanRank getByName(String name) {
        for (ClanRank ranks : values()) {
            if (ranks.name().equalsIgnoreCase(name))
                return ranks;
        }
        return null;
    }
}
