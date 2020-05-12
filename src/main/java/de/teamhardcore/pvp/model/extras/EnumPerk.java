/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.extras;

import org.bukkit.Achievement;
import org.bukkit.Material;
import org.bukkit.potion.PotionEffectType;

public enum EnumPerk {

    SPEED("§b§lSpeed II", Material.SUGAR, 0, PotionEffectType.SPEED, 1, 0),
    ANTI_DEBUFF("§2§lAnti-Debuff", Material.POTION, 8228, null, 0, 2000000),
    JUMP("§e§lSprungkraft II", Material.POTION, 8235, PotionEffectType.JUMP, 1, 2000000),
    NO_HUNGER("§f§lKein Hunger", Material.BREAD, 0, null, 0, 2000000),
    STRENGTH("§c§lStärke II", Material.DIAMOND_SWORD, 0, PotionEffectType.INCREASE_DAMAGE, 1, 2000000),
    FAST_DIG("§e§lEile II", Material.GOLD_PICKAXE, 0, PotionEffectType.FAST_DIGGING, 1, 2000000),
    DOUBLE_XP("§c§lDouble XP", Material.EXP_BOTTLE, 0, null, 0, 2000000),
    ANTI_FIRE("§6§lFeuerresistenz", Material.FIREBALL, 0, PotionEffectType.FIRE_RESISTANCE, 0, 2000000);

    private String name;
    private Material displayItem;
    private int durability;
    private PotionEffectType type;
    private int amplifier;
    private long prize;

    EnumPerk(String name, Material displayItem, int durability, PotionEffectType type, int amplifier, long prize) {
        this.name = name;
        this.displayItem = displayItem;
        this.durability = durability;
        this.type = type;
        this.amplifier = amplifier;
        this.prize = prize;
    }

    public String getName() {
        return name;
    }

    public Material getDisplayItem() {
        return displayItem;
    }

    public int getDurability() {
        return durability;
    }

    public PotionEffectType getType() {
        return type;
    }

    public int getAmplifier() {
        return amplifier;
    }

    public long getPrize() {
        return prize;
    }

    public static EnumPerk getPerkByName(String name) {
        for (EnumPerk perk : values())
            if (perk.name().equalsIgnoreCase(name))
                return perk;
        return null;
    }
}
