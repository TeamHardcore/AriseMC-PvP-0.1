/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.extras;

import org.bukkit.Material;

public enum EnumCommand {

    ENCHANT("§5§l/zaubertisch", Material.ENCHANTMENT_TABLE, "Öffne deinen eigenen Zaubertisch", 2000000),
    SWITCHGLASS("§b§l/switchglass", Material.GLASS, "Wandel Glass zu Glassflaschen um", 2000000),
    GOLDSWITCH("§6§l/goldswitch", Material.GOLD_INGOT, "Wandel Goldnuggets zu Goldbarren um", 2000000),
    ANVIL("§7§l/amboss", Material.ANVIL, "Öffne deinen eigenen Amboss", 2000000),
    FILL("§b§l/fill", Material.POTION, "Fülle alle Glassflaschen mit Wasser", 2000000),
    BODYSEE("§d§l/bodysee", Material.DIAMOND_CHESTPLATE, "Betrachte die Rüstung anderer", 2000000),
    SMELT("§7§l/smelt", Material.FURNACE, "Schmelze deine Items", 2000000),
    PLAYERTIME("§e§l/playertime", Material.WATCH, "Ändere deine persönliche Spielzeit", 2000000);

    private String displayName;
    private Material material;
    private String description;
    private long price;

    EnumCommand(String displayName, Material material, String description, long price) {
        this.displayName = displayName;
        this.material = material;
        this.description = description;
        this.price = price;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public long getPrice() {
        return price;
    }

    public Material getMaterial() {
        return material;
    }

    public static EnumCommand getCommandByName(String name) {
        for (EnumCommand command : values())
            if (command.name().equalsIgnoreCase(name))
                return command;
        return null;
    }
}
