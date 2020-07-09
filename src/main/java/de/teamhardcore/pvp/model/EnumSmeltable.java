/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model;

import org.bukkit.Material;

public enum EnumSmeltable {

    GOLD_ORE(Material.GOLD_ORE, 0, Material.GOLD_INGOT, 0, "§8» §aGolderz §7zu §aGoldbarren"),
    IRON_ORE(Material.IRON_ORE, 0, Material.IRON_INGOT, 0, "§8» §aEisenerz §7zu §aEisenbarren"),
    COAL_ORE(Material.COAL_ORE, 0, Material.COAL_ORE, 0, "§8» §aKohleerz §7zu §aKohle"),
    LAPIS_ORE(Material.LAPIS_ORE, 0, Material.INK_SACK, 4, "§8» §aLapiserz §7zu §aLapis"),
    DIAMOND_ORE(Material.DIAMOND_ORE, 0, Material.DIAMOND, 0, "§8» §aDiamanterz §7zu §aDiamant"),
    REDSTONE_ORE(Material.REDSTONE_ORE, 0, Material.REDSTONE, 0, "§8» §aRedstoneerz §7zu §aRedstone"),
    EMERALD_ORE(Material.EMERALD_ORE, 0, Material.EMERALD, 0, "§8» §aSmaragderz §7zu §aSmaragd"),
    NETHERRACK(Material.NETHERRACK, 0, Material.NETHER_BRICK_ITEM, 0, "§8» §aNetherstein §7zu §aNetherziegel"),
    COBBLESTONE(Material.COBBLESTONE, 0, Material.STONE, 0, "§8» §aBruchstein §7zu §aStein"),
    BEEF(Material.RAW_BEEF, 0, Material.COOKED_BEEF, 0, "§8» §aRindfleisch §7zu §aGekochtes Rindfleisch"),
    PORKCHOP(Material.PORK, 0, Material.GRILLED_PORK, 0, "§8» §aSchweinefleisch §7zu §aGegrilltes Schweinefleisch"),
    FISH(Material.RAW_FISH, 0, Material.COOKED_FISH, 0, "§8» §aFisch §7zu §aGekochter Fisch"),
    SALMON(Material.RAW_FISH, 1, Material.COOKED_FISH, 1, "§8» §aLachs §7zu §aGekochter Lachs"),
    CHICKEN(Material.RAW_CHICKEN, 0, Material.COOKED_CHICKEN, 0, "§8» §aHühnchen §7zu §aGekochtes Hühnchen"),
    RABBIT(Material.RABBIT, 0, Material.COOKED_RABBIT, 0, "§8» §aHasenfleisch §7zu §aGekochtes Hasenfleisch"),
    MUTTON(Material.MUTTON, 0, Material.COOKED_MUTTON, 0, "§8» §aHammelfleisch §7zu §aGekochtes Hammelfleisch"),
    POTATO(Material.POTATO_ITEM, 0, Material.BAKED_POTATO, 0, "§aKartoffel §7zu §aGebackene Kartoffel"),
    CACTUS(Material.CACTUS, 0, Material.INK_SACK, 2, "§8» §aKaktus §7zu §aDunkelgrüne Farbe");

    private final Material from;
    private final int fromData;
    private final Material to;
    private final int toData;
    private final String displayName;

    EnumSmeltable(Material from, int fromData, Material to, int toData, String displayName) {
        this.from = from;
        this.fromData = fromData;
        this.to = to;
        this.toData = toData;
        this.displayName = displayName;
    }


    public Material getFrom() {
        return this.from;
    }


    public int getFromData() {
        return this.fromData;
    }


    public Material getTo() {
        return this.to;
    }


    public int getToData() {
        return this.toData;
    }


    public String getDisplayName() {
        return this.displayName;
    }


    public static EnumSmeltable getByItemData(Material from, int fromData) {
        for (EnumSmeltable smeltable : values()) {
            if (smeltable.getFrom() == from && smeltable.getFromData() == fromData)
                return smeltable;
        }
        return null;
    }

}
