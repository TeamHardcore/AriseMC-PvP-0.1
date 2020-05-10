/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.listeners.world;

import de.teamhardcore.pvp.Main;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class SignChange implements Listener {

    private final Main plugin;

    public SignChange(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        Player player = event.getPlayer();

        if (player.hasPermission("arisemc.sign.color")) {
            for (int i = 0; i < event.getLines().length; i++)
                event.setLine(i, ChatColor.translateAlternateColorCodes('&', event.getLine(i)));

        }
    }

}
