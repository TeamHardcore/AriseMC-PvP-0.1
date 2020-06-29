package de.realmeze.api.item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public interface IItem {
    ItemStack getVanillaItemStack();
    Material getMaterial();
    ItemMeta getItemMeta();
    IItem setItemMeta(ItemMeta itemMeta);
    IItem setMaterial(Material material);
    IItem setLore(int line, String lore);
    IItem setName(String name);
    IItem setAmount(int amount);
    IItem setDurability(short durability);
}
