/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.listeners.player;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.model.duel.Duel;
import de.teamhardcore.pvp.utils.StringDefaults;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerItemConsume implements Listener {

    private final Main plugin;

    public PlayerItemConsume(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerConsume(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        ItemStack itemStack = event.getItem();

        if (itemStack.getType().equals(Material.GOLDEN_APPLE)) {
            if (this.plugin.getDuelManager().getDuelCache().containsKey(player.getUniqueId())) {
                Duel duel = this.plugin.getDuelManager().getDuelCache().get(player.getUniqueId());

                if (duel.getConfiguration().getSettings().isUseGoldenApple()) {
                    player.sendMessage(StringDefaults.DUEL_PREFIX + "§cDu kannst keine Goldenen Äpfel benutzen.");
                    event.setCancelled(true);
                    player.setItemInHand(itemStack);
                    return;
                }


            }

        }


    }

}
