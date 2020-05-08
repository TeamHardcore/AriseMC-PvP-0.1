/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.clan.shop.upgrades.requirements;

public enum RequirementType {

    COINS(""),
    MONEY(""),
    LEVEL("§cMindestens Clan Level §7%requirement%"),
    SLOTS("§cMindestens §7%requirement% §cMitglieder"),
    KILLS("§cMindestens §7%requirement% §cTötungen"),
    TROPHIES("§cMindestens §7%requirement% §cTrophäen");

    private String displayName;

    RequirementType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
