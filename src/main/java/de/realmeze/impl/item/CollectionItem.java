package de.realmeze.impl.item;

import de.realmeze.api.collection.collection.ICollectable;
import de.realmeze.api.item.IItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CollectionItem implements ICollectable, IItem {

    private ItemStack itemStack;

    public CollectionItem(ItemStack itemStack) {
        this.itemStack = itemStack;
    }


    @Override
    public ItemStack getVanillaItemStack() {
        return this.itemStack;
    }

    @Override
    public void giveToPlayer(Player player) {
        player.getInventory().addItem(getVanillaItemStack());
    }
}
