/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.crates;

import de.teamhardcore.pvp.model.crates.actions.CrateItemAction;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class CrateItem {

    private ItemStack itemStack;
    private double weight;

    private List<CrateItemAction> actions;

    public CrateItem(ItemStack itemStack, double weight, CrateItemAction... actions) {
        this.itemStack = itemStack;
        this.weight = weight;
        this.actions = Arrays.asList(actions);
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public double getWeight() {
        return weight;
    }

    public List<CrateItemAction> getActions() {
        return actions;
    }
}
