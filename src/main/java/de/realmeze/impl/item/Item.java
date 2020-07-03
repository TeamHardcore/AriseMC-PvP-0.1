package de.realmeze.impl.item;

import de.realmeze.api.item.IItem;
import org.bukkit.inventory.ItemStack;

public class Item implements IItem {

    private ItemStack itemStack;

    public Item(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    @Override
    public ItemStack getVanillaItemStack() {
        return this.itemStack;
    }
}
