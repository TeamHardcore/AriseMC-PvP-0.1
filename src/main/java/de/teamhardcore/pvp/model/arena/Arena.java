/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.arena;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.model.arena.options.ArenaClanOption;
import de.teamhardcore.pvp.model.clan.Clan;
import de.teamhardcore.pvp.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.*;

public class Arena {

    private final String name;

    private Location min;
    private Location max;
    private Location hologramLocation;

    private Hologram hologram;

    private final Set<ArenaOptionImpl> options;
    private final ArenaStatistics arenaStatistics;

    public Arena(String name) {
        this.name = name;
        this.options = new HashSet<>();
        this.arenaStatistics = new ArenaStatistics(this);

        loadData();
        refreshHologram();
    }

    public Arena(String name, Location hologramLocation, Location min, Location max) {
        this.name = name;
        this.min = min;
        this.max = max;
        this.hologramLocation = hologramLocation;
        this.options = new HashSet<>();
        this.arenaStatistics = new ArenaStatistics(this);

        saveData();
        refreshHologram();
    }

    private void spawnHologram() {
        if (this.hologram != null) return;

        this.hologram = HologramsAPI.createHologram(Main.getInstance(), this.hologramLocation.clone().add(0, 3, 0));
        this.hologram.appendTextLine("§c§lArena§8: §7" + this.name);

        ArenaOptionImpl clanOption = findArenaOption(Main.getInstance().getArenaManager().getOption("general", "clan"));
        ArenaOptionImpl playerOption = findArenaOption(Main.getInstance().getArenaManager().getOption("general", "player"));

        if (clanOption != null && hasArenaOption(clanOption)) {
            if (this.arenaStatistics.getClanStatistic() == null || this.arenaStatistics.getClanStatistic()[0] == null || this.arenaStatistics.getClanStatistic()[1] == null
                    || (!(this.arenaStatistics.getClanStatistic()[0] instanceof Clan)) || (!(this.arenaStatistics.getClanStatistic()[1] instanceof Integer))) {

                this.hologram.appendTextLine("§7Die Arena wurde von noch keinem Clan eingenommen.");
            } else {
                Clan clan = (Clan) this.arenaStatistics.getClanStatistic()[0];
                int clanKills = (int) this.arenaStatistics.getClanStatistic()[1];
                this.hologram.appendTextLine("§7Vom §8'" + clan.getNameColor() + clan.getName() + "§8' §7Clan eingenommen. (§6" + Util.formatNumber(clanKills) + " " + (clanKills == 1 ? "Kill" : "Kills") + "§7)");
            }
        }

        if (playerOption != null && hasArenaOption(playerOption)) {
            if (this.arenaStatistics.getPlayerStatistic() == null || this.arenaStatistics.getPlayerStatistic()[0] == null || this.arenaStatistics.getPlayerStatistic()[1] == null
                    || (!(this.arenaStatistics.getPlayerStatistic()[0] instanceof UUID)) || (!(this.arenaStatistics.getPlayerStatistic()[1] instanceof Integer))) {

                this.hologram.appendTextLine("§7Die Arena wurde von noch keinem Spieler eingenommen.");
            } else {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer((UUID) this.arenaStatistics.getPlayerStatistic()[0]);
                int playerKills = (int) this.arenaStatistics.getPlayerStatistic()[1];

                if (offlinePlayer == null || !offlinePlayer.hasPlayedBefore()) {
                    this.hologram.appendTextLine("§cFehler beim anzeigen des Spielers");
                } else {
                    this.hologram.appendTextLine("§7Gefährlichster Spieler§8: §c" + offlinePlayer.getName() + " §7(§6" + Util.formatNumber(playerKills) + " " + (playerKills == 1 ? "Kill" : "Kills") + "§7)");
                }
            }
        }
    }

    public void despawnHologram() {
        if (this.hologram == null) return;
        this.hologram.delete();
        this.hologram = null;
    }

    public void refreshHologram() {
        if (this.hologram != null) despawnHologram();
        spawnHologram();
    }

    public boolean isInside(Location location) {
        int maxX = Math.max(this.min.getBlockX(), this.max.getBlockX());
        int minX = Math.min(this.min.getBlockX(), this.max.getBlockX());
        int maxY = Math.max(this.min.getBlockY(), this.max.getBlockY());
        int minY = Math.min(this.min.getBlockY(), this.max.getBlockY());
        int maxZ = Math.max(this.min.getBlockZ(), this.max.getBlockZ());
        int minZ = Math.min(this.min.getBlockZ(), this.max.getBlockZ());

        return (location.getWorld().equals(this.max.getWorld())) &&
                (location.getX() >= minX) && (location.getX() <= maxX) &&
                (location.getY() >= minY) && (location.getY() <= maxY) &&
                (location.getZ() >= minZ) && (location.getZ() <= maxZ);
    }

    public ArenaOptionImpl findArenaOption(ArenaOptionBase option) {
        for (ArenaOptionImpl options : this.options) {
            if (options.getCategory().equalsIgnoreCase(option.getCategory()) && options.getOptionName().equalsIgnoreCase(option.getOptionName()))
                return options;
        }
        return null;
    }

    public boolean hasArenaOption(ArenaOptionBase option) {
        return (findArenaOption(option) != null);
    }

    public void addArenaOption(ArenaOptionImpl option) {
        if (hasArenaOption(option)) return;
        this.options.add(option);
        refreshHologram();
        saveData();
    }

    public void removeArenaOption(ArenaOptionBase option) {
        ArenaOptionImpl optionImpl = findArenaOption(option);
        if (optionImpl == null) return;
        optionImpl.executeOnDestroy();
        this.options.remove(optionImpl);
        refreshHologram();
        saveData();
    }

    public void saveData() {
        FileConfiguration cfg = Main.getInstance().getFileManager().getArenaFile().getConfig();

        if (cfg.get(this.name) != null)
            cfg.set(this.name, null);

        cfg.set(this.name + ".Min", Util.locationToString(this.min));
        cfg.set(this.name + ".Max", Util.locationToString(this.max));
        cfg.set(this.name + ".Hologram", Util.locationToString(this.hologramLocation));

        if (!this.options.isEmpty()) {
            List<String> serializedOptions = new ArrayList<>();
            for (ArenaOptionImpl option : this.options) {
                serializedOptions.add(ArenaOptionImpl.serialize(option));
            }
            cfg.set(this.name + ".Options", serializedOptions);
        }

        Main.getInstance().getFileManager().getArenaFile().saveConfig();
    }

    public void loadData() {
        FileConfiguration cfg = Main.getInstance().getFileManager().getArenaFile().getConfig();

        if (cfg.get(this.name) == null) return;

        this.min = Util.stringToLocation(cfg.getString(this.name + ".Min"));
        this.max = Util.stringToLocation(cfg.getString(this.name + ".Max"));
        this.hologramLocation = Util.stringToLocation(cfg.getString(this.name + ".Hologram"));
        if (cfg.get(this.name + ".Options") != null)
            for (String optionStr : cfg.getStringList(this.name + ".Options")) {
                ArenaOptionImpl option = ArenaOptionImpl.deserialize(this, optionStr);
                if (option != null)
                    this.options.add(option);
            }
    }

    public void deleteData() {
        FileConfiguration cfg = Main.getInstance().getFileManager().getArenaFile().getConfig();
        if (cfg.get("Arena." + this.name) != null)
            cfg.set("Arena." + this.name, null);
    }

    public String getName() {
        return name;
    }

    public Location getMin() {
        return min;
    }

    public Location getMax() {
        return max;
    }

    public ArenaStatistics getArenaStatistics() {
        return arenaStatistics;
    }

    public Set<ArenaOptionImpl> getOptions() {
        return options;
    }
}
