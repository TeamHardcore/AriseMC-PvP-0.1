/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.managers;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.model.crates.AbstractCrate;
import de.teamhardcore.pvp.model.crates.CrateItem;
import de.teamhardcore.pvp.model.crates.CrateOpening;
import de.teamhardcore.pvp.model.crates.crates.TestCrate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CrateManager {

    private final Main plugin;

    private final HashMap<String, AbstractCrate> availableCrates = new HashMap<String, AbstractCrate>() {{
        put("Test", new TestCrate());
    }};
    private final HashMap<Player, CrateOpening> activeOpenings = new HashMap<>();

    public CrateManager(Main plugin) {
        this.plugin = plugin;
    }

    public AbstractCrate getCrate(String name) {
        if (!this.availableCrates.containsKey(name)) return null;
        return this.availableCrates.get(name);
    }

    public void openCrateContent(Player player, AbstractCrate crate) {
        if (crate.getCrateItems().isEmpty())
            return;

        Inventory inventory = Bukkit.createInventory(null, 9 * 6, "§c§lCrate Inhalt");
        //   IntStream.range(0, inventory.getSize()).forEach(value -> inventory.setItem(value, ItemDefaults.PLACEHOLDER));

        List<CrateItem> contents = new ArrayList<>(crate.getCrateItems());

        int count = 0;

        for (CrateItem item : contents) {
            if (count >= inventory.getSize())
                break;

            ItemStack display = item.getItemStack().clone();
            ItemMeta meta = display.getItemMeta();
            List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<>();
            lore.add(" ");
            lore.add(" §8■ §eChance§8: §7" + item.getWeight() + "%");
            lore.add(" ");
            //    lore.add("§e§oDas Item wurde bereits §7" + Util.convertLong((long) crate.getReceivedAmount(item)) + "x §e§ogezogen.");
            meta.setLore(lore);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_POTION_EFFECTS);
            display.setItemMeta(meta);
            inventory.addItem(display);
            count++;
        }

        player.openInventory(inventory);
    }

    public HashMap<Player, CrateOpening> getActiveOpenings() {
        return activeOpenings;
    }

    public HashMap<String, AbstractCrate> getAvailableCrates() {
        return availableCrates;
    }
}
