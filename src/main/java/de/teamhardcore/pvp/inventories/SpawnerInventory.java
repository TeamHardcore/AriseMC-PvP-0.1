/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.inventories;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.model.customspawner.AbstractSpawnerType;
import de.teamhardcore.pvp.model.customspawner.CustomSpawner;
import de.teamhardcore.pvp.utils.ItemBuilder;
import de.teamhardcore.pvp.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class SpawnerInventory {

    private static Integer[] defaultSlots = new Integer[]{10, 11, 12, 19, 20, 21, 28, 29, 30};
    private static Integer[] premiumSlots = new Integer[]{14, 15, 16, 23, 24, 25, 32, 33, 34};

    public static void openInventory(Player player, CustomSpawner spawner) {
        Inventory inventory = Bukkit.createInventory(null, 9 * 6, "§c§lWähle einen Typ");

        updateInventory(player, spawner, inventory);
        player.openInventory(inventory);
        Main.getInstance().getSpawnerManager().getPlayersInSpawnerChoosing().put(player, spawner.getLocation());
    }

    private static void updateInventory(Player player, CustomSpawner spawner, Inventory inventory) {
        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(7).setDisplayName(" ").build());
        }

        int dIndex = 0;
        int pIndex = 0;
        for (AbstractSpawnerType type : Main.getInstance().getSpawnerManager().getSpawnerTypes()) {
            List<String> lore = new ArrayList<>();
            lore.add(" ");

            int slot = 0;

            if (!type.isPremium()) {
                if (dIndex >= defaultSlots.length) continue;
                slot = defaultSlots[dIndex];
                dIndex++;

                lore.add("§eKlicke§7, §eum diesen Typ auszuwählen.");
            } else {
                if (pIndex >= premiumSlots.length) continue;
                slot = premiumSlots[pIndex];
                pIndex++;

                if (type.hasUnlocked(Main.getInstance().getUserManager().getUser(player.getUniqueId()))) {
                    lore.add("§eKlicke§7, §eum diesen Typ auszuwählen.");
                } else {
                    lore.add("§c§lPreis§8: §7" + Util.formatNumber(type.getPrice()) + "$");
                    lore.add(" ");
                    lore.add("§cDu hast diesen Typ noch nicht freigeschaltet.");
                    lore.add("§cKlicke§7, §cum den Typ freizuschalten.");
                }
            }
            inventory.setItem(slot, new ItemBuilder(type.getDisplayItem()).setLore(lore).setDurability(type.getDurability()).setDisplayName(type.getName()).build());
        }
    }

    /*
     * Platziere diesen Spawner in deiner Base,
     * um ihn aufzuwerten.
     */

}
