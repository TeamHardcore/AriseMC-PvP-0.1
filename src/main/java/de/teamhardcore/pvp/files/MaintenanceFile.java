/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.files;

import de.teamhardcore.pvp.utils.UUIDFetcher;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MaintenanceFile extends FileBase {

    private final List<String> players;
    private boolean maintenance;

    public MaintenanceFile() {
        super("", "maintenance");
        this.maintenance = true;
        this.players = new ArrayList<>();

        writeDefaults();
        loadData();
    }

    private void loadData() {
        FileConfiguration cfg = getConfig();
        if (cfg.get("Maintenance") == null) return;

        this.maintenance = cfg.getBoolean("Maintenance");
        this.players.addAll(cfg.getStringList("Maintenance.Players"));
    }

    private void writeDefaults() {
        FileConfiguration cfg = getConfig();
        cfg.addDefault("Maintenance", true);
        cfg.addDefault("Maintenance.Players", new ArrayList<>());

        cfg.options().copyDefaults(true);
        saveConfig();
    }

    public void setMaintenance(boolean maintenance) {
        this.maintenance = maintenance;
        FileConfiguration cfg = getConfig();
        cfg.set("Maintenance", maintenance);
        saveConfig();
    }

    public void addPlayer(UUID uuid) {
        FileConfiguration cfg = getConfig();
        if (this.players.contains(uuid.toString())) return;
        this.players.add(uuid.toString());
        cfg.set("Maintenance.Players", this.players);
        saveConfig();
    }

    public void removePlayer(UUID uuid) {
        FileConfiguration cfg = getConfig();
        if (!this.players.contains(uuid.toString())) return;
        this.players.remove(uuid.toString());
        cfg.set("Maintenance.Players", this.players);
        saveConfig();
    }

    public boolean isMaintenance() {
        return maintenance;
    }

    public List<String> getPlayers() {
        return players;
    }
}
