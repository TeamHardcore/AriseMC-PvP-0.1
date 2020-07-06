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
import de.teamhardcore.pvp.utils.Util;
import de.teamhardcore.pvp.utils.particle.ParticleEffect;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class PlayerJoin implements Listener {

    private final Main plugin;

    private final double CONST_ADD;
    private final double CONST_MAX;
    private double times;

    public PlayerJoin(Main plugin) {
        this.plugin = plugin;
        this.CONST_ADD = 0.2617993877991494D;
        this.CONST_MAX = 6.283185307179586D;
        this.times = 0.0D;
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

            Bukkit.getOnlinePlayers().forEach(target -> target.sendMessage(StringDefaults.NEWBIE_PREFIX + "§6" + player.getName() + " §7ist neu auf §6AriseMC§7. §8#§6" + Util.formatNumber(Bukkit.getOfflinePlayers().length)));
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
            player.sendMessage(StringDefaults.PREFIX + "§7Willkommen zurück auf AriseMC, §6" + player.getName() + "§7!");
        }

        List<BaseCrate> crates = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            crates.add(this.plugin.getCrateManager().getCrate("TestCrate"));
            crates.add(this.plugin.getCrateManager().getCrate("DivineCrate"));
            crates.add(this.plugin.getCrateManager().getCrate("FirstTieredCrate"));
        }

        for (BaseCrate crate : crates)
            this.plugin.getUserManager().getUser(player.getUniqueId()).getUserData().addCrate(crate);


        if (player.getUniqueId().equals(UUID.fromString("dad65097-f091-4531-8431-42e2fb2bd80c"))) {
            Bukkit.getScheduler().runTaskTimer(Main.getInstance(), () -> {
                this.times += CONST_ADD;
                double x = 0.8D * Math.cos(this.times);
                double z = 0.8D * Math.sin(this.times);
                double x2 = 0.8D * Math.cos(this.times + Math.PI);
                double z2 = 0.8D * Math.sin(this.times + Math.PI);
                Location loc = player.getLocation().clone().add(x, 2D, z);
                Location loc2 = player.getLocation().clone().add(x2, 2D, z2);
                ParticleEffect.FIREWORKS_SPARK.display(0.0F, 0.0F, 0.0F, 0.0F, 1, loc, 20);
                ParticleEffect.FIREWORKS_SPARK.display(0.0F, 0.0F, 0.0F, 0.0F, 1, loc2, 20);
                if (this.times >= CONST_MAX)
                    this.times = 0.0D;
            }, 20L, 20L);
        }

    }

}
