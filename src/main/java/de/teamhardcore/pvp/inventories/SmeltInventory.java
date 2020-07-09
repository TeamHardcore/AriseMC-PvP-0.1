/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.inventories;

import de.teamhardcore.pvp.model.EnumSmeltable;
import de.teamhardcore.pvp.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class SmeltInventory {

    public static void openSmeltInventory(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 9 * 2, "§c§lSchmelzbare Items");

        for (EnumSmeltable smeltable : EnumSmeltable.values()) {
            inventory.addItem(new ItemBuilder(smeltable.getFrom()).setDurability(smeltable.getFromData()).setDisplayName(smeltable.getDisplayName()).build());
        }

        player.openInventory(inventory);
    }

}
