/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.listeners.player;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.model.LootProtection;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class PlayerPickupItem implements Listener {

    private final Main plugin;

    public PlayerPickupItem(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();
        Item item = event.getItem();

        if (this.plugin.getLootProtectionManager().getLootProtection(item) == null)
            return;

        LootProtection protection = this.plugin.getLootProtectionManager().getLootProtection(item);

        if (protection.getUuid().equals(player.getUniqueId()) && !event.isCancelled()) {
            event.setCancelled(false);
            this.plugin.getLootProtectionManager().removeProtection(item);
            this.plugin.getLootProtectionManager().clearRandomKey(item);
        }

        long diff = System.currentTimeMillis() - (protection.getTimestamp() + 5000L);

        if (diff <= 0L) {
            event.setCancelled(true);
            return;
        }

        event.setCancelled(true);
        this.plugin.getLootProtectionManager().removeProtection(item);
        this.plugin.getLootProtectionManager().clearRandomKey(item);
    }

}
