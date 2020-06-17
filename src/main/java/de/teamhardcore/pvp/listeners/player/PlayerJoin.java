/*
 *
 * @author TeamHardcore
 *
 * Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.listeners.player;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.model.PlayerScoreboard;
import de.teamhardcore.pvp.model.gambling.crates.base.BaseCrate;
import de.teamhardcore.pvp.utils.ItemBuilder;
import de.teamhardcore.pvp.utils.SkullCreator;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

        List<BaseCrate> crates = new ArrayList<>();

        for (int i = 0; i < 14; i++) {
            if (new Random().nextInt(2) == 1) {
                crates.add(this.plugin.getCrateManager().getCrate("TestCrate"));
            } else crates.add(this.plugin.getCrateManager().getCrate("DivineCrate"));
        }

        for (BaseCrate crate : crates)
            this.plugin.getUserManager().getUser(player.getUniqueId()).getUserData().addCrate(crate);

    }

}
