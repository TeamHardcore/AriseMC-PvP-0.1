/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.gambling.crates.content;

import org.bukkit.Sound;

public enum ContentValue {
    COMMON(0, "Gewöhnlich", "§9Gewöhnlich", 11, 100, Sound.ITEM_PICKUP, 0.5F),
    RARE(1, "Selten", "§eSelten", 4, 75, Sound.ITEM_PICKUP, 0.5F),
    MYSTIC(2, "Mystisch", "§5§lMystisch", 10, 25, Sound.EXPLODE, 2.0F),
    LEGENDARY(3, "Legendär", "§c§lLegendär", 14, 5, Sound.ANVIL_LAND, 1.0F);

    private final int value;
    private final String name;
    private final String displayName;
    private final int color;
    private final int defaultChanceWeight;
    private final Sound rewardSound;
    private final float soundPitch;

    ContentValue(int value, String name, String displayName, int color, int defaultChanceWeight, Sound rewardSound, float soundPitch) {
        this.value = value;
        this.name = name;
        this.displayName = displayName;
        this.color = color;
        this.defaultChanceWeight = defaultChanceWeight;
        this.rewardSound = rewardSound;
        this.soundPitch = soundPitch;
    }


    public int getValue() {
        return this.value;
    }


    public String getName() {
        return this.name;
    }


    public String getDisplayName() {
        return this.displayName;
    }


    public int getColor() {
        return this.color;
    }


    public int getDefaultChanceWeight() {
        return this.defaultChanceWeight;
    }


    public Sound getRewardSound() {
        return this.rewardSound;
    }


    public float getSoundPitch() {
        return this.soundPitch;
    }

}
