/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.crates;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class CrateItem {

    private ItemStack itemStack;
    private double weight;

    private CrateItemAction action;

    public CrateItem(ItemStack itemStack, double weight, CrateItemAction action) {
        this.itemStack = itemStack;
        this.weight = weight;
        this.action = action;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public double getWeight() {
        return weight;
    }

    public CrateItemAction getAction() {
        return action;
    }
}
