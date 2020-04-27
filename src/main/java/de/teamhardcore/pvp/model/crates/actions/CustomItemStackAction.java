/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.crates.actions;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CustomItemStackAction implements CrateItemAction {

    private ItemStack itemStack;

    public CustomItemStackAction(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    @Override
    public void doAction(Player player) {
        player.getInventory().addItem(this.itemStack);
    }
}
