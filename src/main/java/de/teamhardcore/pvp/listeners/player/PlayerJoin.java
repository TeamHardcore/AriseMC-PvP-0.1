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
import de.teamhardcore.pvp.utils.SkullCreator;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

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

        ItemStack woodSkull = SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTJkZDdkODE4Y2JhNjUyYjAxY2YzNTBkMmIyYTFjZWVkZDRmNDZhY2FkMDViMmNlODFjM2Y4NzdlYWI3MTcifX19");
        ItemStack testCrate = new ItemBuilder(woodSkull).setDisplayName("§a§lTest Crate").setAmount(64).setLore("", "§eKlicke§7, §eum die Crate zu öffnen.").build();
        player.getInventory().addItem(testCrate);
    }

}
