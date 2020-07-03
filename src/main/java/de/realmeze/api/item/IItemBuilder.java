package de.realmeze.api.item;

import de.realmeze.impl.item.CollectionItem;
import org.bukkit.Material;
import org.bukkit.inventory.meta.ItemMeta;

public interface IItemBuilder extends IItem {
    Material getMaterial();
    ItemMeta getItemMeta();
    IItemBuilder setItemMeta(ItemMeta itemMeta);
    IItemBuilder setMaterial(Material material);
    IItemBuilder setLore(int line, String lore);
    IItemBuilder setName(String name);
    IItemBuilder setAmount(int amount);
    IItemBuilder setDurability(short durability);
    CollectionItem buildCollectionItem();
}
