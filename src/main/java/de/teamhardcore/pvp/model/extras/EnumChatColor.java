/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.extras;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public enum EnumChatColor {

    DARK_BLUE("§9§lDunkelblau", Material.STAINED_CLAY, 11, ChatColor.DARK_BLUE, 200000),
    GRAY("§7§lGrau", Material.STAINED_CLAY, 9, ChatColor.GRAY, 200000),
    ORANGE("§6§lOrange", Material.STAINED_CLAY, 1, ChatColor.GOLD, 200000),
    BLUE("&b§lHellblau", Material.STAINED_CLAY, 3, ChatColor.BLUE, 200000),
    YELLOW("§e§lGelb", Material.STAINED_CLAY, 4, ChatColor.YELLOW, 200000),
    WHITE("§f§lWeiß", Material.STAINED_CLAY, 0, ChatColor.WHITE, 200000),
    GREEN("§a§lGrün", Material.STAINED_CLAY, 5, ChatColor.GREEN, 200000),
    DARK_GREEN("§2§lDunkelgrün", Material.STAINED_CLAY, 13, ChatColor.DARK_GREEN, 200000);

    private String name;
    private Material material;
    private int durability;
    private ChatColor chatColor;
    private long price;

    EnumChatColor(String name, Material material, int durability, ChatColor chatColor, long price) {
        this.name = name;
        this.material = material;
        this.durability = durability;
        this.chatColor = chatColor;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public Material getMaterial() {
        return material;
    }

    public int getDurability() {
        return durability;
    }

    public ChatColor getChatColor() {
        return chatColor;
    }

    public long getPrice() {
        return price;
    }

    public static EnumChatColor getColorByName(String name) {
        for (EnumChatColor chatColor : values())
            if (chatColor.name().equalsIgnoreCase(name))
                return chatColor;
        return null;
    }

}
