/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.inventories;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.user.User;
import de.teamhardcore.pvp.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class RewardInventory {

    public static void openRewardInventory(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 9 * 3, "§c§lBelohnungen");

        for (int i = 0; i < inventory.getSize(); i++)
            inventory.setItem(i, new ItemBuilder(Material.STAINED_GLASS_PANE).setDisplayName(" ").setDurability(7).build());

        inventory.setItem(11, new ItemBuilder(Material.STORAGE_MINECART).setDisplayName("§aTägliche Belohnung").setLore("", "§eKlicke§7, §eum die Belohnung abzuholen.").build());
        inventory.setItem(12, new ItemBuilder(Material.STORAGE_MINECART).setDisplayName("§6§lWöchentliche Belohnung").setLore("", "§eKlicke§7, §eum die Belohnung abzuholen.").build());
        inventory.setItem(13, new ItemBuilder(Material.STORAGE_MINECART).setDisplayName("§c§lMonatliche Belohnung").setLore("", "§eKlicke§7, §eum die Belohnung abzuholen.").build());
        inventory.setItem(15, new ItemBuilder(Material.EMERALD_BLOCK).setDisplayName("§b§lVote Belohnung").setLore("", "§eKlicke§7, §eum die Belohnung abzuholen.").build());

        player.openInventory(inventory);
    }

}
