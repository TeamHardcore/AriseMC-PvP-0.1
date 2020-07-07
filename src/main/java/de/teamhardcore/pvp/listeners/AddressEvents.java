/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.listeners;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.managers.Manager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.net.InetAddress;

public class AddressEvents implements Listener {
    private final Main plugin;

    public AddressEvents(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onLoginEvent(PlayerLoginEvent event) {
        if (this.plugin.getManager().getAddresses().get(event.getAddress()) >= Manager.ADDRESS_LIMIT) {
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "§cZuviele Verbindungen von dieser IP");
            return;
        }
    }

    @EventHandler
    public void onPreLogin(AsyncPlayerPreLoginEvent event) {
        this.plugin.getManager().getAddresses().adjustOrPutValue(event.getAddress(), 1, 1);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        InetAddress address = event.getPlayer().getAddress().getAddress();
        this.plugin.getManager().getAddresses().adjustValue(address, -1);
        if (this.plugin.getManager().getAddresses().get(address) <= 0)
            this.plugin.getManager().getAddresses().remove(address);
    }

    public Main getPlugin() {
        return plugin;
    }
}
