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
            this.plugin.getReportManager().getReportConfirmation().remove(player);
        }

        if (inventory.getTitle().equalsIgnoreCase("§c§lWähle einen Typ")) {
            this.plugin.getSpawnerManager().getPlayersInSpawnerChoosing().remove(player);
        }

        if (inventory.getTitle().startsWith("§c§lKits")) {
            this.plugin.getKitManager().getInventoryCache().remove(player);
        }

        if (inventory.getTitle().equalsIgnoreCase("§c§lKit-Vorschau")) {
            this.plugin.getKitManager().getPreviewCache().remove(player);
        }

    }
}
