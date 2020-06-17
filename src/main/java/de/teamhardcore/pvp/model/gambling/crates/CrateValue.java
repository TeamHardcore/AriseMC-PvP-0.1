/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.gambling.crates;

public enum CrateValue {

    COMMON(0, "§9Gewöhnlich", 11),
    UNCOMMON(1, "§eUngewöhnlich", 4),
    EPIC(2, "§5§lEpisch", 10),
    LEGENDARY(3, "§c§lLegendär", 14);

    private final int rarity;
    private final String displayName;
    private final int color;

    CrateValue(int rarity, String displayName, int color) {
        this.rarity = rarity;
        this.displayName = displayName;
        this.color = color;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public int getRarity() {
        return this.rarity;
    }

    public int getColor() {
        return this.color;
    }

}
