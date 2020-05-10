/*
 *
 * @author TeamHardcore
 *
 * Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.listeners.inventory;

import de.teamhardcore.pvp.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

public class InventoryClose implements Listener {

    private final Main plugin;

    public InventoryClose(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        Inventory inventory = event.getInventory();
        Player player = (Player) event.getPlayer();

        if (inventory.getTitle().startsWith("§9§lReporte ")) {
            Main.getInstance().getReportManager().getReportConfirmation().remove(player);
        }

        if (inventory.getTitle().equalsIgnoreCase("§c§lWähle einen Typ")) {
            Main.getInstance().getSpawnerManager().getPlayersInSpawnerChoosing().remove(player);
        }

    }
}
