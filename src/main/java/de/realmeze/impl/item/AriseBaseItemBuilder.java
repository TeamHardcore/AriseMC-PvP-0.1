package de.realmeze.impl.item;

import de.realmeze.api.item.IItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class AriseBaseItemBuilder implements IItem {

    private final ItemStack itemStack = new ItemStack(Material.AIR);

    public AriseBaseItemBuilder(){
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
    public IItem setItemMeta(ItemMeta itemMeta) {
        this.itemStack.setItemMeta(itemMeta);
        return this;
    }

    @Override
    public IItem setMaterial(Material material) {
        this.itemStack.setType(material);
        return this;
    }

    @Override
    public IItem setLore(int line, String lore) {
        ItemMeta im = this.getItemMeta();
        List<String> oldLore = im.getLore();
        oldLore.set(line, lore);
        this.setItemMeta(im);
        return this;
    }

    @Override
    public IItem setName(String name) {
        ItemMeta im = this.itemStack.getItemMeta();
        im.setDisplayName(name);
        this.itemStack.setItemMeta(im);
        return this;
    }

    @Override
    public IItem setAmount(int amount) {
        this.itemStack.setAmount(amount);
        return this;
    }

    @Override
    public IItem setDurability(short durability) {
        this.itemStack.setDurability(durability);
        return this;
    }

}
