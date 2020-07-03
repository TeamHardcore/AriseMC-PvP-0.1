/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.managers;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.model.LootProtection;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class LootProtectionManager {

    private final Main plugin;

    private Map<Item, LootProtection> lootProtection;

    public LootProtectionManager(Main plugin) {
        this.plugin = plugin;
        this.lootProtection = new HashMap<>();
    }

    public void createLootProtection(UUID uuid, List<Item> items) {
        LootProtection protection = new LootProtection(uuid, items);
        items.forEach(item -> this.lootProtection.put(item, protection));
    }

    public void removeProtection(Item item) {
        if (!this.lootProtection.containsKey(item)) return;

        LootProtection protection = this.lootProtection.get(item);
        protection.getItems().remove(item);
        clearRandomKey(item);
        this.lootProtection.remove(item);
    }

    public LootProtection getLootProtection(Item item) {
        if (!this.lootProtection.containsKey(item)) return null;
        return this.lootProtection.get(item);
    }

    public void clearRandomKey(Item item) {
        ItemStack itemStack = item.getItemStack();
        ItemMeta meta = itemStack.getItemMeta();
        List<String> lore = meta.getLore();

        if (lore == null)
            return;

        lore.remove(lore.size() - 1);
        meta.setLore(lore);
        itemStack.setItemMeta(meta);
        item.setItemStack(itemStack);
    }

    public String getRandomKey() {
        StringBuilder builder = new StringBuilder();
        Random random = new Random();

        String pattern = "";
        pattern = pattern + "abcdefghijklmnopqrstuvwxyz";
        pattern = pattern + pattern.toUpperCase();
        pattern = pattern + "0123456789";

        int length = random.nextInt(11) + 10;
        for (int i = 0; i < length; ++i)
            builder.append(pattern.charAt(random.nextInt(pattern.length())));
        return builder.toString();
    }

}
