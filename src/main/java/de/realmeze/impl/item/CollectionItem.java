package de.realmeze.impl.item;

import de.realmeze.api.collection.collection.ICollectable;
import de.realmeze.api.item.IItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class CollectionItem implements ICollectable, IItem {

    private ItemStack itemStack;

    public CollectionItem(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    @Override
    public Material getToCollect() {
        return getVanillaItemStack().getType();
    }

    @Override
    public ItemStack getVanillaItemStack() {
        return this.itemStack;
    }
}
