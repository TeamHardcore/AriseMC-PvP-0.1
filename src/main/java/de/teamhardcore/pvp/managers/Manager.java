package de.teamhardcore.pvp.managers;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.model.extras.EnumPerk;
import de.teamhardcore.pvp.model.teleport.TPDelay;
import de.teamhardcore.pvp.model.teleport.TPRequest;
import de.teamhardcore.pvp.utils.StringDefaults;
import de.teamhardcore.pvp.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import java.util.*;

public class Manager {

    private final Main plugin;

    private final HashMap<Player, Long> teleportCooldowns = new HashMap<>();
    private final HashMap<Player, TPRequest> teleportRequests = new HashMap<>();
    private final HashMap<Player, TPDelay> teleportDelays = new HashMap<>();
    private final HashMap<Player, Location> lastPositions = new HashMap<>();
    private final HashMap<Player, Long> healCooldown = new HashMap<>();
    private final HashMap<EnumPerk, Set<Player>> perkEffects = new HashMap<>();

    private final ArrayList<Player> playersInEnderchest = new ArrayList<>();
    private final ArrayList<Player> playersInVanish = new ArrayList<>();

    public Manager(Main plugin) {
        this.plugin = plugin;

        for (EnumPerk perk : EnumPerk.values())
            this.perkEffects.put(perk, new HashSet<>());

        startTablistTask();
        startPerkUpdater();
    }

    private void startTablistTask() {
        this.plugin.getServer().getScheduler().runTaskTimer(this.plugin, () -> Bukkit.getOnlinePlayers().forEach(players -> {
            Util.sendHeaderFooter(players, "\n " + StringDefaults.SERVER_NAME + "\n", "\n§9§lPing§8: §7" + players.spigot().getPing() + "\n");
        }), 20L, 20L);
    }

    private void startPerkUpdater() {
        Bukkit.getScheduler().runTaskTimer(this.plugin, () -> {
            for (Set<Player> players : this.perkEffects.values())
                for (Player player : players)
                    refreshPerkEffects(player);
        }, 200L, 200L);
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

    public void addPerkEffect(Player player, EnumPerk perk) {
        if (!this.perkEffects.containsKey(perk)) return;

        Set<Player> players = this.perkEffects.get(perk);
        if (players.contains(player)) return;
        players.add(player);
        refreshPerkEffects(player);
    }

    public void removePerkEffect(Player player, EnumPerk perk) {
        if (!this.perkEffects.containsKey(perk)) return;
        Set<Player> players = this.perkEffects.get(perk);
        if (!players.contains(player)) return;
        players.remove(player);
    }

    public void refreshPerkEffects(Player player) {
        for (Map.Entry<EnumPerk, Set<Player>> entry : this.perkEffects.entrySet()) {
            if (!entry.getValue().contains(player)) continue;
            if (entry.getKey().getType() != null) {
                EnumPerk perk = entry.getKey();
                PotionEffect effect = new PotionEffect(perk.getType(), Integer.MAX_VALUE, perk.getAmplifier(), true);
                player.addPotionEffect(effect);
            }
        }
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

    public Main getPlugin() {
        return plugin;
    }
}
