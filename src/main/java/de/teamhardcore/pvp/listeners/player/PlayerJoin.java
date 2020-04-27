/*
 *
 * @author TeamHardcore
 *
 * Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.listeners.player;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.model.PlayerScoreboard;
import de.teamhardcore.pvp.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {

    private final Main plugin;

    public PlayerJoin(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);

        Player player = event.getPlayer();

        this.plugin.getUserManager().addToCache(player.getUniqueId());

        this.plugin.getScoreboardManager().setScoreboard(player, PlayerScoreboard.ScoreboardType.DEFAULT);
        this.plugin.getScoreboardManager().updateAllScoreboards(true, true);

        player.getInventory().addItem(new ItemBuilder(Material.CHEST).setDisplayName("§a§lTest Crate").setAmount(64).build());
    }

}
