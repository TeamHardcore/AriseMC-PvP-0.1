/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.inventories;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.model.customspawner.CustomSpawner;
import de.teamhardcore.pvp.model.customspawner.EnumSpawnerType;
import de.teamhardcore.pvp.user.User;
import de.teamhardcore.pvp.utils.ItemBuilder;
import de.teamhardcore.pvp.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;

import java.util.ArrayList;
import java.util.List;

public class SpawnerInventory {

    private static final Integer[] slots = new Integer[]{10, 11, 12, 13, 14, 15, 16, 21, 22, 23};

    public static void openInventory(Player player, CustomSpawner spawner) {
        Inventory inventory = Bukkit.createInventory(null, 9 * 4, "§c§lWähle einen Spawner");

        for (int i = 0; i < inventory.getSize(); i++)
            inventory.setItem(i, new ItemBuilder(Material.STAINED_GLASS_PANE).setDisplayName(" ").setDurability(7).build());

        updateInventory(player, spawner, inventory);
        player.openInventory(inventory);
        Main.getInstance().getSpawnerManager().getPlayersInSpawnerChoosing().put(player, spawner.getLocation());
    }

    private static void updateInventory(Player player, CustomSpawner spawner, Inventory inventory) {
        User user = Main.getInstance().getUserManager().getUser(player.getUniqueId());

        int index = 0;
        for (EnumSpawnerType type : EnumSpawnerType.values()) {
            int slot = 0;
            List<String> lore = new ArrayList<>();
            lore.add(" ");

            boolean hasUnlocked = EnumSpawnerType.hasUnlocked(user, type);
            String displayName = (hasUnlocked ? "§a§l" : "§c§l") + type.getName() + " Spawner";
            boolean isType = spawner.getType().equals(type);

            if (index >= slots.length) continue;
            slot = slots[index];
            index++;

            if (isType) {
                lore.add("§8● §a§lAusgewählt");
                inventory.setItem(slot, new ItemBuilder(type.getDisplayItem()).addItemFlags(ItemFlag.HIDE_ENCHANTS).addEnchantment(Enchantment.ARROW_DAMAGE, 1).setLore(lore).setDurability(type.getDurability()).setDisplayName(displayName).build());
                continue;
            }

            if (type.isPremium()) {
                if (hasUnlocked) {
                    lore.add("§eKlicke§7, §eum diesen Typ auszuwählen.");
                } else {
                    lore.add("§c§lPreis§8: §7" + Util.formatNumber(type.getPrice()) + "$");
                    lore.add(" ");
                    lore.add("§cDu hast diesen Typ noch nicht freigeschaltet.");
                    lore.add("§cKlicke§7, §cum den Typ freizuschalten.");
                }
            } else lore.add("§eKlicke§7, §eum diesen Typ auszuwählen.");
            inventory.setItem(slot, new ItemBuilder(type.getDisplayItem()).setLore(lore).setDurability(type.getDurability()).setDisplayName(displayName).build());
        }
    }

   /* private static void updateInventory(Player player, CustomSpawner spawner, Inventory inventory) {

           private static final Integer[] defaultSlots = new Integer[]{10, 11, 12, 19, 20, 21, 28, 29, 30};
    private static final Integer[] premiumSlots = new Integer[]{14, 15, 16, 23, 24, 25, 32, 33, 34};

        for(
    int i = 0; i<inventory.getSize();i++)

    {
        inventory.setItem(i, new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(7).setDisplayName(" ").build());
    }

    User user = Main.getInstance().getUserManager().getUser(player.getUniqueId());

    int dIndex = 0;
    int pIndex = 0;
        for(
    EnumSpawnerType type :EnumSpawnerType.values())

    {
        List<String> lore = new ArrayList<>();
        lore.add(" ");

        int slot;

        boolean hasUnlocked = EnumSpawnerType.hasUnlocked(user, type);
        String displayName = (hasUnlocked ? "§a§l" : "§c§l") + type.getName() + " Spawner";

        if (spawner.getType().equals(type)) {
            lore.add("§8● §a§lAusgewählt");

            if (!type.isPremium()) {
                if (dIndex >= defaultSlots.length) continue;
                slot = defaultSlots[dIndex];
                dIndex++;
            } else {
                if (pIndex >= premiumSlots.length) continue;
                slot = premiumSlots[pIndex];
                pIndex++;
            }

            inventory.setItem(slot,
                    new ItemBuilder(type.getDisplayItem())
                            .setLore(lore)
                            .addItemFlags(ItemFlag.HIDE_ENCHANTS)
                            .addEnchantment(Enchantment.ARROW_DAMAGE, 1)
                            .setDurability(type.getDurability())
                            .setDisplayName(displayName)
                            .build());
            continue;
        }

        if (!type.isPremium()) {
            if (dIndex >= defaultSlots.length) continue;
            slot = defaultSlots[dIndex];
            dIndex++;

            lore.add("§eKlicke§7, §eum diesen Typ auszuwählen.");
        } else {
            if (pIndex >= premiumSlots.length) continue;
            slot = premiumSlots[pIndex];
            pIndex++;

            if (hasUnlocked) {
                lore.add("§eKlicke§7, §eum diesen Typ auszuwählen.");
            } else {
                lore.add("§c§lPreis§8: §7" + Util.formatNumber(type.getPrice()) + "$");
                lore.add(" ");
                lore.add("§cDu hast diesen Typ noch nicht freigeschaltet.");
                lore.add("§cKlicke§7, §cum den Typ freizuschalten.");
            }
        }
        inventory.setItem(slot, new ItemBuilder(type.getDisplayItem()).setLore(lore).setDurability(type.getDurability()).setDisplayName(displayName).build());
    }
}*/
}
