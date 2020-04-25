/*
 *
 * @author TeamHardcore
 *
 * Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.listeners.player;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.model.Support;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class AsyncPlayerChat implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();

        if (message.contains("̇") || message.equalsIgnoreCase("")) {
            event.setCancelled(true);
            return;
        }

        if (Main.getInstance().getSupportManager().getSupports().containsKey(player)) {
            Support support = Main.getInstance().getSupportManager().getSupport(player);
            Support.SupportRole role = support.getRoles().get(player);

            event.getRecipients().clear();

            for (Player players : support.getRoles().keySet())
                event.getRecipients().add(players);

            if (role == Support.SupportRole.MEMBER) {
                event.setFormat("§8[§c§lUser§8] §e%1$s: §7%2$s");
            } else {
                event.setFormat("§8[§4§lSup§8] §c%1$s: §7%2$s");
            }
            return;
        }


    }

}
