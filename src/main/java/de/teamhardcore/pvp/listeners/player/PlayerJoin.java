/*
 *
 * @author TeamHardcore
 *
 * Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.listeners.player;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.model.PlayerScoreboard;
import de.teamhardcore.pvp.model.Warp;
import de.teamhardcore.pvp.model.gambling.crates.base.BaseCrate;
import de.teamhardcore.pvp.utils.ItemBuilder;
import de.teamhardcore.pvp.utils.SkullCreator;
import de.teamhardcore.pvp.utils.StringDefaults;
import org.bukkit.Bukkit;
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

        if (!player.hasPlayedBefore()) {

            Warp spawn = Main.getInstance().getWarpManager().getWarp("Spawn");

            if (spawn != null)
                player.teleport(spawn.getLocation());

            Bukkit.getOnlinePlayers().forEach(target -> target.sendMessage(StringDefaults.NEWBIE_PREFIX + "§6" + player.getName() + " §7ist neu auf §6AriseMC§7."));
            player.sendMessage(" ");
            player.sendMessage(StringDefaults.PREFIX + "§7Willkommen auf §6AriseMC§7, §6" + player.getName() + "§7!");
            player.sendMessage(StringDefaults.PREFIX + "§7Wenn du fragen hast, melde dich im Support.");
            player.sendMessage(StringDefaults.PREFIX + "§7");
            player.sendMessage(StringDefaults.PREFIX + "§7Unser Discord§8: §7discord.arisemc.de");
            player.sendMessage(StringDefaults.PREFIX + "§7Unser Teamspeak§8: §6ts.arisemc.de");
            player.sendMessage(StringDefaults.PREFIX + " ");
            player.sendMessage(" ");
            this.plugin.getKitManager().getKit("member").giveItems(player);
        } else {
            player.sendMessage(StringDefaults.PREFIX + "§7Willkommen zurück, §6" + player.getName() + "§7!");
        }

        List<BaseCrate> crates = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            crates.add(this.plugin.getCrateManager().getCrate("TestCrate"));
            crates.add(this.plugin.getCrateManager().getCrate("DivineCrate"));
            crates.add(this.plugin.getCrateManager().getCrate("FirstTieredCrate"));
        }

        for (BaseCrate crate : crates)
            this.plugin.getUserManager().getUser(player.getUniqueId()).getUserData().addCrate(crate);

    }

}
