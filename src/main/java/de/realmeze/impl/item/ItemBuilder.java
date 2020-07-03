package de.realmeze.impl.item;

import de.realmeze.api.item.IItem;
import de.realmeze.api.item.IItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ItemBuilder implements IItemBuilder {

    private final ItemStack itemStack = new ItemStack(Material.AIR);

    public ItemBuilder(){
    }

    @Override
    public ItemStack getVanillaItemStack() {
        return itemStack;
    }

    @Override
    public Material getMaterial() {
        return this.itemStack.getType();
    }

    @Override
    public ItemMeta getItemMeta() {
        return this.itemStack.getItemMeta();
    }

    @Override
    public IItemBuilder setItemMeta(ItemMeta itemMeta) {
        this.itemStack.setItemMeta(itemMeta);
        return this;
    }

    @Override
    public IItemBuilder setMaterial(Material material) {
        this.itemStack.setType(material);
        return this;
    }

    @Override
    public IItemBuilder setLore(int line, String lore) {
        ItemMeta im = this.getItemMeta();
        List<String> oldLore = im.getLore();
        oldLore.set(line, lore);
        this.setItemMeta(im);
        return this;
    }

    @Override
    public IItemBuilder setName(String name) {
        ItemMeta im = this.itemStack.getItemMeta();
        im.setDisplayName(name);
        this.itemStack.setItemMeta(im);
        return this;
    }

    @Override
    public IItemBuilder setAmount(int amount) {
        this.itemStack.setAmount(amount);
        return this;
    }

    @Override
    public IItemBuilder setDurability(short durability) {
        this.itemStack.setDurability(durability);
        return this;
    }

    public CollectionItem buildCollectionItem(){
        return new CollectionItem(getVanillaItemStack());
    }

}
