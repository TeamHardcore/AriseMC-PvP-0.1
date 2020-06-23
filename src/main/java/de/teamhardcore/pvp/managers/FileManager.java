package de.teamhardcore.pvp.managers;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.files.ConfigFile;
import de.teamhardcore.pvp.files.DuelFile;
import de.teamhardcore.pvp.files.SpawnerFile;
import de.teamhardcore.pvp.files.WarpFile;

public class FileManager {

    private final Main plugin;
    private final WarpFile warpFile;
    private final ConfigFile configFile;
    private final SpawnerFile spawnerFile;
    private final DuelFile duelFile;

    public FileManager(Main plugin) {
        this.plugin = plugin;
        this.configFile = new ConfigFile();
        this.warpFile = new WarpFile();
        this.spawnerFile = new SpawnerFile();
        this.duelFile = new DuelFile();
    }

    public DuelFile getDuelFile() {
        return duelFile;
    }

    public SpawnerFile getSpawnerFile() {
        return spawnerFile;
    }

    public ConfigFile getConfigFile() {
        return configFile;
    }

    public WarpFile getWarpFile() {
        return warpFile;
    }

    public Main getPlugin() {
        return plugin;
    }
}
