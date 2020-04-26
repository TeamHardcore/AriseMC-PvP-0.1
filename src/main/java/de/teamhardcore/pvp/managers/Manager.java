package de.teamhardcore.pvp.managers;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.model.teleport.TPDelay;
import de.teamhardcore.pvp.model.teleport.TPRequest;
import de.teamhardcore.pvp.utils.StringDefaults;
import de.teamhardcore.pvp.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class Manager {

    private final Main plugin;

    private final HashMap<Player, Long> teleportCooldowns = new HashMap<>();
    private final HashMap<Player, TPRequest> teleportRequests = new HashMap<>();
    private final HashMap<Player, TPDelay> teleportDelays = new HashMap<>();
    private final HashMap<Player, Location> lastPositions = new HashMap<>();
    private final HashMap<Player, Long> healCooldown = new HashMap<>();

    private final ArrayList<Player> playersInEnderchest = new ArrayList<>();
    private final ArrayList<Player> playersInVanish = new ArrayList<>();

    public Manager(Main plugin) {
        this.plugin = plugin;

        startTablistTask();
    }

    private void startTablistTask() {
        this.plugin.getServer().getScheduler().runTaskTimer(this.plugin, () -> {

            Bukkit.getOnlinePlayers().forEach(players -> {
                Util.sendHeaderFooter(players, "\n " + StringDefaults.SERVER_NAME + "\n", "\n§9§lPing§8: §7" + players.spigot().getPing() + "\n");
            });


        }, 20L, 20L);
    }

    public HashMap<Player, Long> getTeleportCooldowns() {
        return teleportCooldowns;
    }

    public HashMap<Player, TPRequest> getTeleportRequests() {
        return teleportRequests;
    }

    public HashMap<Player, TPDelay> getTeleportDelays() {
        return teleportDelays;
    }

    public HashMap<Player, Location> getLastPositions() {
        return lastPositions;
    }

    public HashMap<Player, Long> getHealCooldown() {
        return healCooldown;
    }

    public ArrayList<Player> getPlayersInEnderchest() {
        return playersInEnderchest;
    }

    public ArrayList<Player> getPlayersInVanish() {
        return playersInVanish;
    }

    public void vanishAll(Player player) {
        if (this.playersInVanish.contains(player)) return;
        this.playersInVanish.add(player);
        Bukkit.getOnlinePlayers().forEach(players -> {
            if (!players.hasPermission("arisemc.vanish.see") && players != player)
                players.hidePlayer(player);
        });
    }

    public void unvanishAll(Player player) {
        if (!this.playersInVanish.contains(player)) return;
        this.playersInVanish.remove(player);
        Bukkit.getOnlinePlayers().forEach(players -> players.showPlayer(player));
    }

    public Main getPlugin() {
        return plugin;
    }
}
